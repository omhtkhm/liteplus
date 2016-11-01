/**
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

    public String handleMessage(String message) {
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
            EmbeddedDatabase.insertSqlHist(message);//Query Embbeded Derby DB에 저장
            //실행시간 측정
            long start = System.currentTimeMillis(); // 실행시간 측정 용
            ResultSet rs = statement.executeQuery();

            boolean bFirestRow = rs.next(); // 첫번째 행 가져오기
            long end = System.currentTimeMillis(); //실행시간 측정용
            double elapsedTime = (end - start) / 1000.0;
//            Log.debug(( "실행 시간 : " + elapsedTime ));

            StringBuffer resultText=new StringBuffer();
            if (bFirestRow) {   // 첫행이 존재할 경우,
                aInfo.addProperty("success", true); //{"success":true}
                aInfo.addProperty("messageType", "batch"); //{"querySuccess":true}
                aInfo.addProperty("FirstRowTime", "First Rows Retrieved In " + elapsedTime + " seconds"); //{"querySuccess":"", "FirstRowTime":""}

                // 쿼리 결과를 정렬된 텍스트 포맷으로 변환해야 함. 우선 컬럼헤드값을 뽑아서 배열에 넣는다.
                // ResultSetMetaData의 데이터타입을 보고 컬럼의 크기를 결정해야 한다.
//                ArrayList<String> columnHeadNames = new ArrayList<>();
////                ArrayList<Integer> columnTypes = new ArrayList<>();
//                ArrayList<Integer> columnDisplaySizes = new ArrayList<>();
//                ArrayList<String> batchResultRow = new ArrayList<>();
//                ArrayList<String>[] batchResultRows = new ArrayList[100];
//                int[] temp = new int[100];

                ///////////////////////////
//                JsonObject bInfo = new JsonObject(); // {}
                // loop rs.getResultSetMetadata columns
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
//                    bInfo.addProperty(rsmd.getColumnLabel(idx), rs.getString(idx)); // { "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }
//                    columnHeadNames.add( rsmd.getColumnLabel(idx) );
                    tempColumnLabel = rsmd.getColumnLabel(idx);
//                    batchResultRow.add( rs.getString(idx));
//                    columnTypes.add(rsmd.getColumnType(idx));
                    tempColumnSize = rsmd.getColumnDisplaySize(idx);
//                    tempColumnType = rsmd.getColumnType(idx);
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
//                    columnDisplaySizes.add(tempColumnSize);
                    resultText.append( String.format("%1$-"+tempColumnSize+"s" , tempColumnLabel) ).append(" ");
                }
                resultText.append( "\n" );
                for (int idx = 1; idx <= columnCount; idx++) {
                    resultText.append( new String(new char[columnSizes[idx]]).replace("\0", "-") );
                    resultText.append(" ");
                }
                resultText.append( "\n" );
                resultText.append(tempResultText).append( "\n" );
                //컬럼의 사이즈를 FIX하기 위해서, 컬럼크기만큼의 텍스트를 만들어야 함
//                infoArray.add(bInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
//                batchResultRows[0] = batchResultRow;
//
//                //두번째 행 부터 가져오기
////                JsonObject cInfo = null;
                int loopCount = 0;
                while (rs.next() && (loopCount < 1000 )) { // 100회까지만 출력
                    // loop rs.getResultSetMetadata columns
//                    cInfo = new JsonObject();
//                    batchResultRow.clear();
                    loopCount++;
                    tempResultText.setLength(0);
                    for (int idx = 1; idx <= columnCount; idx++) {
//                        cInfo.addProperty(rsmd.getColumnLabel(idx), rs.getString(idx)); // { "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }
//                        batchResultRow.add( rs.getString(idx) );
                        tempColumnValue = rs.getString(idx);
                        if(tempColumnValue != null) {
                            tempColumnType = rsmd.getColumnType(idx);
                            if (tempColumnType == 93) { // data 형식이면, 10자리로 설정 1999-10-12
                                tempColumnValue = tempColumnValue.substring(0, 10); //날짜만 자른다
                            }
                        }
                        tempResultText.append(String.format("%1$-" + columnSizes[idx] + "s", tempColumnValue)).append(" ");
                    }
//                    infoArray.add(cInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" },{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
//                    batchResultRows[i] = batchResultRow;
                    resultText.append( tempResultText ).append("\n");
                }
//                aInfo.add("resultset", resultText); //{"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
                aInfo.addProperty("resultset", resultText.toString());
            } else {   //행이 존재하지 않을 경우, 데이터가 없는 경우
                aInfo.addProperty("success", true); //{"success":true}
                aInfo.addProperty("messageType", "query"); //{"success":true}
                aInfo.addProperty("FirstRowTime", "No Rows Retrieved In " + elapsedTime + " seconds"); //{"querySuccess":"", "FirstRowTime":""}

                JsonObject bInfo = new JsonObject(); // {}
                JsonArray infoArray = new JsonArray(); // []
                bInfo.addProperty(" ", " "); //{"success":true}
                infoArray.add(bInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
//                aInfo.add("resultset", resultText); //{"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
                aInfo.addProperty("resultset", resultText.toString());
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
        aInfo.addProperty("messageType", "batch"); //{"success":true}
        aInfo.addProperty("FirstRowTime", e.getMessage()); //{"success":"", "FirstRowTime":""}

        JsonObject bInfo = new JsonObject(); // {}
        JsonArray infoArray = new JsonArray(); // []
        bInfo.addProperty(" ", " "); //{"success":true}
        infoArray.add(bInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
        aInfo.add("resultset", infoArray); //{"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
        return aInfo;
    }
}
