/**
 * Created by win on 2016-09-18.
 */
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class TableInfo {
    Connection connection;
    PreparedStatement statement;
    ResultSet rs;
    String strModifiedSQLText;
    String strOwner;
    String strTable;

    public String handleMessage(String message) {
        System.out.println("receive from client : " + message);
        Log.debug("receive from client : " + message);

        String[] strOwnerTable = message.split("\\."); // ower, table명이 넘어와야 함, SCOTT.EMP 또는 EMP
        strOwner = strOwnerTable[0];
        strTable = strOwnerTable[1];
        Log.debug("Owner : " + strOwner);
        Log.debug("Table : " + strTable);

        JsonObject aInfo = new JsonObject();
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DSTest");
            connection = ds.getConnection();
            //////// JSON 만들기 ////////////////
            aInfo.addProperty("success", true); //{"querySuccess":true}
            aInfo.addProperty("messageType", "tableinfo"); //{("messageType", "tableinfo"}
            ////////////////////////////////////////////////
            /// Column정보를 가져와서 JsonObject 만들기
//            String strModifiedSQLText ="SELECT a.column_id,a.column_name,a.data_type,a.density,a.nullable,a.num_nulls,a.num_distinct,a.num_buckets,a.low_value,a.high_value,a.sample_size,a.last_analyzed,a.data_default,b.comments FROM all_tab_columns a, all_col_comments b WHERE 1=1 AND a.owner = b.owner AND a.table_name = b.table_name AND a.column_name = b.column_name AND a.owner = 'SCOTT' AND a.table_name= 'EMP' ORDER BY column_id";
            strModifiedSQLText ="SELECT a.column_id,a.column_name,a.data_type,a.density,a.nullable,a.num_nulls,a.num_distinct,a.num_buckets,a.low_value,a.high_value,a.sample_size,a.last_analyzed,a.data_default,b.comments FROM all_tab_columns a, all_col_comments b WHERE 1=1 AND a.owner = b.owner AND a.table_name = b.table_name AND a.column_name = b.column_name AND a.owner = ? AND a.table_name= ? ORDER BY column_id";
//            System.out.println("Column sql : " + strModifiedSQLText);
//            Log.debug("Column sql : " + strModifiedSQLText);
//            statement = connection.prepareStatement(strModifiedSQLText);
//            statement.setString(1, strOwner);
//            statement.setString(2, strTable);
//            rs = statement.executeQuery();

//            ResultSetToJsonObject rstojson = new ResultSetToJsonObject();
            ResultSetToJsonObject rstojson = processSQL();
            rstojson.processResultSet(rs, aInfo, "resultsetcolumninfo"); // aInfo에 json 리절트셋 추가, {"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
            ////////////////////////////////////////////////
            /// Index정보를 가져와서 JsonObject 만들기

//            strModifiedSQLText ="SELECT /* 하위 Tree */owner,sample_size,last_analyzed,partitioned,status,clustering_factor,leaf_blocks,distinct_keys,blevel,avg_leaf_blocks_per_key,avg_data_blocks_per_key,num_rows,degree,ini_trans,FREELISTS,TEMPORARY,generated,buffer_pool,table_owner,table_name,table_type,compression,prefix_length,tablespace_name,max_trans,initial_extent,min_extents,max_extents,pct_increase,pct_threshold,include_column,freelist_groups,pct_free,logging,instances,secondary,user_stats,duration,pct_direct_access,ityp_owner,ityp_name,parameters,global_stats,domidx_status,domidx_opstatus,funcidx_status,join_index FROM all_indexes WHERE 1=1 AND owner = 'SCOTT' AND table_name = 'EMP'";
            strModifiedSQLText ="SELECT /* 하위 Tree */owner,sample_size,last_analyzed,partitioned,status,clustering_factor,leaf_blocks,distinct_keys,blevel,avg_leaf_blocks_per_key,avg_data_blocks_per_key,num_rows,degree,ini_trans,FREELISTS,TEMPORARY,generated,buffer_pool,table_owner,table_name,table_type,compression,prefix_length,tablespace_name,max_trans,initial_extent,min_extents,max_extents,pct_increase,pct_threshold,include_column,freelist_groups,pct_free,logging,instances,secondary,user_stats,duration,pct_direct_access,ityp_owner,ityp_name,parameters,global_stats,domidx_status,domidx_opstatus,funcidx_status,join_index FROM all_indexes WHERE 1=1 AND owner = ? AND table_name = ?";
//            System.out.println("index sql : " + strModifiedSQLText);
//            Log.debug("index sql : " + strModifiedSQLText);
//            statement = connection.prepareStatement(strModifiedSQLText);
//            rs = statement.executeQuery();
//
//            rstojson = new ResultSetToJsonObject();
            rstojson = processSQL();
            rstojson.processResultSet(rs, aInfo, "resultsetindexinfo"); // aInfo에 json 리절트셋 추가

            //////////////////////////////////////////////////////
            /// Table 기본정보를 가져와서 JsonObject 만들기 , SELECT * FROM ALL_TABLES WHERE TABLE_NAME='EMP'
            strModifiedSQLText ="SELECT * FROM ALL_TABLES WHERE OWNER=? and TABLE_NAME=?";
//            System.out.println("tableinfo sql : " + strModifiedSQLText);
//            Log.debug("tableinfo sql : " + strModifiedSQLText);
//            statement = connection.prepareStatement(strModifiedSQLText);
//            rs = statement.executeQuery();
            rstojson = processSQL();
            rstojson.processResultSet(rs, aInfo, "resultsettableinfo"); // aInfo에 json 리절트셋 추가
            //////////////////////////////////////////////////////
            /// Constraint정보를 가져와서 JsonObject 만들기 ,
//            strModifiedSQLText ="SELECT c.constraint_name,c.constraint_type,TO_CHAR(NULL) COLUMNS,c.search_condition,c.r_owner,c.r_constraint_name,TO_CHAR( NULL ) ref_info,c.delete_rule,c.status,c.deferrable,c.deferred,c.validated,c.generated,c.last_change FROM all_constraints c WHERE 1=1 AND c.owner='SCOTT' AND c.table_name='EMP'";
            strModifiedSQLText ="SELECT c.constraint_name,c.constraint_type,TO_CHAR(NULL) COLUMNS,c.search_condition,c.r_owner,c.r_constraint_name,TO_CHAR( NULL ) ref_info,c.delete_rule,c.status,c.deferrable,c.deferred,c.validated,c.generated,c.last_change FROM all_constraints c WHERE 1=1 AND c.owner=? AND c.table_name=?";
//            System.out.println("constraintinfo sql : " + strModifiedSQLText);
//            Log.debug("constraintinfo sql : " + strModifiedSQLText);
//            statement = connection.prepareStatement(strModifiedSQLText);
//            rs = statement.executeQuery();
            rstojson = processSQL();
            rstojson.processResultSet(rs, aInfo, "resultsetconstraintinfo"); // aInfo에 json 리절트셋 추가
            //////////////////////////////////////////////////////
            /// Grants정보를 가져와서 JsonObject 만들기 ,
//            strModifiedSQLText ="SELECT grantor,grantee,privilege,grantable,hierarchy FROM all_tab_privs WHERE 1=1 AND table_schema='SYS' AND table_name='JAVASNM'";
            strModifiedSQLText ="SELECT grantor,grantee,privilege,grantable,hierarchy FROM all_tab_privs WHERE 1=1 AND table_schema=? AND table_name=?";
//            System.out.println("grantsinfo sql : " + strModifiedSQLText);
//            Log.debug("grantsinfo sql : " + strModifiedSQLText);
//            statement = connection.prepareStatement(strModifiedSQLText);
//            rs = statement.executeQuery();
            rstojson = processSQL();
            rstojson.processResultSet(rs, aInfo, "resultsetgrantsinfo"); // aInfo에 json 리절트셋 추가
            //////////////////////////////////////////////////////
            /// Trigger정보를 가져와서 JsonObject 만들기 ,
//            strModifiedSQLText = "select 'CREATE OR REPLACE TRIGGER ' || OWNER || '.' || TRIGGER_NAME || '  ' ||  case when INSTR(TRIGGER_TYPE,'STATEMENT')=0 then TRIGGER_TYPE else SUBSTR (TRIGGER_TYPE, 1, INSTR(TRIGGER_TYPE, 'STATEMENT')- 2) end  || ' ' || TRIGGERING_EVENT || ' ON ' || TABLE_OWNER || '.' || TABLE_NAME || ' ' || COLUMN_NAME ||  ' ' as triggerddl, TRIGGER_BODY from all_triggers where TABLE_OWNER='XDB' and TABLE_NAME='PATH_VIEW'";
//            strModifiedSQLText = "select 'CREATE OR REPLACE TRIGGER ' || OWNER || '.' || TRIGGER_NAME as TRGSTMT1, case when INSTR(TRIGGER_TYPE,'STATEMENT')=0 then TRIGGER_TYPE else SUBSTR (TRIGGER_TYPE, 1, INSTR(TRIGGER_TYPE, 'STATEMENT')- 2) end || ' ' || TRIGGERING_EVENT || ' ON ' || TABLE_OWNER || '.' || TABLE_NAME || ' ' || COLUMN_NAME as TRGSTMT2, TRIGGER_BODY from all_triggers where TABLE_OWNER='XDB' and TABLE_NAME='PATH_VIEW'";
            strModifiedSQLText = "select 'CREATE OR REPLACE TRIGGER ' || OWNER || '.' || TRIGGER_NAME as TRGSTMT1, case when INSTR(TRIGGER_TYPE,'STATEMENT')=0 then TRIGGER_TYPE else SUBSTR (TRIGGER_TYPE, 1, INSTR(TRIGGER_TYPE, 'STATEMENT')- 2) end || ' ' || TRIGGERING_EVENT || ' ON ' || TABLE_OWNER || '.' || TABLE_NAME || ' ' || COLUMN_NAME as TRGSTMT2, TRIGGER_BODY from all_triggers where TABLE_OWNER=? and TABLE_NAME=?";
//            System.out.println("triggerinfo sql : " + strModifiedSQLText);
//            Log.debug("triggerinfo sql : " + strModifiedSQLText);
//            statement = connection.prepareStatement(strModifiedSQLText);
//            rs = statement.executeQuery();
            rstojson = processSQL();
            rstojson.processResultSet(rs, aInfo, "resultsettriggerinfo"); // aInfo에 json 리절트셋 추가
            //////////////////////////////////////////////////////
            /// Trigger정보를 가져와서 JsonObject 만들기 ,
//            strModifiedSQLText = "select 'CREATE OR REPLACE TRIGGER ' || OWNER || '.' || TRIGGER_NAME || '  ' ||  case when INSTR(TRIGGER_TYPE,'STATEMENT')=0 then TRIGGER_TYPE else SUBSTR (TRIGGER_TYPE, 1, INSTR(TRIGGER_TYPE, 'STATEMENT')- 2) end  || ' ' || TRIGGERING_EVENT || ' ON ' || TABLE_OWNER || '.' || TABLE_NAME || ' ' || COLUMN_NAME ||  ' ' as triggerddl, TRIGGER_BODY from all_triggers where TABLE_OWNER='XDB' and TABLE_NAME='PATH_VIEW'";
//            strModifiedSQLText = "SELECT dbms_metadata.get_ddl( 'TABLE' , 'EMP' , 'SCOTT' ) as DDL from dual";
            strModifiedSQLText = "SELECT dbms_metadata.get_ddl( 'TABLE' , ? , ? ) as DDL from dual";
            statement = connection.prepareStatement(strModifiedSQLText);
            statement.setString(1, strTable);
            statement.setString(2, strOwner);
            rs = statement.executeQuery();
//            System.out.println("triggerinfo sql : " + strModifiedSQLText);
//            Log.debug("triggerinfo sql : " + strModifiedSQLText);
//            statement = connection.prepareStatement(strModifiedSQLText);
//            rs = statement.executeQuery();
            rstojson = new ResultSetToJsonObject();
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
        return bInfo;
    }

    public ResultSetToJsonObject processSQL() throws SQLException{
        System.out.println("sql : " + strModifiedSQLText);
        Log.debug("sql : " + strModifiedSQLText);
        statement = connection.prepareStatement(strModifiedSQLText);
        statement.setString(1, strOwner);
        statement.setString(2, strTable);
        rs = statement.executeQuery();

        ResultSetToJsonObject rstojson = new ResultSetToJsonObject();
        return rstojson;
    }
}
