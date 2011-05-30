package usecase.activerecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
    private static Connection conn = null;
    private static String dbHost = "localhost";
    private static String dbPort = "3306";
    private static String database = "jseussdb";
    private static String dbUser = "root";
    private static String dbPassword = "root";
    
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":"
                    + dbPort + "/" + database + "?" + "user=" + dbUser + "&"
                    + "password=" + dbPassword);
		} catch (ClassNotFoundException e) {
            System.out.println("Treiber nicht gefunden");
        } catch (SQLException e) {
        	e.printStackTrace();
            System.out.println("Connect nicht moeglich");
        }
	}
	
	public void save(Person person) {
		Statement query;
        try {
            query = conn.createStatement();
            String sql = "INSERT INTO person (name, prename, age) VALUES ('" +
            		person.getName() + "', '" + person.getPrename() + "', " + person.getAge() + ")"; 
            query.execute(sql);
        } catch (SQLException e) {
        	System.out.println("save error: " + e.getMessage());
        }
	}
}
