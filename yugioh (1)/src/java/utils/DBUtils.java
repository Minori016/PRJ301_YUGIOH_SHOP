/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tungi
 */
public class DBUtils {
    private static final String DB_NAME = "YUGIOH_TCG_SHOP";
    private static final String DB_USER_NAME = "SA";
    private static final String DB_PASSWORD = "12345";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;databaseName=" + DB_NAME;
        conn = DriverManager.getConnection(url, DB_USER_NAME, DB_PASSWORD);
        return conn;
    }
    
  public static void main(String[] args) {
    try {
        Connection conn = DBUtils.getConnection();
        if (conn != null) {
            System.out.println("✅ Connected to: " + conn.getCatalog());
        } else {
            System.out.println("❌ Connection failed!");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
