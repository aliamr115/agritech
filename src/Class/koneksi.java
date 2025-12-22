/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public class koneksi {
    public  static Connection mysqlConfig;
    
    public static Connection configDB(){
        try {
            String url = "jdbc:mysql://localhost:3306/agritech";
            String user = "root";
            String pass = "";
            
            mysqlConfig = DriverManager.getConnection(url, user, pass);
        } catch (SQLException sQLException) {
            System.out.println(sQLException.getMessage());
        }
        
        return mysqlConfig;
    }
    
}
