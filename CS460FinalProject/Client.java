
import java.io.*;
import javax.sound.sampled.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;


public class Client {

	private static String ip = "127.0.0.1";
	private static int port = 9999;
    Socket socket;
    BufferedReader read;
    PrintWriter output;
    Scanner input = new Scanner(System.in);
    BufferedReader stdin;
    String username;

    public void startClient() throws UnknownHostException, IOException{
        //Create socket connection
    	try {
        Socket client = new Socket(ip, port);

        //create printwriter for sending data to server and bufferedreader for taking in data from socket
        read = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

        		

    String userInput = read.readLine(); //initial read to see server hello
    if(userInput.equals("Hello")) //server hello
    {
    	System.out.println("------------------------------");
        System.out.println("Register (1) or Login (2)");
        if(input.nextLine().equals("1"))
        {
        	Register();
        }
        else
        	Login();
        
 }
        }catch (Exception e)
    	{
    		System.out.println(e);
    	}
    	
    }
    
    
    
    public void play(String musicFile) {
		
	}
    
    
    public void Settings()
    {
    	output.println("Settings:"+username);
    	output.flush();
    	try {
    	String settings = read.readLine();
    	System.out.println("------------------------------");
    	System.out.println("Username "+"\t"+"Password (Hash)");
    	System.out.println(settings); //debug with this and formal settings
    	
    	System.out.println("Menu (any key) ");
    	String choice = input.nextLine();
    	if(choice.equals("1"))
    		Menu();
    	else
    		Menu();
    	
    	} catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void Playlist()
    {
    	output.println("Playlist:"+username);
    	output.flush();
    	System.out.println("------------------------------");
    	try { 
    	String playlist = read.readLine();
    	String[] playlistarr = playlist.split("\t");
    	//for(int i = 1; i < playlistarr.length; i++)
    	//System.out.print(playlistarr[i]+"\t");
    	
    	System.out.println(playlist);
    	System.out.println("Select which number song to play or -1 for menu");
    	int choice = input.nextInt();
    	if(choice == -1)
    	{
    		Menu();
    	}
    	else
    	{
    		String playsong = playlistarr[choice+1]; //make array of songs so we can index them 
    		play(playsong);
    	}
    	} 
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	
    }
    
    public void SearchSong()
    {
    	System.out.println("Search for a song to play (case sensitive)");
    	String songsearch = input.nextLine();
    	output.println("SearchSong:"+songsearch);
    	output.flush();
    	
    	try { 

    	if(read.readLine().contains("FoundSong"))
    	{
    		System.out.println("------------------------------");
    		System.out.println("Play (1) "+songsearch);
    		System.out.println("Add song to personal playlist (2)");
    		System.out.println("Search for a different song (3) ");
    		System.out.println("Go to the main menu (4) ");
    		System.out.println("------------------------------");
    		String choice = input.nextLine();
    		if(choice.equals("1"))
    			play(songsearch);
    		else if(choice.equals("2"))
    		{
    			output.println("addPlaylist:"+username+":"+songsearch);//add to playlist basically same thing is liked songs
    			output.flush();
    			String resp = read.readLine();
    			System.out.println(resp);
    			Menu();
    		}
    		else if(choice.equals("3"))
    			SearchSong();
    		else
    			Menu();
    			
    	}
    	else
    	{
    		System.out.println("Song not found!");
    		System.out.println("Search for a different song (1)");
    		System.out.println("Menu (any key)");
    		String choice = input.nextLine();
    		if(choice.equals("1"))
    			SearchSong();
    		else
    			Menu();
    			
    	}
    	}
    	catch (IOException e) {
    	e.printStackTrace();
    	}
    	
    	
    }
    
    public void SearchFriend()
    {
    	System.out.println("Search for a Friends username to view their playlist (case sensitive)");
    	String friendsearch = input.nextLine();
    	output.println("SearchFriend:"+friendsearch);
    	output.flush();
    	
    	try { 
    		String friend = read.readLine();
    	if(friend.contains("Friend's Playlist"))
    	{
    		System.out.println(friend);
    		
    		
    		System.out.println("Search another friend (1) or any key for main menu");
    		String choice = input.nextLine();
    		if(choice.equals("1"))
    			SearchFriend();
    		else
    			Menu();
    			
    	}
    	else
    	{
    		System.out.println("User does not exist!");
    		System.out.println("Search another friend (1) or any key for main menu");
    		String choice = input.nextLine();
    		if(choice.equals("1"))
    			SearchFriend();
    		else
    			Menu();
    	}
    	}
    	catch (IOException e) {
    	e.printStackTrace();
    	}
    	
    	
    }


    public void Login()
    {
    	System.out.println("------------------------------");
    	System.out.println("LOGIN");
    	System.out.println("Enter Username: ");
    	username = input.nextLine();
    	System.out.println("Enter Password");
    	String password = getMd5(input.nextLine()); //md5 this then send to server
    	System.out.println("------------------------------");
    	output.println("Login:"+username+":"+password);
    	output.flush();

    	
    	try {
    		
    	String response = read.readLine();
    	if(response.equals("GoodLogin"))
    	{
    		Menu();
    	}
    	else
    	{
    		System.out.println("Login Failed try again");
    		Login();
    	}
    	} catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void Menu()
    {
    	System.out.println("------------------------------");
    	System.out.println("Search a Song (1) "+"\n"+"Playlist (2) "+"\n"+"Settings (3)"+"\n"+"Search for a Friend (4)"+"\n"+"Exit (5)");
    	String choice = input.nextLine();
    	
    	if(choice.equals("1"))
    		SearchSong();
    	else if(choice.equals("2"))
    		Playlist();
    	else if(choice.equals("3"))
    		Settings();
    	else if(choice.equals("4"))
    		SearchFriend();
    	else if(choice.equals("5"))
    	{
    		output.println("Exit");
    		output.flush();
    		input.close();
    		output.close();
    		System.exit(0);
    	}
    	else
    	{
    		System.out.println("Invalid Choice choose again");
    		Menu();
    	}
    }
    
    public void Register()
    {
    	System.out.println("------------------------------");
    	System.out.println("REGISTER");
    	System.out.println("Enter Username: ");
    	String username = input.nextLine();
    	System.out.println("Enter Password");
    	String password = getMd5(input.nextLine()); //md5 this then send to server
    	System.out.println("Enter First Name: ");
    	String firstName = input.nextLine();
    	System.out.println("Enter Last Name");
    	String lastName = input.nextLine();
    	output.println("Register:"+username+":"+password+":"+firstName+":"+lastName);
    	output.flush();
    	
    	try {
    	String response = read.readLine();
    	
    	
    	if(response.equals("GoodRegister"))
    	{
    		Login();
    	}
    	else
    	{
    		System.out.println("Registration failed try again");
    		Register();
    	}
    	} catch (IOException e)
    	{
    		e.printStackTrace();
    	}
	}
    
    public static String getMd5(String input)
    {
        try {
 
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
 
            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
 
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
 
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
 
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]){
        Client client = new Client();
        try {
            client.startClient();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
