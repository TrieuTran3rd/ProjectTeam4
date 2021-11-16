
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JdbcHelper {
    
    static String url = "jdbc:sqlserver://localhost:1433;databaseName=ky4Final;";
    static String user = "sa";
    static String pass = "songlong";

    public static PreparedStatement getStmt(String sql, Object... args) throws Exception {
        //nạp driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        Connection conn = DriverManager.getConnection(url, user, pass);
        
        PreparedStatement stmt;
        if (sql.trim().startsWith("{")) {
            stmt = conn.prepareCall(sql);//proc
        } else {
            stmt = conn.prepareStatement(sql);//sql
        }
        for (int i = 0; i < args.length; i++) {
            stmt.setObject(i + 1, args[i]);
        }
        return stmt;
    }

    ;
    // update xong đóng lại luôn
    public static int update(String sql, Object... args) throws ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try {
            PreparedStatement stmt = JdbcHelper.getStmt(sql, args);
            try {
                return stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e+"loi dong 48 jdbc helper");
        }
    }

    public static ResultSet query(String sql, Object... args) throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        PreparedStatement stmt = JdbcHelper.getStmt(sql, args);
        return stmt.executeQuery();
    }

    public static Object value(String sql, Object... args) throws ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            if (rs.next()) {
                return rs.getObject(0);
            }
            rs.getStatement().getConnection().close();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e+"loi dong 68 jdbc helper");
        }
    }
}
