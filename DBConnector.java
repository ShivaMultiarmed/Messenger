package com.example.messenger;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.Driver;

public class DBConnector implements Serializable {

    public static final byte NONE = 0, LOGIN_USER_NOT_FOUND = -1, LOGIN_PASSWORD_INCORRECT = -2;
    public static final byte SIGNUP_OK = 2, SIGNUP_USER_EXISTS = -11;
    public Connection mysqlConnection;
    private final String host = "server197.hosting.reg.ru", username = "u1964695_default", password = "dtjCXRmHMfqR0456", db = "u1964695_default";

    private long userid;
    public DBConnector() {

    }

    public void connect()
    {
        try {
            mysqlConnection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/"+db, username, password);

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public ResultSet getAllMessages(long chatid)
    {
        Statement stmt = null;
        ResultSet set = null;
        try {
            stmt = mysqlConnection.createStatement();
            String sql = "SELECT `messageid`,`users`.`userid` as `userid`, `nick`, `text`, `datetime` FROM `messages` \n" +
                    "INNER JOIN `users`\n" +
                    "ON `messages`.`userid` = `users`.`userid`\n" +
                    "WHERE chatid = " + chatid + "\n" +
                    "ORDER BY `datetime`;";

            set = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return set;
    }

    public ResultSet getAllChatInfos(long userid)
    {
        Statement stmt = null;
        ResultSet set = null;
        try
        {
            stmt = mysqlConnection.createStatement();

            String sql = "SELECT `chats`.`chatname` as chatname," +
                    " alllastmsgs.text as text," +
                    " alllastmsgs.datetime as datetime," +
                    " chats.chatid as chatid, " +
                    "chats.chattype as chattype" +
                    " FROM `chats` \n" +
                    "INNER JOIN `chatsbelong`\n" +
                    "ON chats.chatid = chatsbelong.chatid\n" +
                    "\n" +
                    "INNER JOIN \n" +
                    "\n" +
                    "(\n" +
                    "    SELECT * FROM `messages` WHERE `messages`.`messageid` IN\n" +
                    "    (SELECT MAX(`messageid`) as lastmsg FROM `messages` GROUP BY `chatid`)\n" +
                    ") as alllastmsgs\n" +
                    "ON `alllastmsgs`.chatid = `chats`.`chatid`\n" +
                    "\n" +
                    "WHERE `chatsbelong`.`userid` = " + userid + ";";
            set = stmt.executeQuery(sql);

            //stmt.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace(System.err);
        }
        return set;
    }


    public void sendMessage(long userid, long chatid, String text)
    {
        try
        {
            Statement stmt = mysqlConnection.createStatement();

            stmt.executeUpdate("INSERT INTO `messages` (`userid`, `chatid`, `text`) VALUES ("+userid+", "+chatid+", '" + text + "');");

            stmt.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace(System.err);
        }

    }

    public ResultSet getAllUsers(long chatid) {
        ResultSet set = null;
        try {
            Statement stmt = mysqlConnection.createStatement();

            String sql = "SELECT " +
                    "`users`.`userid` as userid," +
                    "`nick`" +
                    " FROM `users`" +
                    " INNER JOIN `chatsbelong`" +
                    " ON `users`.`userid` = `chatsbelong`.`userid`" +
                    " WHERE `chatid` = " + chatid + ";";
            set = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }

        return set;
    }
    public ResultSet getPartner(long chatid, long userid)
    {
        ResultSet partner = null;
        try
        {
            Statement stmt  = mysqlConnection.createStatement();
            String query = "SELECT users.userid as userid," +
                    "nick" +
                    "  FROM `users`" +
                    "INNER JOIN `chatsbelong`" +
                    "ON `users`.userid = chatsbelong.userid " +
                    "WHERE chatsbelong.chatid = " + chatid + " AND " +
                    "users.userid <> "+userid+";";
            System.out.println(query);
            partner = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return partner;
    }

    public long logIn(String nick, String password)
    {
        long result = NONE; // is supposed to be the userid

        try
        {
            Statement stmt = mysqlConnection.createStatement();
            ResultSet user = stmt.executeQuery("SELECT * FROM `users` WHERE `nick` = '"+nick+"';");
            if (user.next())
            {
                String pass = user.getString("password");
                if (!password.equals(pass))
                    result = LOGIN_PASSWORD_INCORRECT;
                else
                    result = user.getLong("userid");
            }
            else
                result = LOGIN_USER_NOT_FOUND;
            user.close();
            stmt.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace(System.err);
        }

        return result;
    }

    public ResultSet getAllAttachments(long chatid) {

        ResultSet set = null;

        try
        {
            Statement stmt = mysqlConnection.createStatement();
            String sql = "SELECT * FROM attachments\n" +
                    "INNER JOIN messages\n" +
                    "ON attachments.messageid = messages.messageid\n" +
                    "WHERE messages.chatid = "+chatid+";";
            set = stmt.executeQuery(sql);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace(System.err);
        }

        return set;

    }
}
