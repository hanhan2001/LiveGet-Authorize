package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.logger.Logger;
import me.xiaoying.sql.SqlFactory;

import java.io.File;

public class LACore {
    private static SqlFactory sqlFactory = null;

    private static final Logger logger = new Logger();

    public static File getDataFolder() {
        return new File(System.getProperty("user.dir"));
    }

    public static Logger getLogger() {
        return LACore.logger;
    }

    public static void setSqlFactory(SqlFactory sqlFactory) {
        if (LACore.sqlFactory != null)
            return;

        LACore.sqlFactory = sqlFactory;
    }

    public static SqlFactory gegSqlFactory() {
        return LACore.sqlFactory;
    }
}