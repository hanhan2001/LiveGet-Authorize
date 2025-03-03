package me.xiaoying.liveget.authorizeserver.plugin;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.configuration.YamlConfiguration;
import me.xiaoying.liveget.authorizeserver.configuration.serialization.ConfigurationSerializable;
import me.xiaoying.liveget.authorizeserver.configuration.serialization.ConfigurationSerialization;
import me.xiaoying.liveget.authorizeserver.event.*;
import me.xiaoying.liveget.authorizeserver.server.Server;
import me.xiaoying.liveget.authorizeserver.utils.Preconditions;
import me.xiaoying.liveget.authorizeserver.utils.ZipUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Plugin Loader java
 */
public class JavaPluginLoader implements PluginLoader {
    private final Server server;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();
    private final Map<String, PluginClassLoader> loaders = new LinkedHashMap<>();
    private final Pattern[] fileFilters = new Pattern[] {Pattern.compile("\\.jar$")};
    private final List<RegisteredListener> registeredListeners = new ArrayList<>();

    public JavaPluginLoader(Server instance) {
        Preconditions.checkArgument(instance != null, "Server cannot be null");
        this.server = instance;
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException {
        Preconditions.checkArgument(file != null, "File cannot be null");

        PluginDescriptionFile description;
        PluginClassLoader loader;

        if (!file.exists())
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exists"));

        try {
            description = this.getPluginDescription(file);
        } catch (InvalidDescription e) {
            throw new RuntimeException(e);
        }

        File dataFolder = new File(file.getParentFile(), description.getName());

        try {
            loader = new PluginClassLoader(this, this.getClass().getClassLoader(), description, dataFolder, file, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loader.plugin;
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescription {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(ZipUtil.getFile(file.getPath(), "plugin.yml"));
        String name = yamlConfiguration.getString("name");
        Preconditions.checkNotNull(name, "Plugin name cannot be null");
        String main = yamlConfiguration.getString("main");
        Preconditions.checkNotNull(main, "Main class cannot be null");
        String version = yamlConfiguration.getString("version");
        Preconditions.checkNotNull(version, "Plugin version cannot be null");
        List<String> authors = new ArrayList<>();
        if (yamlConfiguration.getString("author") != null && !yamlConfiguration.getString("author").isEmpty())
            authors.add(yamlConfiguration.getString("author"));

        if (yamlConfiguration.getStringList("authors") != null && !yamlConfiguration.getStringList("authors").isEmpty())
            authors.addAll(yamlConfiguration.getStringList("authors"));
        return new PluginDescriptionFile(name, main, version, authors.toArray(new String[0]), main);
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "Plugin can not be null");
        Preconditions.checkArgument(listener != null, "Listener can not be null");
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();

        HashSet<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0F);
            Method[] var11 = publicMethods;
            int var10 = publicMethods.length;

            Method method;
            int var9;
            for(var9 = 0; var9 < var10; ++var9) {
                method = var11[var9];
                methods.add(method);
            }

            var11 = privateMethods;
            var10 = privateMethods.length;

            for(var9 = 0; var9 < var10; ++var9) {
                method = var11[var9];
                methods.add(method);
            }
        } catch (NoClassDefFoundError e) {
            LACore.getLogger().warn("Plugin {} has failed to register events for {} because {} dose not exists.", plugin.getDescription().getName(), listener.getClass().getName(), e.getMessage());
            return ret;
        }

        Iterator<Method> iterator = methods.iterator();

        while(true) {
            while(true) {
                Method method;
                EventHandler eh;
                do {
                    do {
                        do {
                            if (!iterator.hasNext())
                                return ret;

                            method = iterator.next();
                            eh = method.getAnnotation(EventHandler.class);
                        } while (eh == null);
                    } while (method.isBridge());
                } while (method.isSynthetic());

                Class<?> checkClass;
                if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                    LACore.getLogger().warn("{} attempted to register an invalid EventHandler method signature \"{}\" in {}", plugin.getDescription().getName(), method.toGenericString(), listener.getClass().getName());
                    return null;
                }

                final
                Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
                method.setAccessible(true);
                Set<RegisteredListener> eventSet = (Set) ret.get(eventClass);
                if (eventSet == null) {
                    eventSet = new HashSet<>();
                    ret.put(eventClass, eventSet);
                }
                EventExecutor executor = (listener1, event) -> {};
                eventSet.add(new RegisteredListener(listener, executor, EventPriority.LOWEST, plugin));
            }
        }
    }

    @Override
    public void delRegisteredListener(Listener listener) {
        Iterator<RegisteredListener> iterator = this.registeredListeners.iterator();
        RegisteredListener registeredListener;
        while (iterator.hasNext() && (registeredListener = iterator.next()) != null) {
            if (registeredListener.getListener() != listener)
                continue;

            iterator.remove();
        }
    }

    @Override
    public void delRegisteredListener(Plugin plugin) {
        Iterator<RegisteredListener> iterator = this.registeredListeners.iterator();
        RegisteredListener registeredListener;
        while (iterator.hasNext() && (registeredListener = iterator.next()) != null) {
            if (registeredListener.getPlugin() == null || registeredListener.getPlugin() != plugin)
                continue;

            iterator.remove();
        }
    }

    @Override
    public void delRegisteredListeners() {
        this.registeredListeners.clear();
    }

    @Override
    public List<RegisteredListener> getRegisteredListener() {
        return this.registeredListeners;
    }

    @Override
    public Pattern[] getPluginFileFilters() {
        return this.fileFilters;
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        Preconditions.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");

        if (plugin.isEnabled())
            return;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < plugin.getDescription().getAuthors().length; i++) {
            stringBuilder.append(plugin.getDescription().getAuthors()[i]);

            if (i == plugin.getDescription().getAuthors().length - 1)
                break;

            stringBuilder.append(", ");
        }

        String message = String.format("Enabling %s %s by %s", plugin.getDescription().getName(), plugin.getDescription().getVersion(), stringBuilder);
        LACore.getLogger().info(message);

        JavaPlugin jPlugin = (JavaPlugin) plugin;
        String pluginName = jPlugin.getDescription().getName();

        if (!this.loaders.containsKey(pluginName))
            this.loaders.put(pluginName, (PluginClassLoader) jPlugin.getClassLoader());

        try {
            jPlugin.setEnabled(true);
        } catch (Throwable ex) {
//            LACore.getLogger().warn("Error occurred while enabling {} (Is it up to date?)\n{}", plugin.getDescription().getName(), ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        Preconditions.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");

        if (!plugin.isEnabled())
            return;

        String message = String.format("Disabling %s", plugin.getDescription().getName());
        LACore.getLogger().info(message);

//        this.server.getPluginManager().callEvent(new PluginDisableEvent(plugin));

        JavaPlugin jPlugin = (JavaPlugin) plugin;
        ClassLoader cloader = jPlugin.getClassLoader();
        try {
            jPlugin.setEnabled(false);
        } catch (Throwable ex) {
            LACore.getLogger().warn("Error occurred while disabling {} (Is it up to date?)\n{}", plugin.getDescription().getName(), ex.getMessage());
        }

        this.loaders.remove(jPlugin.getDescription().getName());
        if (!(cloader instanceof PluginClassLoader))
            return;

        PluginClassLoader loader = (PluginClassLoader) cloader;
        Set<String> names = loader.getClasses();

        for (String name : names)
            removeClass(name);
    }

    private void removeClass(String name) {
        Class<?> clazz = this.classes.remove(name);

        try {
            if (clazz != null && ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.unregisterClass(serializable);
            }
        } catch (NullPointerException nullPointerException) {}
    }

    Class<?> getClassByName(String name) {
        Class<?> cachedClass = this.classes.get(name);
        if (cachedClass != null)
            return cachedClass;

        for (String s : this.loaders.keySet()) {
            PluginClassLoader loader = this.loaders.get(s);

            try { cachedClass = loader.findClass(name, false); } catch (ClassNotFoundException e) {}
            if (cachedClass != null)
                return cachedClass;
        }
        return null;
    }

    void setClass(String name, Class<?> clazz) {
        if (this.classes.containsKey(name))
            return;

        this.classes.put(name, clazz);
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.registerClass(serializable);
        }
    }
}