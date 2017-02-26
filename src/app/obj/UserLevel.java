/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.obj;

import java.io.Serializable;

/**
 *
 * @author Isuru
 */
public class UserLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer levelId;
    private String levelCode;
    private String levelName;
    private String levelDisplayName;
    private boolean activeLevel;

    public UserLevel() {
    }

    public UserLevel(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelDisplayName() {
        return levelDisplayName;
    }

    public void setLevelDisplayName(String levelDisplayName) {
        this.levelDisplayName = levelDisplayName;
    }

    public boolean isActiveLevel() {
        return activeLevel;
    }

    public void setIsActiveLevel(boolean isActiveUser) {
        this.activeLevel = isActiveUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (levelId != null ? levelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserLevel)) {
            return false;
        }
        UserLevel other = (UserLevel) object;
        return !((this.levelId == null && other.levelId != null) || (this.levelId != null && !this.levelId.equals(other.levelId)));
    }

    @Override
    public String toString() {
        return levelCode;
    }

}
