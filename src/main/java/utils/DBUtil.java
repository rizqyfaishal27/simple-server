package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.AppConfig;

public class DBUtil {

    private static final Connection INSTANCE = createConnection();

    private static Connection createConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            String sqliteFile = AppConfig.SQLITE_FILE;
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
            return connection;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Connection getConnection() {
        return INSTANCE;
    }

    public static void setupDatabase() {
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30);
            statement.execute("create table if not exists users(user_id integer not null unique, name string, saldo integer);");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}