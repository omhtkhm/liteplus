package exem.liteplusweb;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;

/**
 * Created by win on 2016-09-18.
 */
public class ResultSetToJsonObject {
    public JsonObject processResultSet(ResultSet rs, JsonObject aInfo, String strKey) throws SQLException{
            ResultSetMetaData rsmd = rs.getMetaData();

            JsonArray infoArray = new JsonArray(); // []
            //결과행 가져오기
            JsonObject cInfo = null;
            while (rs.next()) {
                // loop rs.getResultSetMetadata columns
                cInfo = new JsonObject();
                for (int idx = 1; idx <= rsmd.getColumnCount(); idx++) {
                    cInfo.addProperty(rsmd.getColumnLabel(idx), rs.getString(idx)); // { "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }
                }
                infoArray.add(cInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" },{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
            }
            aInfo.add(strKey, infoArray);
            return aInfo;
    }

    public ResultSet processSQL(Connection connection, String strModifiedSQLText) throws SQLException{
        System.out.println("sql : " + strModifiedSQLText);
        Log.debug("sql : " + strModifiedSQLText);
        PreparedStatement statement;
        ResultSet rs;
        statement = connection.prepareStatement(strModifiedSQLText);
        rs = statement.executeQuery();
        return rs;
    }

//    public JsonObject sqlExceptionToJson(SQLException e) {
//        JsonObject aInfo = new JsonObject();
//        aInfo.addProperty("success", false); //{"success":true}
//        aInfo.addProperty("messageType", "popup"); //{"success":true}
//        aInfo.addProperty("errormessage", e.getMessage( )); //{"success":true}
//        return aInfo;
//    }
//    public JsonObject processSQLtoJson(Connection connection, String sql, JsonObject aInfo, String strKey) throws SQLException{
//        System.out.println("sql : " + sql);
//        exem.liteplusweb.Log.debug("sql : " + sql);
//        PreparedStatement statement;
//        ResultSet rs;
//        statement = connection.prepareStatement(sql);
//        rs=statement.executeQuery();
//        aInfo = processResultSet(rs, aInfo, strKey);
//        return aInfo;
//    }
}
