package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class SQLHandler {
    private static Connection connection;
    private static PreparedStatement psGetNickname;
    private static PreparedStatement psRegistration;
    private static PreparedStatement psChangeNick;

    public static boolean connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            preparedAllStatements();
            return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
               }
         catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void preparedAllStatements() throws SQLException {
        psGetNickname = connection.prepareStatement("SELECT name FROM users WHERE nickname = ? AND password = ?; ");
        psRegistration = connection.prepareStatement("INSERT INTO users(name , nickname, password) VALUES (?, ?, ?);");
        psChangeNick = connection.prepareStatement("UPDATE users SET name = ? WHERE name = ?;");
    }

    public static String getNicknameByLoginAndPassword(String login, String password){
        String nick = null;
        try {
            psGetNickname.setString(1,login);
            psGetNickname.setString(2, password);
            ResultSet resultSet = psGetNickname.executeQuery();
            if(resultSet.next()){
                nick = resultSet.getString(1);
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return nick;

    }
    public static boolean registration(String login, String password, String nickName){
        try {
            psRegistration.setString(1, login);
            psRegistration.setString(2, password);
            psRegistration.setString(3,nickName);
            psRegistration.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

    }
    public static boolean changeNick(String oldNickname, String newNickname){
        try {
            psChangeNick.setString(1, oldNickname);
            psChangeNick.setString(2, newNickname);
            psChangeNick.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

    }
    public static void disconnect(){
        try {
            psRegistration.close();
            psChangeNick.close();
            psGetNickname.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
