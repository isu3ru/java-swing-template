/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.helpers.SystemPrivilege;
import app.models.UserModel;
import app.obj.Privilege;
import app.obj.User;
import app.obj.UserLevel;
import java.util.ArrayList;
import java.util.List;

/**
 * User management related controller functions
 *
 * @author Isuru
 */
public class UserController {

    /**
     * Save user level
     *
     * @param lvl
     * @return
     */
    public static boolean saveUserLevel(UserLevel lvl) {
        try {
            return UserModel.saveUserLevel(lvl);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Update user level
     *
     * @param lvl
     * @return
     */
    public static boolean updateUserLevel(UserLevel lvl) {
        try {
            return UserModel.updateUserLevel(lvl);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete the user level
     *
     * @param lvl
     * @return
     */
    public static boolean deleteUserLevel(UserLevel lvl) {
        try {
            return UserModel.deleteUserLevel(lvl);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get user levels list as a List
     *
     * @return
     */
    public static List<UserLevel> getUserLevelsList() {
        List<UserLevel> list = new ArrayList<>();
        try {
            list = UserModel.getUserLevelsList();
        } catch (Exception e) {
        }
        return list;
    }

    public static boolean createUser(User user) {
        try {
            return UserModel.createUser(user);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean updateUser(User user) {
        try {
            return UserModel.updateUser(user);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean updateUserPassword(User user, String new_pass) {
        try {
            return UserModel.updateUserPassword(user, new_pass);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteUser(User user) {
        try {
            return UserModel.deleteUser(user);
        } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<User> getUsersList() {
        ArrayList<User> usersList = new ArrayList<>();
        try {
            usersList = UserModel.getUsersList();
        } catch (Exception e) {
        }
        return usersList;
    }

    public static boolean signInUser(User user, String password) {
        try {
            return UserModel.signInUser(user, password);
        } catch (Exception e) {
            return false;
        }
    }
    public static String updateUserLoggedStatus(User user) {
        try {
            return UserModel.updateUserLoggedStatus(user);
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Privilege> getAvailableUserLevelPrivileges(UserLevel ul) {
        ArrayList<Privilege> al = new ArrayList<>();
        try {
            al = UserModel.getAvailableUserLevelPrivileges(ul);
        } catch (Exception e) {
        }
        return al;
    }

    public static ArrayList<Privilege> getAssignedUserLevelPrivileges(UserLevel ul) {
        ArrayList<Privilege> al = new ArrayList<>();
        try {
            al = UserModel.getAssignedUserLevelPrivileges(ul);
        } catch (Exception e) {
        }
        return al;
    }

    public static boolean assignUserLevelPrivileges(UserLevel ul, ArrayList<Privilege> arls) {
        try {
            return UserModel.assignUserLevelPrivileges(ul, arls);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean removeUserLevelPrivileges(UserLevel ul, ArrayList<Privilege> arls) {
        try {
            return UserModel.removeUserLevelPrivileges(ul, arls);
        } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<Privilege> getAvailableUserPrivileges(User u) {
        ArrayList<Privilege> al = new ArrayList<>();
        try {
            al = UserModel.getAvailableUserPrivileges(u);
        } catch (Exception e) {
        }
        return al;
    }

    public static ArrayList<Privilege> getAssignedUserPrivileges(User u) {
        ArrayList<Privilege> al = new ArrayList<>();
        try {
            al = UserModel.getAssignedUserPrivileges(u);
        } catch (Exception e) {
        }
        return al;
    }
    
    public static boolean assignUserPrivileges(User u, ArrayList<Privilege> arls) {
        try {
            return UserModel.assignUserPrivileges(u, arls);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean removeUserPrivileges(User u, ArrayList<Privilege> arls) {
        try {
            return UserModel.removeUserPrivileges(u, arls);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isPrivileged(SystemPrivilege prv){
        try {
            return UserModel.isPrivileged(prv);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isAdminPassword(String password) {
        try {
            return UserModel.isAdminPassword(password);
        } catch (Exception e) {
            return false;
        }
    }
    
}
