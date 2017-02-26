package app.models;

import app.helpers.SystemPrivilege;
import app.helpers.Utilities;
import app.obj.Privilege;
import app.obj.User;
import app.obj.UserLevel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User management related model functions
 *
 * @author Isuru
 */
public class UserModel extends Model {

    /**
     * Save user level
     *
     * @param lvl
     * @return
     * @throws Exception
     */
    public static boolean saveUserLevel(UserLevel lvl) throws Exception {
        String query = "INSERT INTO `user_levels` (`level_code`, `level_name`, `level_display_name`, `level_active`) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, lvl.getLevelCode());
            ps.setString(2, lvl.getLevelName());
            ps.setString(3, lvl.getLevelDisplayName());
            ps.setBoolean(4, lvl.isActiveLevel());
            ps.executeUpdate();
        }
        return true;
    }

    /**
     * Update user level
     *
     * @param lvl
     * @return
     * @throws Exception
     */
    public static boolean updateUserLevel(UserLevel lvl) throws Exception {
        String query = "UPDATE `user_levels` "
                + "SET `level_code`=?, `level_name`=?, `level_display_name`=?, `level_active`=? "
                + "WHERE `level_id`=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, lvl.getLevelCode());
            ps.setString(2, lvl.getLevelName());
            ps.setString(3, lvl.getLevelDisplayName());
            ps.setBoolean(4, lvl.isActiveLevel());
            ps.setInt(5, lvl.getLevelId());
            ps.executeUpdate();
        }
        return true;
    }

    /**
     * Delete user level
     *
     * @param lvl
     * @return
     * @throws Exception
     */
    public static boolean deleteUserLevel(UserLevel lvl) throws Exception {
        String query = "DELETE `user_levels` WHERE `level_id`=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.executeUpdate();
        }
        return true;
    }

    /**
     * Get user levels list model function
     *
     * @return
     * @throws Exception
     */
    public static List<UserLevel> getUserLevelsList() throws Exception {
        ArrayList<UserLevel> uls = new ArrayList<>();
        String query = "SELECT user_levels.level_id, user_levels.level_code, "
                + "user_levels.level_name, user_levels.level_display_name, user_levels.level_active "
                + "FROM `user_levels` ORDER BY user_levels.level_name ASC";
        ResultSet rs = con.prepareStatement(query).executeQuery();
        while (rs.next()) {
            UserLevel ul = new UserLevel();
            ul.setLevelId(rs.getInt("level_id"));
            ul.setLevelCode(rs.getString("level_code"));
            ul.setLevelName(rs.getString("level_name"));
            ul.setLevelDisplayName(rs.getString("level_display_name"));
            ul.setIsActiveLevel(rs.getBoolean("level_active"));
            uls.add(ul);
        }
        return uls;
    }

    public static UserLevel getUserLevelById(int ulid) throws Exception {
        UserLevel ul = null;
        String query = "SELECT user_levels.level_id, user_levels.level_code, "
                + "user_levels.level_name, user_levels.level_display_name, user_levels.level_active "
                + "FROM `user_levels` WHERE user_levels.level_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, ulid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ul = new UserLevel();
            ul.setLevelId(rs.getInt("level_id"));
            ul.setLevelCode(rs.getString("level_code"));
            ul.setLevelName(rs.getString("level_name"));
            ul.setLevelDisplayName(rs.getString("level_display_name"));
            ul.setIsActiveLevel(rs.getBoolean("level_active"));
        }
        return ul;
    }

    public static boolean createUser(User user) throws Exception {
        String cuq = "INSERT INTO `users` (`user_name`, `user_display_name`, "
                + "`user_password`, `user_level`, `user_status`, `user_type`) "
                + "VALUES (?, ?, PASSWORD(?), ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(cuq)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserDisplayName());
            ps.setString(3, user.getUserPassword());
            ps.setInt(4, user.getUserLevel().getLevelId());
            ps.setInt(5, user.getUserStatus());
            ps.setInt(6, user.getUserType());
            ps.executeUpdate();
        }
        return true;
    }

    public static boolean updateUser(User user) throws Exception {
        String cuq = "UPDATE `users` "
                + "SET `user_name`=?, `user_display_name`=?, "
                + "`user_level`=?, `user_status`=?, `user_type`=? "
                + "WHERE `user_id`=?";
        try (PreparedStatement ps = con.prepareStatement(cuq)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserDisplayName());
            ps.setInt(3, user.getUserLevel().getLevelId());
            ps.setInt(4, user.getUserStatus());
            ps.setInt(5, user.getUserType());
            ps.setInt(6, user.getUserId());
            ps.executeUpdate();
        }
        return true;
    }

    public static boolean updateUserPassword(User user, String new_pass) throws Exception {
        String selq = "SELECT COUNT(`user_id`) AS UC FROM `users` WHERE `user_id`=?";
        PreparedStatement selqps = con.prepareStatement(selq);
        selqps.setInt(1, user.getUserId());
        ResultSet rs = selqps.executeQuery();
        if (rs.next()) {
            if (rs.getInt("UC") > 0) {
                String cuq = "UPDATE `users` SET `user_password`=PASSWORD(?) WHERE `user_id`=?";
                try (PreparedStatement ps = con.prepareStatement(cuq)) {
                    ps.setString(1, new_pass);
                    ps.setInt(2, user.getUserId());
                    ps.executeUpdate();
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean deleteUser(User user) throws Exception {
        String cuq = "DELETE FROM `users` WHERE `user_id`=?";
        try (PreparedStatement ps = con.prepareStatement(cuq)) {
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
        }
        return true;
    }

    public static ArrayList<User> getUsersList() throws Exception {
        ArrayList<User> ulist = new ArrayList<>();
        String query = "SELECT users.user_id, users.user_name, users.user_display_name, users.user_password, users.user_level, "
                + "users.user_status, users.user_type, users.user_close_time FROM `users` ORDER BY users.user_name ASC";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setUserName(rs.getString("user_name"));
            u.setUserDisplayName(rs.getString("user_display_name"));
            u.setUserPassword(rs.getString("user_password"));
            u.setUserLevel(getUserLevelById(rs.getInt("user_level")));
            u.setUserStatus(rs.getInt("user_status"));
            u.setUserType(rs.getInt("user_type"));
            u.setUserCloseTime(rs.getDate("user_close_time"));
            ulist.add(u);
        }
        return ulist;
    }

    public static User getUserByID(int userid) throws Exception {
        User u = null;
        String query = "SELECT users.user_id, users.user_name, users.user_display_name, users.user_password, users.user_level, "
                + "users.user_status, users.user_type, users.user_close_time FROM `users` WHERE users.user_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, userid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setUserName(rs.getString("user_name"));
            u.setUserDisplayName(rs.getString("user_display_name"));
            u.setUserPassword(rs.getString("user_password"));
            u.setUserLevel(getUserLevelById(rs.getInt("user_level")));
            u.setUserStatus(rs.getInt("user_status"));
            u.setUserType(rs.getInt("user_type"));
            u.setUserCloseTime(rs.getDate("user_close_time"));
        }
        return u;
    }

    public static boolean signInUser(User user, String password) throws Exception {
        String query = "SELECT users.user_id, users.user_name, users.user_display_name, users.user_password, "
                + "users.user_level, users.user_status, users.user_type, users.user_close_time "
                + "FROM `users` WHERE users.user_id = ? AND users.user_password = PASSWORD(?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, user.getUserId());
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            //TODO: check for temporary user type and expiry time
            //bump away the inactive users
            return rs.getInt("user_status") == 1;
        }
        return false;
    }

    public static String updateUserLoggedStatus(User user) throws Exception {
        String query = "UPDATE users SET user_logged_last=?, last_logged_hash=? WHERE users.user_id =?";
        PreparedStatement ps = con.prepareStatement(query);
        String formatd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        UUID randomUUID = UUID.randomUUID();
        ps.setString(1, formatd);
        ps.setString(2, randomUUID.toString());
        ps.setInt(3, user.getUserId());
        ps.executeUpdate();

        String q2 = "INSERT INTO `login_history` (`user_id`, `logged_in_time`, `login_hash`) VALUES (?, ?, ?)";
        ps = con.prepareStatement(q2);
        ps.setInt(1, user.getUserId());
        ps.setString(2, formatd);
        ps.setString(3, randomUUID.toString());
        ps.executeUpdate();

        ps.close();
        return randomUUID.toString();
    }

    public static ArrayList<Privilege> getAvailableUserLevelPrivileges(UserLevel ul) throws Exception {
        ArrayList<Privilege> prvls = new ArrayList<>();
        String query = "SELECT system_privileges.prv_id, system_privileges.prv_code, system_privileges.prv_name, "
                + "system_privileges.prv_display_name, system_privileges.prv_parent "
                + "FROM system_privileges "
                + "WHERE system_privileges.prv_id NOT IN ( "
                + "SELECT user_level_privileges.prv_id FROM `user_level_privileges` WHERE user_level_privileges.level_id = ? "
                + ")";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, ul.getLevelId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Privilege sp = new Privilege();
            sp.setPrvId(rs.getInt("prv_id"));
            sp.setPrvCode(rs.getString("prv_code"));
            sp.setPrvName(rs.getString("prv_name"));
            sp.setPrvDisplayName(rs.getString("prv_display_name"));
            sp.setPrvParent(rs.getInt("prv_parent"));
            prvls.add(sp);
        }
        return prvls;
    }

    public static ArrayList<Privilege> getAssignedUserLevelPrivileges(UserLevel ul) throws Exception {
        ArrayList<Privilege> prvls = new ArrayList<>();
        String query = "SELECT user_level_privileges.level_id, system_privileges.prv_id, system_privileges.prv_code, "
                + "system_privileges.prv_name, system_privileges.prv_display_name, system_privileges.prv_parent "
                + "FROM user_level_privileges "
                + "INNER JOIN system_privileges ON user_level_privileges.prv_id = system_privileges.prv_id "
                + "WHERE user_level_privileges.level_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, ul.getLevelId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Privilege sp = new Privilege();
            sp.setPrvId(rs.getInt("prv_id"));
            sp.setPrvCode(rs.getString("prv_code"));
            sp.setPrvName(rs.getString("prv_name"));
            sp.setPrvDisplayName(rs.getString("prv_display_name"));
            sp.setPrvParent(rs.getInt("prv_parent"));
            prvls.add(sp);
        }
        return prvls;
    }

    public static boolean assignUserLevelPrivileges(UserLevel ul, ArrayList<Privilege> arls) throws Exception {
        if (arls.isEmpty()) {
            return false;
        }
        String query = "INSERT INTO `user_level_privileges` (`level_id`, `prv_id`) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (Privilege arl : arls) {
                ps.setInt(1, ul.getLevelId());
                ps.setInt(2, arl.getPrvId());
                ps.executeUpdate();
            }
        }
        return true;
    }

    public static boolean removeUserLevelPrivileges(UserLevel ul, ArrayList<Privilege> arls) throws Exception {
        if (arls.isEmpty()) {
            return false;
        }
        String query = "DELETE FROM `user_level_privileges` WHERE `level_id` = ? AND `prv_id` = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (Privilege arl : arls) {
                ps.setInt(1, ul.getLevelId());
                ps.setInt(2, arl.getPrvId());
                ps.executeUpdate();
            }
        }
        return true;
    }

    public static ArrayList<Privilege> getAvailableUserPrivileges(User u) throws Exception {
        ArrayList<Privilege> arl = new ArrayList<>();
        String query = "SELECT system_privileges.prv_id, system_privileges.prv_code, system_privileges.prv_name, "
                + "system_privileges.prv_display_name, system_privileges.prv_parent "
                + "FROM user_level_privileges "
                + "INNER JOIN system_privileges ON user_level_privileges.prv_id = system_privileges.prv_id "
                + "WHERE user_level_privileges.level_id = ? AND user_level_privileges.prv_id NOT IN ( "
                + "SELECT user_privileges.prv_id FROM user_privileges WHERE user_privileges.user_id = ? "
                + ")";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, u.getUserLevel().getLevelId());
        ps.setInt(2, u.getUserId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Privilege sp = new Privilege();
            sp.setPrvId(rs.getInt("prv_id"));
            sp.setPrvCode(rs.getString("prv_code"));
            sp.setPrvName(rs.getString("prv_name"));
            sp.setPrvDisplayName(rs.getString("prv_display_name"));
            sp.setPrvParent(rs.getInt("prv_parent"));
            arl.add(sp);
        }
        return arl;
    }

    public static ArrayList<Privilege> getAssignedUserPrivileges(User u) throws Exception {
        ArrayList<Privilege> al = new ArrayList<>();
        String query = "SELECT system_privileges.prv_id, system_privileges.prv_code, "
                + "system_privileges.prv_name, system_privileges.prv_display_name, system_privileges.prv_parent "
                + "FROM user_privileges "
                + "INNER JOIN system_privileges ON user_privileges.prv_id = system_privileges.prv_id "
                + "WHERE user_privileges.user_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, u.getUserId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Privilege sp = new Privilege();
            sp.setPrvId(rs.getInt("prv_id"));
            sp.setPrvCode(rs.getString("prv_code"));
            sp.setPrvName(rs.getString("prv_name"));
            sp.setPrvDisplayName(rs.getString("prv_display_name"));
            sp.setPrvParent(rs.getInt("prv_parent"));
            al.add(sp);
        }
        return al;
    }

    public static boolean assignUserPrivileges(User u, ArrayList<Privilege> arls) throws Exception {
        if (arls.isEmpty()) {
            return false;
        }
        String query = "INSERT INTO `user_privileges` (`user_id`, `prv_id`) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (Privilege arl : arls) {
                ps.setInt(1, u.getUserId());
                ps.setInt(2, arl.getPrvId());
                ps.executeUpdate();
            }
        }
        return true;
    }

    public static boolean removeUserPrivileges(User u, ArrayList<Privilege> arls) throws Exception {
        if (arls.isEmpty()) {
            return false;
        }
        String query = "DELETE FROM `user_privileges` WHERE `user_id` = ? AND `prv_id` = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (Privilege arl : arls) {
                ps.setInt(1, u.getUserId());
                ps.setInt(2, arl.getPrvId());
                ps.executeUpdate();
            }
        }
        return true;
    }

    public static boolean isPrivileged(SystemPrivilege prv) throws Exception {
        String uid = System.getProperty("userid", "0");
        int userid = Utilities.getIntegerValue(uid);

        String query = "SELECT Count(user_privileges.prv_id) AS prvcount "
                + "FROM user_privileges "
                + "WHERE user_privileges.user_id = ? AND user_privileges.prv_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, userid);
        ps.setInt(2, prv.getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("prvcount") == 1;
        }
        return false;
    }

    public static boolean isAdminPassword(String password) throws Exception {
        String query = "SELECT Count(users.user_id) AS USERCOUNT "
                + "FROM `users` WHERE users.user_password = PASSWORD (?) AND "
                + "users.user_level = 001 AND users.user_status = 1";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("USERCOUNT") > 0;
        }
        return false;
    }

}
