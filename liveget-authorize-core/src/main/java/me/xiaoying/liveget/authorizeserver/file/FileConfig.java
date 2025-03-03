package me.xiaoying.liveget.authorizeserver.file;

public class FileConfig extends SFile {
    public static String SERVER_HOST;
    public static int SERVER_PORT;

    public static String SETTING_JWT_KEY_ID,
            SETTING_JWT_PUBLIC_KEY,
            SETTING_JWT_PRIVATE_KEY;

    public static int SETTING_CACHE_USER_TIME, SETTING_CACHE_TOKEN_TIME;
    public static boolean SETTING_CACHE_USER_SAVE, SETTING_CACHE_TOKEN_SAVE;

    public static String SETTING_PASSWORD_ENCRYPT, SETTING_PASSWORD_PASSWORD;

    public static int SETTING_DATA_CACHE_TIME;

    public static String SETTING_DATEFORMAT;

    public static String SETTING_TABLE_PREFIX,
            SETTING_TABLE_USER,
            SETTING_TABLE_GROUP,
            SETTING_TABLE_MODULE,
            SETTING_TABLE_OPTION,
            SETTING_TABLE_TOKEN;

    public static String SETTING_DATA_TYPE,
            SETTING_DATA_MYSQL_HOSTNAME,
            SETTING_DATA_MYSQL_DATABASE,
            SETTING_DATA_MYSQL_USERNAME,
            SETTING_DATA_MYSQL_PASSWORD;
    public static int SETTING_DATA_MYSQL_PORT,
            SETTING_DATA_MYSQL_RECONNECT_TIME,
            SETTING_DATA_MYSQL_RECONNECT_DELAY;

    public static String SETTING_DATA_SQLITE;

    public FileConfig() {
        super("Configuration.yml");
    }

    @Override
    public void onLoad() {
        FileConfig.SERVER_HOST = this.getConfiguration().getString("Server.Host");
        FileConfig.SERVER_PORT = this.getConfiguration().getInt("Server.Port");

        FileConfig.SETTING_JWT_KEY_ID = this.getConfiguration().getString("Setting.Jwt.KeyId");
        FileConfig.SETTING_JWT_PUBLIC_KEY = this.getConfiguration().getString("Setting.Jwt.PublicKey");
        FileConfig.SETTING_JWT_PRIVATE_KEY = this.getConfiguration().getString("Setting.Jwt.PrivateKey");

        FileConfig.SETTING_CACHE_USER_TIME = this.getConfiguration().getInt("Setting.Cache.User.Time");
        FileConfig.SETTING_CACHE_USER_SAVE = this.getConfiguration().getBoolean("Setting.Cache.User.Save");

        FileConfig.SETTING_CACHE_TOKEN_TIME = this.getConfiguration().getInt("Setting.Cache.Token.Time");
        FileConfig.SETTING_CACHE_TOKEN_SAVE = this.getConfiguration().getBoolean("Setting.Cache.Token.Save");

        FileConfig.SETTING_PASSWORD_ENCRYPT = this.getConfiguration().getString("Setting.Password.Encrypt");
        FileConfig.SETTING_PASSWORD_PASSWORD = this.getConfiguration().getString("Setting.Password.Password");

        FileConfig.SETTING_DATA_CACHE_TIME = this.getConfiguration().getInt("Setting.DataCacheTime");

        FileConfig.SETTING_DATEFORMAT = this.getConfiguration().getString("Setting.DateFormat");

        FileConfig.SETTING_TABLE_PREFIX =  this.getConfiguration().getString("Setting.Table.Prefix");
        FileConfig.SETTING_TABLE_USER =  this.getConfiguration().getString("Setting.Table.User");
        FileConfig.SETTING_TABLE_GROUP =  this.getConfiguration().getString("Setting.Table.Group");
        FileConfig.SETTING_TABLE_MODULE = this.getConfiguration().getString("Setting.Table.Module");
        FileConfig.SETTING_TABLE_OPTION = this.getConfiguration().getString("Setting.Table.Option");
        FileConfig.SETTING_TABLE_TOKEN = this.getConfiguration().getString("Setting.Table.Token");

        FileConfig.SETTING_DATA_TYPE = this.getConfiguration().getString("Setting.Data.Type");
        FileConfig.SETTING_DATA_MYSQL_HOSTNAME = this.getConfiguration().getString("Setting.Data.Mysql.Hostname");
        FileConfig.SETTING_DATA_MYSQL_DATABASE = this.getConfiguration().getString("Setting.Data.Mysql.Database");
        FileConfig.SETTING_DATA_MYSQL_USERNAME = this.getConfiguration().getString("Setting.Data.Mysql.Username");
        FileConfig.SETTING_DATA_MYSQL_PASSWORD = this.getConfiguration().getString("Setting.Data.Mysql.Password");
        FileConfig.SETTING_DATA_MYSQL_PORT = this.getConfiguration().getInt("Setting.Data.Mysql.Port");
        FileConfig.SETTING_DATA_MYSQL_RECONNECT_TIME = this.getConfiguration().getInt("Setting.Data.Mysql.ReconnectTime");
        FileConfig.SETTING_DATA_MYSQL_RECONNECT_DELAY = this.getConfiguration().getInt("Setting.Data.Mysql.ReconnectDelay");

        FileConfig.SETTING_DATA_SQLITE = this.getConfiguration().getString("Setting.Data.Sqlite");
    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onDisable() {

    }
}