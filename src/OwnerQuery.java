import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by win on 2016-09-25.
 */
public class OwnerQuery {
    Connection connection;
    PreparedStatement statement;
    ResultSet rs;
    String strModifiedSQLText;
//    String strOwner;
    String strTable;

    public String handleMessage(String message) {
        System.out.println("receive from client : " + message);
        Log.debug("receive from client : " + message); // 테이블명
        strTable = message;
        JsonObject aInfo = new JsonObject();
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DSTest");
            connection = ds.getConnection();
            //////// JSON 만들기 ////////////////
            aInfo.addProperty("success", true); //{"querySuccess":true}
            aInfo.addProperty("messageType", "tableowner"); //{("messageType", "tableinfo"}
            /// Column정보를 가져와서 JsonObject 만들기
            strModifiedSQLText ="select OWNER from all_tables where table_name=?";

            ResultSetToJsonObject rstojson = processSQL();
            rstojson.processResultSet(rs, aInfo, "resultsettableowner"); // aInfo에 json 리절트셋 추가, {"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
        }catch (SQLException e) {
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
            }
            if (statement != null) try {
                statement.close();
            } catch (SQLException ex) {
            }
            if (connection != null) try {
                connection.close();
            } catch (SQLException ex) {
            }
        }

        String bInfo = aInfo.toString();
        Log.debug(bInfo);
        return bInfo;
    }

    public ResultSetToJsonObject processSQL() throws SQLException{
        System.out.println("sql : " + strModifiedSQLText);
        Log.debug("sql : " + strModifiedSQLText);
        statement = connection.prepareStatement(strModifiedSQLText);
        statement.setString(1, strTable);
        rs = statement.executeQuery();

        ResultSetToJsonObject rstojson = new ResultSetToJsonObject();
        return rstojson;
    }
}
