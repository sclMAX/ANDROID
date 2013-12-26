package ar.com.sclmax.pruebamysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectarMySQL {
	private static String Host = "sql3.freesqldatabase.com";
    private static String User = "sql312769";
    private static String Password = "vP7!sZ8%";
    private static String DataBase = "sql312769";
    private static String port = "3306";
    
    public static Connection conectar() throws SQLException, ClassNotFoundException {
    	String urlConexionMySQL = "";
    	urlConexionMySQL = "jdbc:mysql://" + Host + ":" + port+ "/"+DataBase;
    	Class.forName("com.mysql.jdbc.Driver");
        
        return DriverManager.getConnection(urlConexionMySQL,User, Password);
    }

}
