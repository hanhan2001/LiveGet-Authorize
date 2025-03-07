package me.xiaoying.liveget.authorizeserver.file;

/**
 * Message.yml
 */
public class FileMessage extends SFile {
    public static String USER_CREATE_FREQUENTLY,
            USER_CREATE_EXISTS_EMAIL,
            USER_CREATE_EXISTS_PHONE,
            USER_CREATE_WRONG_PASSWORD;

    public static String PARAMETER_MISSING_PARAMETER;

    public FileMessage() {
        super("Message.yml");
    }

    @Override
    public void onLoad() {
        FileMessage.USER_CREATE_FREQUENTLY = this.getConfiguration().getString("User.Create.Frequently");
        FileMessage.USER_CREATE_EXISTS_EMAIL = this.getConfiguration().getString("User.Create.ExistsEmail");
        FileMessage.USER_CREATE_EXISTS_PHONE = this.getConfiguration().getString("User.Create.ExistsPhone");
        FileMessage.USER_CREATE_WRONG_PASSWORD = this.getConfiguration().getString("User.Create.WrongPassword");

        FileMessage.PARAMETER_MISSING_PARAMETER = this.getConfiguration().getString("Parameter.MissingParameter");
    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onDisable() {

    }
}