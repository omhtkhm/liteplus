import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by win on 2016-09-17.
 */
public class Plan {
    Connection connection;
    PreparedStatement statement;
    ResultSet rs;

    //    private static final String SQL_LIST = "SELECT ID, ARTICLE_NAME from JCGEXAMPLE";
//    Log2 log;

//    public Plan() {
//        log = Log2.getInstance();
//    }

    public String handleMessage(String message) {
        System.out.println("receive from client : " + message);
        Log.debug("receive from client : " + message);
        ///////////////////////////////////////////////////////////////
        // DB 쿼리를 하여 resultset을 JSON으로 변환하여 전송한다.
        /////////////////////////////////////////////////////////////////

        String strSQLText = message;
        String strModifiedSQLText = QueryParser.removeSemicolon(strSQLText);
        System.out.println("semicolin deleted : " + strModifiedSQLText);
        Log.debug("semicolin deleted : " + strModifiedSQLText);

        StringBuffer aInfo = new StringBuffer("");
//        aInfo = new StringBuffer("{");
//        aInfo.append( "\"result\": \"success\",");
//        aInfo.append( "\"messageType\": \"plan\",");
//        aInfo.append("        \"root\": {");
//        aInfo.append("    \"expanded\": true,");
//        aInfo.append("                    \"iconCls\": \"ico-noimage\",");
//        aInfo.append("                    \"children\": [");
//        aInfo.append("{");
//        aInfo.append("\"text\": \"SELECT STATEMENT   ALL_ROWS-Cost : 6\",");
//        aInfo.append("                    \"expanded\": true,");
//        aInfo.append("                    \"iconCls\": \"ico-noimage\",");
//        aInfo.append("                    \"children\": [");
//        aInfo.append("{");
//        aInfo.append("                    \"text\": \"MERGE JOIN   \",");
//        aInfo.append("                        \"expanded\": true,");
//        aInfo.append("                        \"iconCls\": \"ico-noimage\",");
//        aInfo.append("                        \"children\": [");
//        aInfo.append("                    {");
//        aInfo.append("                        \"text\": \"TABLE ACCESS BY INDEX ROWID SCOTT.DEPT(2) ANALYZED\",");
//        aInfo.append("                            \"expanded\": true,");
//        aInfo.append("                            \"iconCls\": \"ico-noimage\",");
//        aInfo.append("                            \"children\": [");
//        aInfo.append("                        {");
//        aInfo.append("                            \"text\": \"INDEX FULL SCAN SCOTT.PK_DEPT ANALYZED\",");
//        aInfo.append("                                \"leaf\": true,");
//        aInfo.append("                                \"iconCls\": \"ico-noimage\"");
//        aInfo.append("                        }");
//        aInfo.append("                        ]");
//        aInfo.append("                    },");
//        aInfo.append("                    {");
//        aInfo.append("                        \"text\": \"SORT JOIN   (\\\"EMP\\\".\\\"DEPTNO\\\"=\\\"DEPT\\\".\\\"DEPTNO\\\") (\\\"EMP\\\".\\\"DEPTNO\\\"=\\\"DEPT\\\".\\\"DEPTNO\\\")\",");
//        aInfo.append("                            \"expanded\": true,");
//        aInfo.append("                            \"iconCls\": \"ico-noimage\",");
//        aInfo.append("                            \"children\": [");
//        aInfo.append("                        {");
//        aInfo.append("                            \"text\": \"TABLE ACCESS FULL SCOTT.EMP(1) ANALYZED\",");
//        aInfo.append("                                \"leaf\": true,");
//        aInfo.append("                                \"iconCls\": \"ico-noimage\"");
//        aInfo.append("                        }");
//        aInfo.append("                        ]");
//        aInfo.append("                    }");
//        aInfo.append("                    ]");
//        aInfo.append("}");
//        aInfo.append("]");
//        aInfo.append("}");
//        aInfo.append("]");
//        aInfo.append("}");
//        aInfo.append("}");
//
//        String bInfo = aInfo.toString();
//        Log.debug(bInfo);


        String strPlan = "EXPLAIN PLAN SET STATEMENT_ID = 'liteplusweb1' FOR "+strModifiedSQLText;
        try {
            InitialContext ctx = new InitialContext();
            // Here we lookup the datasource with the name
            // "java:comp/env/jdbc/jcgDS"
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DSTest");
            Connection connection = ds.getConnection();

            PreparedStatement statement = connection.prepareStatement(strPlan);

            statement.executeQuery();  // Explain Plan for ~~ 실행
            //
            String strLeafCheck = "select PARENT_ID from PLAN_TABLE where STATEMENT_ID = 'liteplusweb1' and PLAN_ID = (select MAX(PLAN_ID) from plan_table where STATEMENT_ID = 'liteplusweb1')";
            Log.debug(strLeafCheck);
            statement = connection.prepareStatement(strLeafCheck);
            rs = statement.executeQuery(); // Plan_table 쿼리 수행
            HashSet<Integer> setParentID = new HashSet(); // leaf 여부 계산을 위해 ParentID값이 없는 것을 찾아야 함
            while (rs.next()) {
                setParentID.add(rs.getInt("PARENT_ID"));
            }
            //
            String strPlanQueryResult = "select OPERATION || ' ' || OPTIONS || ' ' ||  (case when OBJECT_OWNER is null then '' else (OBJECT_OWNER || '.') end) ||  OBJECT_NAME || (case when OBJECT_INSTANCE is null then ' ' else '(' ||  OBJECT_INSTANCE || ') ' end) ||  OPTIMIZER || (case when rownum=1 then '-Cost : ' || COST end) || (case when ACCESS_PREDICATES is null then '' else ' (' ||  ACCESS_PREDICATES || ')' end) || (case when FILTER_PREDICATES is null then '' else ' (' ||  FILTER_PREDICATES || ')' end)  as planinfo, ID, PARENT_ID, DEPTH from PLAN_TABLE where STATEMENT_ID = 'liteplusweb1' and PLAN_ID = (select MAX(PLAN_ID) from plan_table where STATEMENT_ID = 'liteplusweb1')";
            Log.debug(strPlanQueryResult);
            statement = connection.prepareStatement(strPlanQueryResult);
            rs = statement.executeQuery(); // Plan_table 쿼리 수행

            ArrayList<Character> arrBracket = new ArrayList(); // 괄호를 제대로 닫기위해 임시로 생성

            aInfo.append("{");
            arrBracket.add('{');
            Log.debug("{");

            aInfo.append("\"success\": true,"); //{"success":true}
            aInfo.append("\"messageType\": \"plan\",");

            // root노드를 만들어 주려고 추가함
            aInfo.append("\"root\" : ");
            aInfo.append("{");
            arrBracket.add('{');
            Log.debug("{");

            aInfo.append("\"expanded\":true,");
            aInfo.append("\"iconCls\":\"co-noimage\",");
            ///////////// 여기까지가 root 노드를 위해 추가한 부분

            aInfo.append("\"children\": ");
            aInfo.append("[");
            arrBracket.add('[');
            Log.debug("[");


            int intPrevDepth = 0;
            int intCurrentDepth = 0;

            while (rs.next()) {
                // loop rs.getResultSetMetadata columns
                intCurrentDepth = rs.getInt("DEPTH");
                if( intCurrentDepth > intPrevDepth){
                    aInfo.append("\"children\" : ");
                    aInfo.append("[");
                    arrBracket.add('[');
                    Log.debug("[");
                }else if(intCurrentDepth !=0 && intCurrentDepth == intPrevDepth){
                    aInfo.append("},");
                    arrBracket.remove(arrBracket.size()-1);
                    Log.debug("},");
                }else if(intCurrentDepth < intPrevDepth){
                    aInfo.append("}");
                    arrBracket.remove(arrBracket.size()-1);
                    Log.debug("}");
                    aInfo.append("]");
                    arrBracket.remove(arrBracket.size()-1);
                    Log.debug("]");
                    aInfo.append("},");
                    arrBracket.remove(arrBracket.size()-1);
                    Log.debug("},");
                }

                aInfo.append("{");
                arrBracket.add('{');
                Log.debug("{");
                String strPlanInfo = rs.getString("PLANINFO");
                strPlanInfo = strPlanInfo.replace("\"","\\\""); // "를 \"로 변환
                aInfo.append("\"text\" : \"").append(strPlanInfo).append("\",");
                if(setParentID.contains(rs.getInt("ID"))) {
                    aInfo.append("\"expanded\" : true,");
                } else {
                    aInfo.append("\"leaf\" : true,");
                }
                aInfo.append("\"iconCls\" : \"ico-noimage\",");
                intPrevDepth = rs.getInt("DEPTH");
            }

            for( int i=(arrBracket.size()-1);i>=0;i--){
                if( arrBracket.get(i) == '{') {
                    aInfo.append("}");
                    arrBracket.remove(i);
                    Log.debug("}");
                } else {
                    aInfo.append("]");
                    arrBracket.remove(i);
                    Log.debug("]");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            Log.debug(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            Log.debug(e.getMessage());
        }finally {
            if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (statement != null) try { statement.close(); } catch(SQLException ex) {}
            if (connection != null) try { connection.close(); } catch(SQLException ex) {}
        }
        String bInfo = aInfo.toString();
        Log.debug(bInfo);
        return bInfo;
    }
}
