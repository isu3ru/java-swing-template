package app.helpers;

/**
 *
 * @author Isuru
 */
public enum SystemPrivilege {

    REPORTS(1, 101, "Reports");

    /**
     * id -> Unique Integer
     * code -> Unique Integer
     * displayName -> What the privilege display name should be
     */
    private SystemPrivilege(int id, int code, String displayName) {
        this.id = id;
        this.code = code;
        this.displayName = displayName;
    }

    private int id;
    private int code;
    private String displayName;

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     *
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return code + " - " + displayName;
    }

}
