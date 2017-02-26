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
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer prvId;
    private String prvCode;
    private String prvName;
    private String prvDisplayName;
    private Integer prvParent;

    public Privilege() {
    }

    public Privilege(Integer prvId) {
        this.prvId = prvId;
    }

    public Integer getPrvId() {
        return prvId;
    }

    public void setPrvId(Integer prvId) {
        this.prvId = prvId;
    }

    public String getPrvCode() {
        return prvCode;
    }

    public void setPrvCode(String prvCode) {
        this.prvCode = prvCode;
    }

    public String getPrvName() {
        return prvName;
    }

    public void setPrvName(String prvName) {
        this.prvName = prvName;
    }

    public String getPrvDisplayName() {
        return prvDisplayName;
    }

    public void setPrvDisplayName(String prvDisplayName) {
        this.prvDisplayName = prvDisplayName;
    }

    public Integer getPrvParent() {
        return prvParent;
    }

    public void setPrvParent(Integer prvParent) {
        this.prvParent = prvParent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prvId != null ? prvId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Privilege)) {
            return false;
        }
        Privilege other = (Privilege) object;
        return !((this.prvId == null && other.prvId != null) || (this.prvId != null && !this.prvId.equals(other.prvId)));
    }

    @Override
    public String toString() {
        return prvCode;
    }

}
