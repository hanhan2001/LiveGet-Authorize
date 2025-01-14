package me.xiaoying.liveget.authorizeserver.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.LiveGetAuthorizeServer;
import me.xiaoying.liveget.authorizeserver.file.FileConfig;
import me.xiaoying.liveget.authorizeserver.permission.Permission;
import me.xiaoying.liveget.authorizeserver.utils.DateUtil;
import me.xiaoying.sql.entity.Column;
import me.xiaoying.sql.entity.Record;
import me.xiaoying.sql.entity.Table;
import me.xiaoying.sql.sentence.*;

import java.io.Console;
import java.text.DecimalFormat;
import java.util.*;

public class SimpleUserManager implements UserManager {
    private final Map<String, Integer> uuidUsers = new HashMap<>();
    private final Map<String, Integer> emailUsers = new HashMap<>();
    private final Map<String, Integer> accountUsers = new HashMap<>();
    private final Map<Integer, User> hashUsers = new HashMap<>();

    private int user_count = 0;

    public SimpleUserManager() {
        List<Column> user_columns = new ArrayList<>();
        user_columns.add(new Column("uuid", "varchar", 9));
        user_columns.add(new Column("name", "varchar", 255));
        user_columns.add(new Column("account", "varchar", 255));
        user_columns.add(new Column("password", "varchar", 255));
        user_columns.add(new Column("phone_number", "int", 11));
        user_columns.add(new Column("email", "varchar", 255));
        user_columns.add(new Column("group", "varchar", 255));
        user_columns.add(new Column("permissions", "longtext", 0));
        user_columns.add(new Column("ip", "varchar", 15));
        user_columns.add(new Column("register_time", "varchar", 255));
        user_columns.add(new Column("last_login_time", "varchar", 255));
        user_columns.add(new Column("photo_path", "varchar", 255));
        LiveGetAuthorizeServer.getSqlFactory().run(new Create(user_columns, FileConfig.SETTING_TABLE_USER));

        // Determine whither the table if empty.
        List<Table> select_tables = LiveGetAuthorizeServer.getSqlFactory().run(new Select(Collections.singletonList("uuid"), FileConfig.SETTING_TABLE_USER));
        if (select_tables.get(0).getRecords().isEmpty()) {
            LACore.getLogger().info("User list is empty, need to create a default admin account.");

            Scanner scanner = new Scanner(System.in);
            Console console = System.console();

            String account, password, email;
            long phoneNumber;
            LACore.getLogger().print("Account: ");
            account = console.readLine();

            LACore.getLogger().print("Password: ");
            password = String.valueOf(console.readPassword());

            LACore.getLogger().print("PhoneNumber: ");
            phoneNumber = scanner.nextLong();

            LACore.getLogger().print("Email: ");
            email = console.readLine();

            this.createUser(account, account, password, phoneNumber, email, "admin");

            this.user_count = 1;
            LACore.getLogger().info("Complete...");
        } else this.user_count = select_tables.get(0).getRecords().size();
    }

    @Override
    public User getUserByUUID(String uuid) {
        return this.uuidUsers.containsKey(uuid) ? this.getUserByHashcode(this.uuidUsers.get(uuid)) : this.findUser("uuid", uuid);
    }

    @Override
    public User getUserByAccount(String account) {
        return this.accountUsers.containsKey(account) ? this.getUserByHashcode(this.accountUsers.get(account)) : this.findUser("account", account);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.emailUsers.containsKey(email) ? this.getUserByHashcode(this.emailUsers.get(email)) : this.findUser("email", email);
    }

    /**
     * Call the method when the cache contains the hashcode
     *
     * @param hashcode User's hashcode
     * @return User
     */
    public User getUserByHashcode(int hashcode) {
        return this.hashUsers.get(hashcode);
    }

    private User findUser(String key, String value) {
        if (!key.equalsIgnoreCase("uuid") && !key.equalsIgnoreCase("account") && !key.equalsIgnoreCase("email"))
            return null;

        List<String> columns = new ArrayList<>();
        columns.add("uuid");
        columns.add("name");
        columns.add("account");
        columns.add("password");
        columns.add("phone_number");
        columns.add("email");
        columns.add("group");
        columns.add("permissions");
        columns.add("ip");
        columns.add("register_time");
        columns.add("last_login_time");
        columns.add("photo_path");
        Select select = new Select(columns, FileConfig.SETTING_TABLE_USER);
        List<Table> tables = LiveGetAuthorizeServer.getSqlFactory().run(select.condition(new Condition(key, value, Condition.Type.EQUAL)));
        if (tables.isEmpty())
            return null;
        Record record = tables.get(0).getRecords().get(0);

        List<Permission> permissions = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(record.get("permissions").toString()).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Date save = null, over = null;

            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            jsonObject.get("permission");
            if (jsonObject.has("save")) save = DateUtil.parse(jsonObject.get("save").getAsString(), FileConfig.SETTING_DATEFORMAT);
            if (jsonObject.has("save")) over = DateUtil.parse(jsonObject.get("over").getAsString(), FileConfig.SETTING_DATEFORMAT);

            permissions.add(new ServerPermission(jsonObject.get("permission").toString(), save, over));
        }

        ServerUser user = new ServerUser(record.get("uuid").toString(), record.get("name").toString(), record.get("account").toString(), record.get("password").toString(), Long.parseLong(record.get("phone_number").toString()), record.get("email").toString(), record.get("group").toString(), permissions, record.get("ip").toString(), DateUtil.parse(record.get("registerTime").toString(), FileConfig.SETTING_DATEFORMAT), DateUtil.parse(record.get("lastLoginTime").toString(), FileConfig.SETTING_DATEFORMAT));
        this.recordUser(user);
        return user;
    }

    @Override
    public User createUser(String name, String account, String password, long phoneNumber, String email, String group) {
        ServerUser user = new ServerUser(new DecimalFormat("000000000").format(this.user_count), name, account, password, phoneNumber, email, group, new ArrayList<>(), "0.0.0.0", new Date(), new Date());

        JsonArray jsonArray = new JsonArray();
        if (user.getPermissions().isEmpty())
            jsonArray.add("");
        else
            user.getPermissions().forEach(permission -> jsonArray.add(permission.toString()));

        Insert insert = new Insert(FileConfig.SETTING_TABLE_USER);
        insert.insert(user.getUUID(), user.getName(), user.getAccount(), user.getPassword(), user.getPhoneNumber(), user.getEmail(), user.getGroup(), jsonArray.getAsString(), user.getIP(), DateUtil.getDate(user.getRegisterTime(), FileConfig.SETTING_DATEFORMAT), DateUtil.getDate(user.getLastLoginTime(), FileConfig.SETTING_DATEFORMAT), "");
        LiveGetAuthorizeServer.getSqlFactory().run(insert);
        this.recordUser(user);
        return user;
    }

    @Override
    public void delUser(User user) {
        this.uuidUsers.remove(user.getUUID());
        this.emailUsers.remove(user.getEmail());
        this.accountUsers.remove(user.getAccount());

        this.hashUsers.remove(user.hashCode());

        ServerUser serverUser = (ServerUser) user;
        serverUser.setAdmin(false);
        serverUser.setEmail("");
        serverUser.setGroup("default");
        serverUser.setPassword("");
        serverUser.setName("已注销账户");
        serverUser.save();
    }

    /**
     * Record user information in memory
     *
     * @param user User
     */
    private void recordUser(User user) {
        int hashcode = user.hashCode();

        this.uuidUsers.put(user.getUUID(), hashcode);
        this.emailUsers.put(user.getEmail(), hashcode);
        this.accountUsers.put(user.getAccount(), hashcode);

        this.hashUsers.put(hashcode, user);
    }
}