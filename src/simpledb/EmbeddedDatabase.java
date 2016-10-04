package simpledb;

import java.sql.Connection;
//import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//import org.apache.derby.jdbc.EmbeddedDriver;

public class EmbeddedDatabase {
    static Connection connection=null;
    static boolean isTableCreated=false;

//    public static void main(String[] args) {
////        EmbeddedDatabase e = new EmbeddedDatabase();
////        e.testDerby();
//        initDB();
//        insertSqlHist("select * from emp");
//        selectSqlHist();
//        shutdownDB();
//    }

    public static void initDB(){
        if (connection==null) try {
            createDatabaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!isTableCreated) {
            createHistSqlTable();
            isTableCreated = true;
        }
//        return connection;
    }

    public static void createDatabaseConnection() throws SQLException, ClassNotFoundException {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driver);
        String url = "jdbc:derby:hist_sql.db;create=true";
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
//        return c;
    }

    public static void createHistSqlTable(){
        Statement stmt;
//        String createSQL = "create table sql_history ("
//                + "id integer not null generated always as"
//                + " identity (start with 1, increment by 1),   "
//                + "lastrun timestamp default current_timestamp, sqlstmt varchar(2000),"
//                + "constraint primary_key primary key (id))";
        String createSQL = "create table sql_history ("
                + "id integer not null,"
                + "lastrun timestamp default current_timestamp, sqlstmt varchar(2000),"
                + "constraint primary_key primary key (id))";

        try {
            stmt = connection.createStatement();
            stmt.execute(createSQL);
            isTableCreated=true;
        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
    }

    public static void insertSqlHist(String sql) {
        PreparedStatement pstmt;
        Statement stmt;
        ResultSet rs = null;
        int id = 0;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery("select case when max(id) is null then 1 else max(id)+1 end  from sql_history");
            if(rs.next()) id = rs.getInt(1);
            pstmt = connection.prepareStatement("insert into sql_history (id, sqlstmt) values(?,?)");
            pstmt.setInt(1, id);
            pstmt.setString(2, sql);
            pstmt.executeUpdate();
//            connection.commit();
        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
    }

    public static ResultSet selectSqlHist(int iCurrentIndex, String strDirection) {
        PreparedStatement pstmt;
        Statement stmt;
        ResultSet rs = null;
        int iMaxIndex = 0;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery("select max(id) from sql_history");
            if(rs.next()) {
                iMaxIndex = rs.getInt(1);
            }

            if(iCurrentIndex == 0) iCurrentIndex = iMaxIndex; // 0이 입력되면, ID최대값, 즉, 최근 SQL문을 가지고 온다
            else if ( iCurrentIndex > iMaxIndex ) iCurrentIndex = 1;

            if(strDirection.equals("next")) {
                pstmt = connection.prepareStatement("select min(id) from sql_history where id >= ?");
                pstmt.setInt(1, iCurrentIndex);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    iCurrentIndex = rs.getInt(1);
                }
            }

            if(strDirection.equals("previous")) {
                pstmt = connection.prepareStatement("select max(id) from sql_history where id <= ?");
                pstmt.setInt(1, iCurrentIndex);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    iCurrentIndex = rs.getInt(1);
                }
            }

            pstmt = connection.prepareStatement("select id, lastrun, sqlstmt from sql_history where id=?");
            pstmt.setInt(1, iCurrentIndex);
            rs = pstmt.executeQuery();

        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
        return rs;
    }

    public static ResultSet selectSqlHistGrid() {
        PreparedStatement pstmt;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement("select id, lastrun, sqlstmt from sql_history order by id desc");
            rs = pstmt.executeQuery();

        } catch (SQLException ex) {
            System.out.println("in connection" + ex);
        }
        return rs;
    }

//    public static Connection createDatabaseConnection() throws SQLException {
//        Driver derbyEmbeddedDriver = new EmbeddedDriver();
//        DriverManager.registerDriver(derbyEmbeddedDriver);
//
//        conn = DriverManager.getConnection("jdbc:derby:hist_sql.db;create=true");;
//        conn.setAutoCommit(true);
//    }

    public static void shutdownDB(){
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            if (((ex.getErrorCode() == 50000) &&
                    ("XJ015".equals(ex.getSQLState())))) {
                System.out.println("Derby shut down normally");
            } else {
                System.err.println("Derby did not shut down normally");
                System.err.println(ex.getMessage());
            }
        }
    }
}