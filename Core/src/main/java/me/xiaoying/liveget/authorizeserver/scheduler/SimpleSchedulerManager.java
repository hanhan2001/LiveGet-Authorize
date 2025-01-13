package me.xiaoying.liveget.authorizeserver.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleSchedulerManager implements ScheduledManager {
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(200);

    private final Map<Integer, Task> tasks = new HashMap<>();
    private boolean interrupt = false;
    private int id = 0;

    public SimpleSchedulerManager() {
        SimpleSchedulerManager.executorService.scheduleAtFixedRate(() -> {
            Iterator<Integer> iterator = this.tasks.keySet().iterator();

            while (iterator.hasNext()) {
                if (this.interrupt)
                    return;

                int id = iterator.next();
                Task task = this.tasks.get(id);

                if (task.isInterrupt()) {
                    iterator.remove();
                    continue;
                }

                if (!task.ready() || !task.finished())
                    continue;

                task.finished(false);
                task.run();
            }
        }, 0, 100, TimeUnit.MICROSECONDS);
    }

    @Override
    public void cancelTask(int task) {
        Task t;

        if ((t = this.tasks.get(task)) == null)
            return;

        t.cancel();
    }

    public int scheduleSyncDelayedTask(Runnable runnable, long delay) {
        Task task = new Task(this.id++, Task.TaskType.SYNC_RUN, runnable, delay, 0);
        this.tasks.put(task.getId(), task);
        return task.getId();
    }

    public int scheduleSyncRepeatingTask(Runnable runnable, long delay, long period) {
        Task task = new Task(this.id++, Task.TaskType.SYNC_REPEAT, runnable, delay, period);
        this.tasks.put(task.getId(), task);
        return task.getId();
    }


    public int scheduleAsyncDelayedTask(Runnable runnable) {
        Task task = new Task(this.id++, Task.TaskType.ASYNC_RUN, runnable, 0, 0);
        this.tasks.put(task.getId(), task);
        return task.getId();
    }

    public int scheduleAsyncDelayedTask(Runnable runnable, long delay) {
        Task task = new Task(this.id++, Task.TaskType.ASYNC_RUN, runnable, delay, 0);
        this.tasks.put(task.getId(), task);
        return task.getId();
    }

    public int scheduleAsyncRepeatingTask(Runnable runnable, long delay, long period) {
        Task task = new Task(this.id++, Task.TaskType.ASYNC_REPEAT, runnable, delay, period);
        this.tasks.put(task.getId(), task);
        return task.getId();
    }

    public void stop() {
        this.interrupt = true;
    }

    protected static ScheduledExecutorService getExecutorService() {
        return SimpleSchedulerManager.executorService;
    }

    protected static final class Task {
        private final int id;
        private final TaskType type;
        private final Runnable runnable;
        private final long delay;
        private final long period;
        private boolean finished = true;
        private Date lastRunTime = new Date();
        private boolean interrupt = false;
        private boolean first = true;

        public Task(int id, TaskType type, Runnable runnable, long delay, long period) {
            this.id = id;
            this.type = type;
            this.runnable = runnable;
            this.delay = delay;
            this.period = period;
        }

        public int getId() {
            return this.id;
        }

        public TaskType getType() {
            return this.type;
        }

        public Runnable getRunnable() {
            return this.runnable;
        }

        public long getDelay() {
            return this.delay;
        }

        public long getPeriod() {
            return this.period;
        }

        public boolean finished() {
            return this.finished;
        }

        public void finished(boolean finish) {
            this.finished = finish;
        }

        public boolean isInterrupt() {
            return this.interrupt;
        }

        /**
         * Determine task can run
         *
         * @return Boolean
         */
        public boolean ready() {
            return new Date().getTime() - this.lastRunTime.getTime() >= this.period * 50L;
        }

        public void run() {
            if (this.isInterrupt())
                return;

            // 20tick = 1s = 1000ms
            Runnable run = () -> {
                this.lastRunTime = new Date();

                this.getRunnable().run();

                this.finished(true);

                if (this.getType() == TaskType.SYNC_RUN || this.getType() == TaskType.ASYNC_RUN)
                    this.interrupt = true;
            };

            // Type of sync
            if (this.getType() == TaskType.SYNC_REPEAT || this.getType() == TaskType.SYNC_RUN) {
                if (this.first) {
                    SimpleSchedulerManager.getExecutorService().schedule(run, this.delay * 50L, TimeUnit.MICROSECONDS);
                    this.first = false;
                } else
                    SimpleSchedulerManager.getExecutorService().schedule(run, 0, TimeUnit.MICROSECONDS);
                return;
            }

            new Thread(() -> {
                try {
                    wait(this.delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                run.run();
            }).start();
        }

        public void cancel() {
            this.interrupt = true;
        }

        public enum TaskType {
            SYNC_REPEAT,
            SYNC_RUN,
            ASYNC_REPEAT,
            ASYNC_RUN;
        }
    }
}