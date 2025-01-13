package me.xiaoying.liveget.authorizeserver.file;

/**
 * File of Language.yml
 */
public class FileLanguage extends SFile {
    public static String COMMAND_MISSING_PERMISSION;

    public FileLanguage() {
        super("Language.yml");
    }

    @Override
    public void onLoad() {
        FileLanguage.COMMAND_MISSING_PERMISSION = this.getConfiguration().getString("Command.MissingPermission");
    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onDisable() {

    }
}