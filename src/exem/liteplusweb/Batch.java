package exem.liteplusweb; /**
 * Created by win on 2016-10-09.
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import simpledb.EmbeddedDatabase;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class Batch {
    Connection connection;
    PreparedStatement statement;
    ResultSet rs;

    public String handleMessage(String message, String sqlIndex) {
        System.out.println("receive from client : " + message);
        Log.debug("receive from client : " + message);

        String strSQLText = message;
        String strModifiedSQLText = QueryParser.removeSemicolon(strSQLText);

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

            StringBuffer resultText=new StringBuffer();
            if (bFirestRow) {   // 첫행이 존재할 경우,
                aInfo.addProperty("success", true); //{"success":true}
                aInfo.addProperty("messageType", "batch"); //{"querySuccess":true}
                aInfo.addProperty("FirstRowTime", "First Rows Retrieved In " + elapsedTime + " seconds"); //{"querySuccess":"", "FirstRowTime":""}

                // 쿼리 결과를 정렬된 텍스트 포맷으로 변환해야 함. 우선 컬럼헤드값을 뽑아서 배열에 넣는다.
                // ResultSetMetaData의 데이터타입을 보고 컬럼의 크기를 결정해야 한다.
                ResultSetMetaData rsmd = rs.getMetaData();

//                JsonArray infoArray = new JsonArray(); // []
                int tempColumnSize=0;
                String tempColumnLabel;
                StringBuffer tempResultText=new StringBuffer();
                int columnCount = rsmd.getColumnCount(); // 컬럼의 갯수
                int[] columnSizes = new int[columnCount+1]; //컬럼 폭크기 저장변수
                int tempColumnType=0;
                String tempColumnValue;

                for (int idx = 1; idx <= columnCount; idx++) {
                    tempColumnLabel = rsmd.getColumnLabel(idx);
                    tempColumnSize = rsmd.getColumnDisplaySize(idx);
                    if( tempColumnLabel.length() > tempColumnSize ) tempColumnSize = tempColumnLabel.length(); //컬럼head명이 길면 head명 길이로 설정

                    tempColumnValue = rs.getString(idx);
                    if(tempColumnValue != null) {
                        tempColumnType = rsmd.getColumnType(idx);
                        if (tempColumnType == 93) { // data 형식이면, 10자리로 설정 1999-10-12
                            tempColumnValue = tempColumnValue.substring(0, 10); //날짜만 자른다
                        }
                    }
                    tempResultText.append(String.format("%1$-" + tempColumnSize + "s", tempColumnValue)).append(" ");
                    columnSizes[idx] = tempColumnSize;
                    resultText.append( String.format("%1$-"+tempColumnSize+"s" , tempColumnLabel) ).append(" ");
                }
                resultText.append( "\n" );
                for (int idx = 1; idx <= columnCount; idx++) {
                    resultText.append( new String(new char[columnSizes[idx]]).replace("\0", "-") );
                    resultText.append(" ");
                }
                resultText.append( "\n" );
                resultText.append(tempResultText).append( "\n" );
                //두번째 행부터 가져오기
                int loopCount = 0;
                while (rs.next() && (loopCount < 1000 )) { // 1000행까지만 출력
                    loopCount++;
                    tempResultText.setLength(0);
                    for (int idx = 1; idx <= columnCount; idx++) {
                        tempColumnValue = rs.getString(idx);
                        if(tempColumnValue != null) {
                            tempColumnType = rsmd.getColumnType(idx);
                            if (tempColumnType == 93) { // data 형식이면, 10자리로 설정 1999-10-12
                                tempColumnValue = tempColumnValue.substring(0, 10); //날짜만 자른다
                            }
                        }
                        tempResultText.append(String.format("%1$-" + columnSizes[idx] + "s", tempColumnValue)).append(" ");
                    }
                    resultText.append( tempResultText ).append("\n");
                }
                aInfo.addProperty("resultset", resultText.toString());
                aInfo.addProperty("sqlindex", sqlIndex);
            } else {   //행이 존재하지 않을 경우, 데이터가 없는 경우
                aInfo.addProperty("success", true); //{"success":true}
                aInfo.addProperty("messageType", "query"); //{"success":true}
                aInfo.addProperty("FirstRowTime", "No Rows Retrieved In " + elapsedTime + " seconds"); //{"querySuccess":"", "FirstRowTime":""}

                JsonObject bInfo = new JsonObject(); // {}
                JsonArray infoArray = new JsonArray(); // []
                bInfo.addProperty(" ", " "); //{"success":true}
                infoArray.add(bInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
                aInfo.addProperty("resultset", resultText.toString());
                aInfo.addProperty("sqlindex", sqlIndex);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            aInfo = sqlExceptionBatch(e);
//            aInfo.addProperty("resultset", e.getMessage());
            aInfo.addProperty("sqlindex", sqlIndex);
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

    public JsonObject sqlExceptionBatch(SQLException e) {
        JsonObject aInfo = new JsonObject();
        aInfo.addProperty("success", false); //{"success":true}
        aInfo.addProperty("messageType", "batch"); //{"success":true}
        aInfo.addProperty("FirstRowTime", e.getMessage()); //{"success":"", "FirstRowTime":""}

//        JsonObject bInfo = new JsonObject(); // {}
//        JsonArray infoArray = new JsonArray(); // []
//        bInfo.addProperty(" ", " "); //{"success":true}
//        infoArray.add(bInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
        aInfo.addProperty("resultset", e.getMessage()); //{"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
        return aInfo;
    }
}
