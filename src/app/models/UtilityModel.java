package app.models;

import app.helpers.Utilities;
import static app.models.Model.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Isuru
 */
public class UtilityModel extends Model {

    public static boolean isPropertyTypeAvailable(int propt) throws Exception {
        String sq = "SELECT Count(properties.prop_id) AS propct FROM properties WHERE properties.prop_type = ?";
        PreparedStatement ps = con.prepareStatement(sq);
        ps.setInt(1, propt);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("propct") > 0;
        }
        return true;
    }

    private static String _build_hash(String trn_id, String trn_no, String trn_desc, String trn_time, int trn_user, String trn_comment) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(trn_id).append(trn_no).append(trn_desc).append(trn_time).append(trn_user).append(trn_comment);
        return Utilities.MD5_HEX(sb.toString());
    }

    public static boolean truncateDatabaseTable(String table_name) throws Exception {
        PreparedStatement ps = con.prepareStatement("TRUNCATE TABLE " + table_name);
        ps.executeUpdate();
        ps.close();
        return true;
    }

}
