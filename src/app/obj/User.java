/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.obj;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Isuru
 */
public class User implements Serializable {

    public enum UserType {

        PERMANENT(1, "Permanent User"), TEMPORARY(0, "Temporary User");

        private int utid;
        private String utypename;

        private UserType(int utid, String utypename) {
            this.utid = utid;
            this.utypename = utypename;
        }

        public int getUtid() {
            return utid;
        }

        public void setUtid(int utid) {
            this.utid = utid;
        }

        public String getUtypename() {
            return utypename;
        }

        public void setUtypename(String utypename) {
            this.utypename = utypename;
        }

        public UserType getUserTypeById(int id) {
            if (id == 0) {
                return PERMANENT;
            }
            if (id == 1) {
                return TEMPORARY;
            }
            return TEMPORARY;
        }
        
        @Override
        public String toString() {
            return utypename;
        }

    }

    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String userName;
    private String userDisplayName;
    private String userPassword;
    private Integer userStatus;
    private Integer userType;
    private Date userCloseTime;
    private List<Privilege> wbSystemPrivilegesList;
    private UserLevel userLevel;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getUserCloseTime() {
        return userCloseTime;
    }

    public void setUserCloseTime(Date userCloseTime) {
        this.userCloseTime = userCloseTime;
    }

    public List<Privilege> getWbSystemPrivilegesList() {
        return wbSystemPrivilegesList;
    }

    public void setWbSystemPrivilegesList(List<Privilege> wbSystemPrivilegesList) {
        this.wbSystemPrivilegesList = wbSystemPrivilegesList;
    }

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(UserLevel userLevel) {
        this.userLevel = userLevel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return !((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId)));
    }

    @Override
    public String toString() {
        return userName;
    }

}
