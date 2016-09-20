/**
 * Created by win on 2016-09-18.
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class TableInfo {
    Connection connection;
    PreparedStatement statement;
    ResultSet rs;
//    ResultSetMetaData rsmd;

//    Log2 log;

//    public TableInfo() {
//        log = Log2.getInstance();
//    }

    public String handleMessage(String message) {
        System.out.println("receive from client : " + message);
        Log.debug("receive from client : " + message);
        ///////////////////////////////////////////////////////////////
        // DB 쿼리를 하여 resultset을 JSON으로 변환하여 전송한다.
        /////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////
        /// Table Column정보를 가져와서 JsonObject 만들기
        String strSQLText = message;
//        String strModifiedSQLText ="select column_id, column_name, data_type,  density, nullable, num_nulls, num_distinct, num_buckets, low_value, high_value, sample_size, last_analyzed, data_default from all_tab_columns where table_name='EMP' order by column_id";
//        String strModifiedSQLText ="select column_id, column_name, data_type,  density, nullable, num_nulls, num_distinct, num_buckets, low_value, high_value, sample_size, last_analyzed, data_default from all_tab_columns where table_name='EMP' order by column_id";
        String strModifiedSQLText ="SELECT a.column_id,a.column_name,a.data_type,a.density,a.nullable,a.num_nulls,a.num_distinct,a.num_buckets,a.low_value,a.high_value,a.sample_size,a.last_analyzed,a.data_default,b.comments FROM all_tab_columns a, all_col_comments b WHERE 1=1 AND a.owner = b.owner AND a.table_name = b.table_name AND a.column_name = b.column_name AND a.owner = 'SCOTT' AND a.table_name= 'EMP' ORDER BY column_id";
        System.out.println("columninfo sql : " + strModifiedSQLText);
        Log.debug("columninfo sql : " + strModifiedSQLText);

        JsonObject aInfo = new JsonObject();
//        String replymessage = "{\"success\":true,\"messageType\":\"tableinfo\",\"resultset\":[{\"EMPNO\":\"7782\",\"ENAME\":\"CLARK\",\"JOB\":\"MANAGER\",\"MGR\":\"7839\",\"HIREDATE\":\"1981-06-09 00:00:00\",\"SAL\":\"2450\",\"COMM\":null,\"DEPTNO\":\"10\",\"DEPTNO\":\"10\",\"DNAME\":\"ACCOUNTING\",\"LOC\":\"NEW YORK\"},{\"EMPNO\":\"7839\",\"ENAME\":\"KING\",\"JOB\":\"PRESIDENT\",\"MGR\":null,\"HIREDATE\":\"1981-11-17 00:00:00\",\"SAL\":\"5000\",\"COMM\":null,\"DEPTNO\":\"10\",\"DEPTNO\":\"10\",\"DNAME\":\"ACCOUNTING\",\"LOC\":\"NEW YORK\"},{\"EMPNO\":\"7934\",\"ENAME\":\"MILLER\",\"JOB\":\"CLERK\",\"MGR\":\"7782\",\"HIREDATE\":\"1982-01-23 00:00:00\",\"SAL\":\"1300\",\"COMM\":null,\"DEPTNO\":\"10\",\"DEPTNO\":\"10\",\"DNAME\":\"ACCOUNTING\",\"LOC\":\"NEW YORK\"},{\"EMPNO\":\"7566\",\"ENAME\":\"JONES\",\"JOB\":\"MANAGER\",\"MGR\":\"7839\",\"HIREDATE\":\"1981-04-02 00:00:00\",\"SAL\":\"2975\",\"COMM\":null,\"DEPTNO\":\"20\",\"DEPTNO\":\"20\",\"DNAME\":\"RESEARCH\",\"LOC\":\"DALLAS\"},{\"EMPNO\":\"7902\",\"ENAME\":\"FORD\",\"JOB\":\"ANALYST\",\"MGR\":\"7566\",\"HIREDATE\":\"1981-12-03 00:00:00\",\"SAL\":\"3000\",\"COMM\":null,\"DEPTNO\":\"20\",\"DEPTNO\":\"20\",\"DNAME\":\"RESEARCH\",\"LOC\":\"DALLAS\"},{\"EMPNO\":\"7876\",\"ENAME\":\"ADAMS\",\"JOB\":\"CLERK\",\"MGR\":\"7788\",\"HIREDATE\":\"1987-05-23 00:00:00\",\"SAL\":\"1100\",\"COMM\":null,\"DEPTNO\":\"20\",\"DEPTNO\":\"20\",\"DNAME\":\"RESEARCH\",\"LOC\":\"DALLAS\"},{\"EMPNO\":\"7369\",\"ENAME\":\"SMITH\",\"JOB\":\"CLERK\",\"MGR\":\"7902\",\"HIREDATE\":\"1980-12-17 00:00:00\",\"SAL\":\"800\",\"COMM\":null,\"DEPTNO\":\"20\",\"DEPTNO\":\"20\",\"DNAME\":\"RESEARCH\",\"LOC\":\"DALLAS\"},{\"EMPNO\":\"7788\",\"ENAME\":\"SCOTT\",\"JOB\":\"ANALYST\",\"MGR\":\"7566\",\"HIREDATE\":\"1987-04-19 00:00:00\",\"SAL\":\"3000\",\"COMM\":null,\"DEPTNO\":\"20\",\"DEPTNO\":\"20\",\"DNAME\":\"RESEARCH\",\"LOC\":\"DALLAS\"},{\"EMPNO\":\"7521\",\"ENAME\":\"WARD\",\"JOB\":\"SALESMAN\",\"MGR\":\"7698\",\"HIREDATE\":\"1981-02-22 00:00:00\",\"SAL\":\"1250\",\"COMM\":\"500\",\"DEPTNO\":\"30\",\"DEPTNO\":\"30\",\"DNAME\":\"SALES\",\"LOC\":\"CHICAGO\"},{\"EMPNO\":\"7844\",\"ENAME\":\"TURNER\",\"JOB\":\"SALESMAN\",\"MGR\":\"7698\",\"HIREDATE\":\"1981-09-08 00:00:00\",\"SAL\":\"1500\",\"COMM\":\"0\",\"DEPTNO\":\"30\",\"DEPTNO\":\"30\",\"DNAME\":\"SALES\",\"LOC\":\"CHICAGO\"},{\"EMPNO\":\"7499\",\"ENAME\":\"ALLEN\",\"JOB\":\"SALESMAN\",\"MGR\":\"7698\",\"HIREDATE\":\"1981-02-20 00:00:00\",\"SAL\":\"1600\",\"COMM\":\"300\",\"DEPTNO\":\"30\",\"DEPTNO\":\"30\",\"DNAME\":\"SALES\",\"LOC\":\"CHICAGO\"},{\"EMPNO\":\"7900\",\"ENAME\":\"JAMES\",\"JOB\":\"CLERK\",\"MGR\":\"7698\",\"HIREDATE\":\"1981-12-03 00:00:00\",\"SAL\":\"950\",\"COMM\":null,\"DEPTNO\":\"30\",\"DEPTNO\":\"30\",\"DNAME\":\"SALES\",\"LOC\":\"CHICAGO\"},{\"EMPNO\":\"7698\",\"ENAME\":\"BLAKE\",\"JOB\":\"MANAGER\",\"MGR\":\"7839\",\"HIREDATE\":\"1981-05-01 00:00:00\",\"SAL\":\"2850\",\"COMM\":null,\"DEPTNO\":\"30\",\"DEPTNO\":\"30\",\"DNAME\":\"SALES\",\"LOC\":\"CHICAGO\"},{\"EMPNO\":\"7654\",\"ENAME\":\"MARTIN\",\"JOB\":\"SALESMAN\",\"MGR\":\"7698\",\"HIREDATE\":\"1981-09-28 00:00:00\",\"SAL\":\"1250\",\"COMM\":\"1400\",\"DEPTNO\":\"30\",\"DEPTNO\":\"30\",\"DNAME\":\"SALES\",\"LOC\":\"CHICAGO\"}]}";
        try {
            InitialContext ctx = new InitialContext();
            // Here we lookup the datasource with the name
            // "java:comp/env/jdbc/jcgDS"
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DSTest");
            Connection connection = ds.getConnection();

            PreparedStatement statement = connection.prepareStatement(strModifiedSQLText);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
//            boolean bFirestRow = rs.next(); // 첫번째 행 가져오기

            aInfo.addProperty("success", true); //{"querySuccess":true}
            aInfo.addProperty("messageType", "tableinfo"); //{("messageType", "tableinfo"}

            JsonArray infoArray = new JsonArray(); // []
            //결과행 가져오기
            JsonObject cInfo = null;
            while (rs.next()) {
                // loop rs.getResultSetMetadata columns
                cInfo = new JsonObject();
                for (int idx = 1; idx <= rsmd.getColumnCount(); idx++) {
                    cInfo.addProperty(rsmd.getColumnLabel(idx), rs.getString(idx)); // { "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }
//                    Log.debug(rsmd.getColumnLabel(idx) + " : " + rs.getString(idx));
                }
                infoArray.add(cInfo); // [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" },{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]
            }
            aInfo.add("resultsetcolumninfo", infoArray); //{"querySuccess": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}

            ////////////////////////////////////////////////
            /// Index정보를 가져와서 JsonObject 만들기

            strModifiedSQLText ="SELECT /* 하위 Tree */owner,sample_size,last_analyzed,partitioned,status,clustering_factor,leaf_blocks,distinct_keys,blevel,avg_leaf_blocks_per_key,avg_data_blocks_per_key,num_rows,degree,ini_trans,FREELISTS,TEMPORARY,generated,buffer_pool,table_owner,table_name,table_type,compression,prefix_length,tablespace_name,max_trans,initial_extent,min_extents,max_extents,pct_increase,pct_threshold,include_column,freelist_groups,pct_free,logging,instances,secondary,user_stats,duration,pct_direct_access,ityp_owner,ityp_name,parameters,global_stats,domidx_status,domidx_opstatus,funcidx_status,join_index FROM all_indexes WHERE 1=1 AND owner = 'SCOTT' AND table_name = 'EMP'";
            System.out.println("index sql : " + strModifiedSQLText);
            Log.debug("index sql : " + strModifiedSQLText);
            statement = connection.prepareStatement(strModifiedSQLText);
            rs = statement.executeQuery();

            ResultSetToJsonObject rstojson = new ResultSetToJsonObject();
            rstojson.processResultSet(rs, aInfo, "resultsetindexinfo"); // aInfo에 json 리절트셋 추가

            //////////////////////////////////////////////////////
            /// Table 기본정보를 가져와서 JsonObject 만들기 , SELECT * FROM ALL_TABLES WHERE TABLE_NAME='EMP'
            strModifiedSQLText ="SELECT * FROM ALL_TABLES WHERE TABLE_NAME='EMP'";
            System.out.println("tableinfo sql : " + strModifiedSQLText);
            Log.debug("tableinfo sql : " + strModifiedSQLText);
            statement = connection.prepareStatement(strModifiedSQLText);
            rs = statement.executeQuery();
            rstojson.processResultSet(rs, aInfo, "resultsettableinfo"); // aInfo에 json 리절트셋 추가
            //////////////////////////////////////////////////////
            /// Constraint정보를 가져와서 JsonObject 만들기 ,
            strModifiedSQLText ="SELECT c.constraint_name,c.constraint_type,TO_CHAR(NULL) COLUMNS,c.search_condition,c.r_owner,c.r_constraint_name,TO_CHAR( NULL ) ref_info,c.delete_rule,c.status,c.deferrable,c.deferred,c.validated,c.generated,c.last_change FROM all_constraints c WHERE 1=1 AND c.owner='SCOTT' AND c.table_name='EMP'";
            System.out.println("constraintinfo sql : " + strModifiedSQLText);
            Log.debug("constraintinfo sql : " + strModifiedSQLText);
            statement = connection.prepareStatement(strModifiedSQLText);
            rs = statement.executeQuery();
            rstojson.processResultSet(rs, aInfo, "resultsetconstraintinfo"); // aInfo에 json 리절트셋 추가
            //////////////////////////////////////////////////////
            /// Grants정보를 가져와서 JsonObject 만들기 ,
            strModifiedSQLText ="SELECT grantor,grantee,privilege,grantable,hierarchy FROM all_tab_privs WHERE 1=1 AND table_schema='SYS' AND table_name='JAVASNM'";
            System.out.println("grantsinfo sql : " + strModifiedSQLText);
            Log.debug("grantsinfo sql : " + strModifiedSQLText);
            statement = connection.prepareStatement(strModifiedSQLText);
            rs = statement.executeQuery();
            rstojson.processResultSet(rs, aInfo, "resultsetgrantsinfo"); // aInfo에 json 리절트셋 추가
            //////////////////////////////////////////////////////
            /// Trigger정보를 가져와서 JsonObject 만들기 ,
//            strModifiedSQLText = "select 'CREATE OR REPLACE TRIGGER ' || OWNER || '.' || TRIGGER_NAME || '  ' ||  case when INSTR(TRIGGER_TYPE,'STATEMENT')=0 then TRIGGER_TYPE else SUBSTR (TRIGGER_TYPE, 1, INSTR(TRIGGER_TYPE, 'STATEMENT')- 2) end  || ' ' || TRIGGERING_EVENT || ' ON ' || TABLE_OWNER || '.' || TABLE_NAME || ' ' || COLUMN_NAME ||  ' ' as triggerddl, TRIGGER_BODY from all_triggers where TABLE_OWNER='XDB' and TABLE_NAME='PATH_VIEW'";
            strModifiedSQLText = "select 'CREATE OR REPLACE TRIGGER ' || OWNER || '.' || TRIGGER_NAME as TRGSTMT1, case when INSTR(TRIGGER_TYPE,'STATEMENT')=0 then TRIGGER_TYPE else SUBSTR (TRIGGER_TYPE, 1, INSTR(TRIGGER_TYPE, 'STATEMENT')- 2) end || ' ' || TRIGGERING_EVENT || ' ON ' || TABLE_OWNER || '.' || TABLE_NAME || ' ' || COLUMN_NAME as TRGSTMT2, TRIGGER_BODY from all_triggers where TABLE_OWNER='XDB' and TABLE_NAME='PATH_VIEW'";
            System.out.println("triggerinfo sql : " + strModifiedSQLText);
            Log.debug("triggerinfo sql : " + strModifiedSQLText);
            statement = connection.prepareStatement(strModifiedSQLText);
            rs = statement.executeQuery();
            rstojson.processResultSet(rs, aInfo, "resultsettriggerinfo"); // aInfo에 json 리절트셋 추가
            //////////////////////////////////////////////////////
            /// Trigger정보를 가져와서 JsonObject 만들기 ,
//            strModifiedSQLText = "select 'CREATE OR REPLACE TRIGGER ' || OWNER || '.' || TRIGGER_NAME || '  ' ||  case when INSTR(TRIGGER_TYPE,'STATEMENT')=0 then TRIGGER_TYPE else SUBSTR (TRIGGER_TYPE, 1, INSTR(TRIGGER_TYPE, 'STATEMENT')- 2) end  || ' ' || TRIGGERING_EVENT || ' ON ' || TABLE_OWNER || '.' || TABLE_NAME || ' ' || COLUMN_NAME ||  ' ' as triggerddl, TRIGGER_BODY from all_triggers where TABLE_OWNER='XDB' and TABLE_NAME='PATH_VIEW'";
            strModifiedSQLText = "SELECT dbms_metadata.get_ddl( 'TABLE' , 'EMP' , 'SCOTT' ) as DDL from dual";
            System.out.println("triggerinfo sql : " + strModifiedSQLText);
            Log.debug("triggerinfo sql : " + strModifiedSQLText);
            statement = connection.prepareStatement(strModifiedSQLText);
            rs = statement.executeQuery();
            rstojson.processResultSet(rs, aInfo, "resultsetddlinfo"); // aInfo에 json 리절트셋 추가

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
//        return replymessage;
        return bInfo;
    }
}
