import java.io.BufferedReader;
import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Server {
	public static int port = 9999;
	public static String ip = "127.0.0.1";
	
	static final String databasePrefix ="cs366-2231_carlsonca20";
	static final String netID ="carlsonca20"; 
	static final String hostName ="washington.uww.edu"; 
    static final String databaseURL ="jdbc:mariadb://"+hostName+"/"+databasePrefix;
	static final String password="cc7893";
    static BufferedReader read;
    static PrintWriter output;
    public static String username;
    public static String userPassword;
     
	public static void main(String args[]) throws IOException {


	     try {

	    	 //connection(); //connect to MariaDB
	    	 
	    	 SocketAddress endPoint = new InetSocketAddress(ip, port);
			 ServerSocket listener = new ServerSocket();
			 listener.bind(endPoint);
			 

			 Socket sock = listener.accept();

			 
			 read = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			 output = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

				
			 String hello = "Hello";
			 output.println(hello); // initial server hello to establish connection
			 output.flush();
			 boolean open = true;
			 String username;
			 			 


	         while (true)
	         {
	        	 String userInput = read.readLine();
	        	 
				 String[] result = userInput.split(":");  //split user input by : so we can send a chunk of data and seperate it accordingly
				 for(int i = 0; i < result.length; i++)
					 System.out.println(result[i]); // prints user input for debugging
				 
	        	 if(result[0].equals("Register"))
	        	 {

	        		 output.println(register(result));
	        		 output.flush();

	        	 }
	        	 else if(result[0].equals("Login"))
	        	 {
	        		 
	        		 output.println(login(result));
	        		 output.flush();

	        		
	        	 }
	        	 else if(result[0].equals("SearchSong"))
	        	 {
	        		output.println(search(result));
	        		output.flush();

	        	 }
	        	 else if(result[0].equals("SearchFriend"))
	        	 {
	        		 output.println(searchfriends(result));
	        		 output.flush();
	        	 }
	        	 else if(result[0].equals("Play"))
	        	 {
	        		 String path = search(result); //edit this and parse the path out of the full search record
	        		 //play(path);
	        		 output.flush();
	        		 
	        	 }
	        	 else if(result[0].equals("addPlaylist"))
	        	 {
	        		 output.println(addPlaylist(result));
	        		 output.flush();

	        	 }
	        	 else if(result[0].equals("Playlist"))
	        	 {
	        		 output.println(showPlaylist(result));
	        		 output.flush();

	        	 }
	        	 else if(result[0].equals("Settings"))
	        	 {
	        		 
	        		 output.println(settings(result));
	        		 output.flush();

	        	 }
	        	 else if(result[0].equals("Exit"))
	        	 {
	        		 sock.close();
	        		 open = false;
	        	 }
 
	         }
	         
	     }
	     catch(Exception e)
		 {
			System.out.println(e);
		 }
	    }
	
	
	
	public static String register(String[] input)
	{
		String response = "";
		String querystring = "SELECT * FROM UserLogin WHERE uname = '"+input[1]+"';";
		System.out.println(queryUser(querystring));
		
		 if(queryUser(querystring).equals(""))
		 {
			 String insert = "INSERT INTO UserLogin (uname, pword) VALUES ('"+input[1]+"', '"+input[2]+"');";
			 queryUser(insert);
			 response = "GoodRegister";
		 }
		 return response;
		 
		 
	}
	
	public static String login(String[] input)
	{
		String querystring = "SELECT * FROM UserLogin WHERE uname = '"+input[1]+"';";
		String result = queryUser(querystring);
		String response = "";
		 if(result.contains(input[1]) && result.contains(input[2])) //make sure 1 and 2 element are username and password (hash)
		 {
			 response = "GoodLogin";

		 }
		 return response;
	}
	
	public static String search(String[] input)
	{
		String querystring = "SELECT * FROM Songs WHERE song_title = '"+input[1]+"';";
		String result = querySong(querystring);
		String response;
		if(result.equals(""))
		{
			response = "searchfailed";
		}
		else
			response = "FoundSong "+result;
		
		return response;
		
	}
	
	
	public static String searchfriends(String[] input)
	{
		String querystring = "SELECT * FROM Playlist WHERE uname = '"+input[1]+"';";
		String result = queryPlaylist(querystring);
		String response;
		if(result.equals(" "))
		{
			response = "searchfailed";
		}
		else
			response = "Friend's Playlist: "+result;
		
		return response;
		
	}
	
	public static String showPlaylist(String[] input)
	{
		String querystring = "SELECT * FROM Playlist WHERE uname = '"+input[1]+"';";
		String result = queryPlaylist(querystring);
		String response;
		if(result.equals(""))
		{
			response = "searchfailed";
		}
		else
			response = "Personal Playlist: "+result;
		
		return response;
	}
	
	public static String addPlaylist(String[] input)
	{
		String response = "";
		String querystring = "SELECT * FROM Playlist WHERE uname = '"+input[1]+"' AND song_title = '"+input[2]+"';";
		 if(queryPlaylist(querystring).equals("")) // checks if song is already in playlist
		 {
			 querystring = "INSERT INTO Playlist (uname, song_title) VALUES ('"+input[1]+"', '"+input[2]+"')"; //1 is uname 2 is song name
			 queryPlaylist(querystring);
			 response = "Successfully added Song";
		 }
		 return response;
	}
	
	public static String settings(String[] input)
	{
		String querystring = "SELECT * FROM UserLogin WHERE uname = '"+input[1]+"';";
		String result = queryUser(querystring);
		
		return result; // no need to check for null or missing because user is already logged in to use this method
		
	}
	 
	public static String querySong(String sqlQuery) {   
		String result = "";
		Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
		try {
			connection = DriverManager.getConnection(databaseURL, netID, password);
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sqlQuery);

    		 StringBuilder sb = new StringBuilder();
             while (resultSet.next()) {
                 sb.append(resultSet.getString("song_title")).append("\t")
                   .append(resultSet.getString("artist")).append("\t")
                   .append(resultSet.getString("link"));
             }
             result = sb.toString();
             

         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             try {
                 if (resultSet != null) resultSet.close();
                 if (statement != null) statement.close();
                 if (connection != null) connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
	    	
	    	return result; // returns sql query response
	    }
	
	public static String queryUser(String sqlQuery) {   // This method will execute a query to the console as of right now 
		String result = "";
		Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
		try {
			connection = DriverManager.getConnection(databaseURL, netID, password);
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sqlQuery);
    		System.out.println(result);

    		 StringBuilder sb = new StringBuilder();
             while (resultSet.next()) {
                 sb.append(resultSet.getString("uname")).append("\t")
                   .append(resultSet.getString("pword")).append("\n");
             }
             result = sb.toString();

         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             try {
                 if (resultSet != null) resultSet.close();
                 if (statement != null) statement.close();
                 if (connection != null) connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
	    	
	    	return result; // returns sql query response
	    }
	
	public static String queryPlaylist(String sqlQuery) {  

		String result = "";
		Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
		try {
			connection = DriverManager.getConnection(databaseURL, netID, password);
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sqlQuery);
    		System.out.println(result);

    		 StringBuilder sb = new StringBuilder();
             while (resultSet.next()) {
                 sb.append(resultSet.getString("song_title")).append("\t");
             }
             result = sb.toString();
             
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             try {
                 if (resultSet != null) resultSet.close();
                 if (statement != null) statement.close();
                 if (connection != null) connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
	    	
	    	return result; // returns sql query response
	    }
	 
    
}


  