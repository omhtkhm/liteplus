import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hun on 2016-09-27.
 */
public class Merge {
    Connection connection;
    PreparedStatement statement;
    ResultSet rs;
    String strModifiedSQLText;
    String strOwner;
    String strTable;

    public String handleMessage(String message) {
        System.out.println("receive from client : " + message);
        Log.debug("receive from client : " + message);

        JsonObject aInfo = new JsonObject();
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DSTest");
            connection = ds.getConnection();
            if(message.contains(".")){ //owner와 같이 받았으면
                String[] strOwnerTable = message.split("\\."); // ower, view명이 넘어와야 함, SCOTT.EMP 또는 EMP
                strOwner = strOwnerTable[0];
                strTable = strOwnerTable[1];
            }else {   //뷰명만 받았으면
                strTable = message;
                strOwner = GetOwnerFromView();
            }
            Log.debug("Owner : " + strOwner);
            Log.debug("Table : " + strTable);
            //테이블이 존재하는지 Check
            strModifiedSQLText ="select count(*) as VIEWCOUNT from all_views where OWNER = ? and VIEW_NAME = ?";
            processSQL(); //DB쿼리 후 결과값을 rs멤버변수에 저장
            // aInfo에 json 리절트셋 추가, {"tablecount": ["TABLECOUNT": "0"]}
            rs.next();
            if(rs.getInt("VIEWCOUNT")==0){  // 테이블이 존재하지 않으면
                throw new NoRowsException();
            }
            //////// JSON 만들기 ////////////////
            aInfo.addProperty("success", true); //{"success":true}
            aInfo.addProperty("messageType", "merge"); //{("messageType", "merge"}
            /// VIEW sql정보를 가져와서 JsonObject 만들기
            strModifiedSQLText ="SELECT TEXT from ALL_VIEWS a WHERE a.owner = ? AND a.view_name= ?";
            ResultSetToJsonObject rstojson = processSQL();
            rstojson.processResultSet(rs, aInfo, "resultsetmerge"); // aInfo에 json 리절트셋 추가, {"success": cc, "FirstRowTime": dd, "resultset": [{ "컬럼명":"컬럼값", "컬럼명":"컬럼값",  "컬럼명":"컬럼값" }]}
        }catch (NoRowsException e) {
            // 테이블이 존재하지 않으므로, success: false, message: "OWNER.TABLE is not found"
            aInfo.addProperty("success", false); //{"success":false}
            aInfo.addProperty("messageType", "tableinfo"); //{("messageType", "tableinfo"}
            aInfo.addProperty("errormessage", "[VIEW] "+message+"\nNo such object found"); //뷰가 존재하지 않음
            Log.debug(e.getMessage());
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

    public String GetOwnerFromView() throws SQLException{
        strModifiedSQLText ="select OWNER from all_views where view_name=?";
        System.out.println("sql : " + strModifiedSQLText);
        Log.debug("sql : " + strModifiedSQLText);
        statement = connection.prepareStatement(strModifiedSQLText);
        statement.setString(1, strTable);
        rs = statement.executeQuery();
        String  owner = "";
        while (rs.next()) {
            owner = rs.getString("OWNER");
        }
        return owner;
    }
}
