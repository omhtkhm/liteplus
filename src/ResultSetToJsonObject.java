import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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
}
