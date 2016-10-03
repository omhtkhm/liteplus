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

public class History {
    ResultSet rs;

    public String handleMessage(String strCurrentIndex, String strDirection) {
        System.out.println("receive from client : index" + strCurrentIndex+" , direction : "+strDirection);
        Log.debug("receive from client : index" + strCurrentIndex + " , direction : " + strDirection);

        JsonObject aInfo = new JsonObject();
        try {
            int iCurrentIndex = Integer.parseInt(strCurrentIndex);
            if(iCurrentIndex!=0) {
                if (strDirection.equals("next")) iCurrentIndex++;
                if (strDirection.equals("previous")) iCurrentIndex--;
            }
            rs = EmbeddedDatabase.selectSqlHist(iCurrentIndex, strDirection);
//            rs.next(); // 1행
//            if(rs.getInt("VIEWCOUNT")==0){  // 테이블이 존재하지 않으면
//                throw new NoRowsException();
//            }
            //////// JSON 만들기 ////////////////
            aInfo.addProperty("success", true); //{"success":true}
            aInfo.addProperty("messageType", "history"); //{("messageType", "merge"}
            /// VIEW sql정보를 가져와서 JsonObject 만들기
            ResultSetToJsonObject rstojson = new ResultSetToJsonObject();
            rstojson.processResultSet(rs, aInfo, "resultsethistory"); // aInfo에 json 리절트셋 추가, {"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
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
