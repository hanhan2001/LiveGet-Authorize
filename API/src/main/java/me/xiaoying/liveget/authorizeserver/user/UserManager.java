package me.xiaoying.liveget.authorizeserver.user;

/**
 * Manager of user
 */
public interface UserManager {
    /**
     * Get user by user's UUID
     *
     * @param uuid User's UUID
     * @return User
     */
    User getUserByUUID(String uuid);

    /**
     * Get user by user's account
     *
     * @param account User's account
     * @return User
     */
    User getUserByAccount(String account);

    /**
     * Get user by user's email
     *
     * @param email User's e-mail
     * @return User
     */
    User getUserByEmail(String email);

    /**
     * Get user by user's phone number
     *
     * @param phoneNumber User's phone number
     * @return User
     */
    User getUserByPhoneNumber(long phoneNumber);

    /**
     * Create a new user
     *
     * @param name User's name
     * @param account User's account
     * @param password User's password
     * @param phoneNumber User's phone number
     * @param email User's email
     * @param group User's group
     * @return User
     */
    User createUser(String name, String account, String password, long phoneNumber, String email, String group);

    /**
     * Delete user
     *
     * @param user User entity
     */
    void delUser(User user);
}