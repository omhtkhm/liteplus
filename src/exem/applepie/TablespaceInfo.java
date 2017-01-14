package exem.applepie; /**
 * Created by win on 2016-09-18.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import exem.liteplusweb.Log;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class TablespaceInfo {
    Connection connection;
    PreparedStatement statement;
    ResultSet rs;
    String strModifiedSQLText;

    public String handleMessage() {
        strModifiedSQLText ="select a.tablespace_name,\n" +
                "       a.bytes_alloc/(1024*1024) \"TOTAL ALLOC (MB)\",\n" +
                "       a.physical_bytes/(1024*1024) \"TOTAL PHYS ALLOC (MB)\",\n" +
                "       nvl(b.tot_used,0)/(1024*1024) \"USED (MB)\",\n" +
                "       (nvl(b.tot_used,0)/a.bytes_alloc)*100 \"% USED\"\n" +
                "from ( select tablespace_name,\n" +
                "       sum(bytes) physical_bytes,\n" +
                "       sum(decode(autoextensible,'NO',bytes,'YES',maxbytes)) bytes_alloc\n" +
                "       from dba_data_files\n" +
                "       group by tablespace_name ) a,\n" +
                "     ( select tablespace_name, sum(bytes) tot_used\n" +
                "       from dba_segments\n" +
                "       group by tablespace_name ) b\n" +
                "where a.tablespace_name = b.tablespace_name (+)\n" +
                "--and   (nvl(b.tot_used,0)/a.bytes_alloc)*100 > 10\n" +
                "and   a.tablespace_name not in (select distinct tablespace_name from dba_temp_files)\n" +
                "and   a.tablespace_name not like 'UNDO%'\n" +
                "order by 1";
        JsonObject jsonReplyMsg = new JsonObject(); //{}
        jsonReplyMsg.addProperty("messageType", "tablespaceinfo");

        try {
                InitialContext ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DSTest");
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(strModifiedSQLText);
                rs = statement.executeQuery(); // Plan_table 쿼리 수행

                JsonArray jsonarrRows = new JsonArray(); // []
                if (rs.next()) {   // 첫행이 존재할 경우,
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int numOfColumns = rsmd.getColumnCount();
                    String[] ColumnNames = new String[numOfColumns+1];


                    JsonObject jsonHeader = new JsonObject(); //{}
                    for (int idx = 1; idx <= numOfColumns; idx++) {
                        ColumnNames[idx] = rsmd.getColumnLabel(idx);
                        jsonHeader.addProperty(ColumnNames[idx], rs.getString(idx)); // { "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }
                    }
                    jsonarrRows.add(jsonHeader); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]

                    //두번째 행 부터 가져오기
                    while (rs.next()) {
                        JsonObject jsonRow = new JsonObject();
                        for (int idx = 1; idx <= numOfColumns; idx++) {
                            jsonRow.addProperty(ColumnNames[idx], rs.getString(idx)); // { "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }
                        }
                        jsonarrRows.add(jsonRow); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" },{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
                    }
                }
                jsonReplyMsg.addProperty("success", true); //{"success":true}
                jsonReplyMsg.add("resultset", jsonarrRows); //{"success": true, "messageType": "tablesapceinfo", "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
            }
            catch (SQLException e) {
                e.printStackTrace();
                jsonReplyMsg = sqlExceptionToJson(e);
                Log.debug(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                jsonReplyMsg = sqlExceptionToJson(e);
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
            return jsonReplyMsg.toString();
        }

    public JsonObject sqlExceptionToJson(Exception e) {
        JsonObject aInfo = new JsonObject();
        aInfo.addProperty("success", false);
        aInfo.addProperty("errormessage", e.getMessage( ));
        return aInfo;
    }
}
