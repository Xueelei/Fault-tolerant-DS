import java.io.*;
import java.net.*;
import java.util.*;
\

public class Server {
   private ServerSocket serverSocket;
   Server(int port) {
       try{
           serverSocket = new ServerSocket(port);
           System.out.println("Server waiting for client on port " + serverSocket.getLocalPort());

           while(true) {
               Socket socket = serverSocket.accept();  // accept connection
               System.out.println("New client asked for a connection");
               TcpThread t = new TcpThread(socket);    // make a thread of it
               System.out.println("Starting a thread for a new Client");
               t.start();
           }
       }
       catch(IOException e){
           System.out.println("Exception on new ServerSocket: " + e);
       }
   }

//  you must ¡°run¡± server to have the server run as a console application
   public static void main(String[] arg) {
       // start server on port 1500
       new Server(1500);
   }

/** One instance of this thread will run for each client */
  class TcpThread extends Thread {
      // the socket where to listen/talk
      Socket socket;
      ObjectInputStream Sinput;
      ObjectOutputStream Soutput;
      TcpThread(Socket socket) {
          this.socket = socket;
      }
      public void run() {
          /* Creating both Data Stream */
          System.out.println("Thread trying to create Object Input/Output Streams");
          try
          {
              // create output
              Soutput = new ObjectOutputStream(socket.getOutputStream());
              Soutput.flush();
              Sinput  = new ObjectInputStream(socket.getInputStream());
          }
          catch (IOException e) {
              System.out.println("Exception creating new Input/output Streams: " + e);
              return;
          }

          while(true) {
              System.out.println("Thread waiting for a String from the Client");
              // read a String (which is an object)
              try {
                  String str = (String) Sinput.readObject();
                  String heartbeat = "Are you alive???";
                  System.out.println(str);

                  String result;
                  if (str.contains(heartbeat))
                      result = "im alive";
                  else
                      result = searchLibrary(str);


                  try {
                   Thread.sleep(10);
                   } catch (InterruptedException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
                   }

                  Soutput.writeObject("It is \"" + result + "\"");
                  Soutput.flush();
              }
              catch (IOException e) {
                  System.out.println("Exception reading/writing  Streams: " + e);
                  break;
              }// will surely not happen with a String
              catch (ClassNotFoundException o) {
              }
             
          }
          try {
              Soutput.close();
              Sinput.close();
          }
          catch (Exception e) {
          }
          return;
      }
  }

  @SuppressWarnings("rawtypes")
public String searchLibrary(String bookname){
      Hashtable library = new Hashtable();
      Enumeration names;
      String str;
      String result;

      library.put("Pride and Prejudice", "Yes");
      library.put("Little Fires Everywhere", "No");
      library.put("Great Expectations", "Yes");
      library.put("Jane Eyre", "Yes");
      library.put("The Kite Runner", "No");
        
      result = (String) library.get(bookname);
          
      return result;
  }
}