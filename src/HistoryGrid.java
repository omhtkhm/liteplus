/**
 * Created by win on 2016-10-03.
 */
import com.google.gson.JsonObject;
import simpledb.EmbeddedDatabase;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryGrid {
    ResultSet rs;

    public String handleMessage() {
        System.out.println("receive from client : no argument in history grid");
        Log.debug("receive from client : no argument in history grid");

        JsonObject aInfo = new JsonObject();
        try {

            rs = EmbeddedDatabase.selectSqlHistGrid();
            //////// JSON 만들기 ////////////////
            aInfo.addProperty("success", true); //{"success":true}
            aInfo.addProperty("messageType", "historygrid"); //{("messageType", "merge"}
            /// VIEW sql정보를 가져와서 JsonObject 만들기
            ResultSetToJsonObject rstojson = new ResultSetToJsonObject();
            rstojson.processResultSet(rs, aInfo, "resultsethistorygrid"); // aInfo에 json 리절트셋 추가, {"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
        }/*catch (NoRowsException e) {
            // 테이블이 존재하지 않으므로, success: false, message: "OWNER.TABLE is not found"
            aInfo.addProperty("success", false); //{"success":false}
            aInfo.addProperty("messageType", "tableinfo"); //{("messageType", "tableinfo"}
            aInfo.addProperty("errormessage", "[VIEW] "+message+"\nNo such object found"); //뷰가 존재하지 않음
            Log.debug(e.getMessage());
        }*/catch (SQLException e) {
            e.printStackTrace();
//            sqlExceptionToGrid(e);
            Log.debug(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            Log.debug(e.getMessage());
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        String bInfo = aInfo.toString();
        Log.debug(bInfo);
        return bInfo;
    }
}
