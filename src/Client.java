import java.net.*;
import java.io.*;
import java.util.*;


public class Client {

 String primaryIP = "128.237.178.17";
 String secondaryIP = "128.237.177.71";
 String dataInString;
 Scanner user_input = new Scanner(System.in); // user input
 ObjectInputStream Sinput;        // to read the socket
 ObjectOutputStream Soutput;    // towrite on the socket
 Socket socket;
 int flag = 1;
 boolean primary = true; 

 // Constructor connection receiving a socket number
 Client(int port){
       try {
           System.out.println("Trying to connect to primary server..");
           // primary server
           socket = new Socket(primaryIP, port);
       }
       catch(Exception e) {
           System.out.println("Error connecting to primary server " + e + "\n");
           System.out.println("Trying to connect to secondary server..");
           try {
               //secondary ip address
               socket = new Socket(secondaryIP, port);
               primary = false;
           } catch(Exception f) {
               System.out.println("Error connecting to secondary server" + e + "\n");
               return;
           }
       }


       System.out.println("Connection accepted " +
               socket.getInetAddress() + ":" +
               socket.getPort() + "\n");
       
       try
       {
           Sinput  = new ObjectInputStream(socket.getInputStream());
           Soutput = new ObjectOutputStream(socket.getOutputStream());
       }
       catch (IOException e) {
           System.out.println("Exception creating new Input/output Streams: " + e);
           return;
       }



       heartbeat h = new heartbeat(port); 
       h.start();
       while(true) {
	       while(flag==1) {
	           System.out.print("Type in the book name : ");
	
	           if (user_input.hasNextLine()) {
	               // my connection is established
	
	               // send the question (String) to the server
	               String bookname = new String ();
	               bookname = user_input.nextLine();
	               System.out.println("Client2 sending \"" + bookname + "\" to server\n");
	
	               try {
	                   Soutput.writeObject(bookname);
	                   Soutput.flush();
	               }
	               catch(IOException e) { 
	            	   flag = 0;
	               }
	               // read back the answer from the server
	               String response;
	               //long t1 = 0;
	               try {
	                   // socket.setSoTimeout(2000);
	                  //  t1 = System.currentTimeMillis();
	                    response = (String) Sinput.readObject();
	                    String[] tokens = response.split("/");
	                    dataInString = tokens[1];
	                    System.out.println("Read back from server: " + response);
	                }
	               catch(Exception e) {
	                //   long t2 = System.currentTimeMillis();
	                //   System.out.println(t2-t1 + "Problem reading back from server: " + e);
	                 //  break;
	            	   flag = 0;
	               }
	           }
	       }
	       while(flag == 0) {
	    	   try {
				Thread.sleep(1000);
			   } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
	       }
	       
       }
   }
 
   class heartbeat extends Thread{
       int port;
       
	   heartbeat(int port) {
           this.port = port;
       }
	   public void run() {
		   String heartbeatstr;
		   heartbeatstr="Are you alive???";
		   while(true)
		   {
			   
		       try {
		    	   Soutput.writeObject(heartbeatstr + " from Client2 with checkpoint /" + dataInString);
		           Soutput.flush();		       
		       }
		       catch(IOException e) {
		           System.out.println("Error writing to the socket: " + e);
		           flag = 0;
		           
		           if (primary) {
	                   
	                   System.out.println("Error connecting to primary server " + e + "\n");
	                   System.out.println("Trying to connect to secondary server..");
	                   try {
	                       //secondary ip address
	                       socket = new Socket(secondaryIP, port);
	                       primary = false;
	                   } catch(Exception f) {
	                       System.out.println("Error connecting to secondary server" + e + "\n");
	                       return;
	                   
	                   }
                   }
                   //if it was connected to secondary
                   else {
                	   System.out.println("Error connecting to secondary server " + e + "\n");
	                   System.out.println("Trying to connect to recovered primary server..");
	                   try {
	                       //primary ip address
	                       socket = new Socket(primaryIP, port);
	                       primary = true;
	                   } catch(Exception f) {
	                       System.out.println("Error connecting to primary server" + e + "\n");
	                       return;
	                   
	                   }
                	   
                   }System.out.println("Connection accepted " +
                           socket.getInetAddress() + ":" +
                           socket.getPort() + "\n");
                   
                   try
                   {
                       Sinput  = new ObjectInputStream(socket.getInputStream());
                       Soutput = new ObjectOutputStream(socket.getOutputStream());
                   }
                   catch (IOException e1) {
                       System.out.println("Exception creating new Input/output Streams: " + e);
                       return;
                   }
                   
                   flag = 1;
                   continue;
		       }
		       
		       try {
		           socket.setSoTimeout(10000);
		           String response = (String) Sinput.readObject();
		           System.out.println("Read back from server: " + response);
		       }
		       catch(Exception e) {
		    	   System.out.println("Problem reading back from server: " + e);
		           flag = 0;
		           
		    	   if (primary) {
	                   
	                   System.out.println("Error connecting to primary server " + e + "\n");
	                   System.out.println("Trying to connect to secondary server..");
	                   try {
	                       //secondary ip address
	                       socket = new Socket(secondaryIP, port);
	                       primary = false;
	                   } catch(Exception f) {
	                       System.out.println("Error connecting to secondary server" + e + "\n");
	                       return;
	                   
	                   }
                   }
                   //if it was connected to secondary
                   else {
                	   System.out.println("Error connecting to secondary server " + e + "\n");
	                   System.out.println("Trying to connect to recovered primary server..");
	                   try {
	                       //primary ip address
	                       socket = new Socket(primaryIP, port);
	                       primary = true;
	                   } catch(Exception f) {
	                       System.out.println("Error connecting to primary server" + e + "\n");
	                       return;
	                   
	                   }
                	   
                   }System.out.println("Connection accepted " +
                           socket.getInetAddress() + ":" +
                           socket.getPort() + "\n");
                   
                   try
                   {
                       Sinput  = new ObjectInputStream(socket.getInputStream());
                       Soutput = new ObjectOutputStream(socket.getOutputStream());
                   }
                   catch (IOException e1) {
                       System.out.println("Exception creating new Input/output Streams: " + e);
                       return;
                   }
                   
                   flag = 1;
		       }
		       
		       try {
				Thread.sleep(3000);
				} catch (InterruptedException e) {}
		   }	   
	   }
   }

   public static void main(String[] arg) {
       new Client(1500);
   }
  }