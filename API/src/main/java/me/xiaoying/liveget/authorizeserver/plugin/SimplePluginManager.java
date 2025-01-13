package me.xiaoying.liveget.authorizeserver.plugin;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.event.*;
import me.xiaoying.liveget.authorizeserver.server.Server;
import me.xiaoying.liveget.authorizeserver.utils.Preconditions;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Plugin PluginManager Simple
 */
public class SimplePluginManager implements PluginManager {
    private final Server server;
    private final List<Plugin> plugins = new ArrayList<>();
    private final Map<String, Plugin> lookupNames = new HashMap<>();
    private final Map<Pattern, PluginLoader> fileAssociations = new HashMap<>();

    public SimplePluginManager(Server instance) {
        this.server = instance;
    }

    @Override
    public void registerInterface(Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        PluginLoader pluginLoader;
        if (!PluginLoader.class.isAssignableFrom(loader))
            throw new IllegalArgumentException(String.format("Class %s does not implement interface PluginLoader", loader.getName()));

        try {
            Constructor<? extends PluginLoader> constructor = loader.getConstructor(Server.class);
            pluginLoader = constructor.newInstance(this.server);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        Pattern[] patterns = pluginLoader.getPluginFileFilters();

        synchronized (this) {
            for (Pattern pattern : patterns)
                this.fileAssociations.put(pattern, pluginLoader);
        }
    }

    @Override
    public Plugin getPlugin(String name) {
        return this.lookupNames.get(name.replace(' ', '_'));
    }

    @Override
    public Plugin[] getPlugins() {
        return new ArrayList<>(this.plugins).toArray(new Plugin[0]);
    }

    @Override
    public boolean isPluginEnabled(String name) {
        Plugin plugin = this.getPlugin(name);
        return this.isPluginEnabled(plugin);
    }

    @Override
    public boolean isPluginEnabled(Plugin plugin) {
        return plugin != null && this.plugins.contains(plugin) && plugin.isEnabled();
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        Preconditions.checkArgument(file != null, "File cannot be null");
        Set<Pattern> filters = this.fileAssociations.keySet();
        Plugin plugin = null;

        for (Pattern filter : filters) {
            String name = file.getName();

            Matcher match = filter.matcher(name);
            if (match.find()) {
                PluginLoader loader = this.fileAssociations.get(filter);
                plugin = loader.loadPlugin(file);
            }
        }

        if (plugin != null) {
            this.plugins.add(plugin);
            this.lookupNames.put(plugin.getDescription().getName(), plugin);
        }

        return plugin;
    }

    @Override
    public Plugin[] loadPlugins(File directory) {
        Preconditions.checkArgument(directory != null, "Directory cannot be null");
        Preconditions.checkArgument(directory.isDirectory(), "Directory must be a directory");

        assert directory.listFiles() != null;

        if (directory.listFiles() == null || Objects.requireNonNull(directory.listFiles()).length == 0)
            return null;

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            try {
                this.loadPlugin(file);
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>(this.plugins).toArray(new Plugin[0]);
    }

    @Override
    public void disablePlugins() {
        Plugin[] plugins = this.getPlugins();

        for (Plugin plugin : plugins)
            this.disablePlugin(plugin);
    }

    @Override
    public void clearPlugins() {
        synchronized (this) {
            this.disablePlugins();
            this.plugins.clear();
            this.lookupNames.clear();
            this.fileAssociations.clear();
            HandlerList.unregisterAll();
        }
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        if (plugin.isEnabled())
            return;

        plugin.getPluginloader().enablePlugin(plugin);
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        if (!plugin.isEnabled())
            return;

        plugin.getPluginloader().disablePlugin(plugin);
        HandlerList.unregisterAll(plugin);
    }

    @Override
    public void callEvent(Event event) throws IllegalStateException {
        synchronized (this) {
            this.fireEvent(event);
        }
    }

    private void fireEvent(Event event) {
        HandlerList handlers = event.getHandlers();
        RegisteredListener[] listeners = handlers.getRegisteredListener();
        if (listeners == null)
            return;

        for (RegisteredListener registration : listeners) {
            if (registration.getPlugin() == null || !registration.getPlugin().isEnabled())
                continue;

            try {
                registration.callEvent(event);
            } catch (AuthorNagException e) {
                LACore.getLogger().warn(e.getMessage());
            } catch (Throwable throwable) {
//                LACore.getLogger().warn("Could not pass event {} to {}", event.getClass().getName(), registration.getPlugin().getDescription().getName());
                throwable.printStackTrace();
            }
        }
    }


    @Override
    public void registerEvents(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
        }

        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginloader().createRegisteredListeners(listener, plugin).entrySet())
            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
    }

    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin) {
        Preconditions.checkArgument(listener != null, "Listener cannot be null");
        Preconditions.checkArgument(priority != null, "Priority cannot be null");
        Preconditions.checkArgument(executor != null, "Executor cannot be null");
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");

        if (!plugin.isEnabled())
            throw new IllegalPluginAccessException("Plugin attempted to register " + event + " while not enabled");

        this.getEventListeners(event).register(new RegisteredListener(listener, executor, priority, plugin));
    }

    /**
     * Register listener by server
     *
     * @param event Event
     * @param listener Listener
     * @param priority Priority
     * @param executor EventExecutor
     */
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor) {
        Preconditions.checkArgument(listener != null, "Listener cannot be null");
        Preconditions.checkArgument(priority != null, "Priority cannot be null");
        Preconditions.checkArgument(executor != null, "Executor cannot be null");

        this.getEventListeners(event).register(new RegisteredListener(listener, executor, priority, null));
    }

    private HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = this.getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            if (!Modifier.isStatic(method.getModifiers()))
                throw new IllegalAccessException("getHandlerList must be static");

            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException("Error while registering listener for event type " + type.toString() + ": " + e);
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList", new Class[0]);
            return clazz;
        } catch (NoSuchMethodException noSuchMethodException) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass()))
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
        }
    }
}