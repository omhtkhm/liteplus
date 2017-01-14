package exem.liteplusweb;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import simpledb.EmbeddedDatabase;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class Query {
    Connection connection;
    PreparedStatement statement;
    ResultSet rs;

    public String handleMessage(String message) {
        System.out.println("receive from client : " + message);
        Log.debug("receive from client : " + message);

        String strSQLText = message;
        String strModifiedSQLText = QueryParser.removeSemicolon(strSQLText);
//        System.out.println("semicolin deleted : " + strModifiedSQLText);
//        exem.liteplusweb.Log.debug("semicolin deleted : " + strModifiedSQLText);

        JsonObject aInfo = new JsonObject();
        try {
            InitialContext ctx = new InitialContext();
            // Here we lookup the datasource with the name
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DSTest");
            Connection connection = ds.getConnection();
            PreparedStatement statement = connection.prepareStatement(strModifiedSQLText);
            EmbeddedDatabase.insertSqlHist(message);//exem.liteplusweb.Query Embbeded Derby DB에 저장
            //실행시간 측정
            long start = System.currentTimeMillis(); // 실행시간 측정 용
            ResultSet rs = statement.executeQuery();

            boolean bFirestRow = rs.next(); // 첫번째 행 가져오기
            long end = System.currentTimeMillis(); //실행시간 측정용
            double elapsedTime = (end - start) / 1000.0;
//            exem.liteplusweb.Log.debug(( "실행 시간 : " + elapsedTime ));

            if (bFirestRow) {   // 첫행이 존재할 경우,
                aInfo.addProperty("success", true); //{"success":true}
                aInfo.addProperty("messageType", "query"); //{"querySuccess":true}
                aInfo.addProperty("FirstRowTime", "First Rows Retrieved In " + elapsedTime + " seconds"); //{"querySuccess":"", "FirstRowTime":""}

                JsonObject bInfo = new JsonObject(); // {}
                // loop rs.getResultSetMetadata columns
                ResultSetMetaData rsmd = rs.getMetaData();

                JsonArray infoArray = new JsonArray(); // []
                for (int idx = 1; idx <= rsmd.getColumnCount(); idx++) {
                    bInfo.addProperty(rsmd.getColumnLabel(idx), rs.getString(idx)); // { "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }
                }
                infoArray.add(bInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]

                //두번째 행 부터 가져오기
                JsonObject cInfo = null;
                while (rs.next()) {
                    // loop rs.getResultSetMetadata columns
                    cInfo = new JsonObject();
                    for (int idx = 1; idx <= rsmd.getColumnCount(); idx++) {
                        cInfo.addProperty(rsmd.getColumnLabel(idx), rs.getString(idx)); // { "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }
                    }
                    infoArray.add(cInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" },{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
                }
                aInfo.add("resultset", infoArray); //{"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
            } else {   //행이 존재하지 않을 경우, 데이터가 없는 경우
                aInfo.addProperty("success", true); //{"success":true}
                aInfo.addProperty("messageType", "query"); //{"success":true}
                aInfo.addProperty("FirstRowTime", "No Rows Retrieved In " + elapsedTime + " seconds"); //{"querySuccess":"", "FirstRowTime":""}

                JsonObject bInfo = new JsonObject(); // {}
                JsonArray infoArray = new JsonArray(); // []
                bInfo.addProperty(" ", " "); //{"success":true}
                infoArray.add(bInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
                aInfo.add("resultset", infoArray); //{"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            aInfo = sqlExceptionToGrid(e);
            Log.debug(e.getMessage());
        } catch (Exception e) {
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

        return aInfo.toString();
    }

    public JsonObject sqlExceptionToGrid(SQLException e) {
        JsonObject aInfo = new JsonObject();
        aInfo.addProperty("success", false); //{"success":true}
        aInfo.addProperty("messageType", "query"); //{"success":true}
        aInfo.addProperty("FirstRowTime", e.getMessage()); //{"success":"", "FirstRowTime":""}

        JsonObject bInfo = new JsonObject(); // {}
        JsonArray infoArray = new JsonArray(); // []
        bInfo.addProperty(" ", " "); //{"success":true}
        infoArray.add(bInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
        aInfo.add("resultset", infoArray); //{"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
        return aInfo;
    }
}
