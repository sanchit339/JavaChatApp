import java.net.*;
import java.io.*;

public class Server {
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // constructor
    public Server(){
        try {
            server = new ServerSocket(3000);
            System.out.println("Server is ready to accept the connection");
            System.out.println("Waiting...");
            socket = server.accept(); // assigning the socket
            // we need an input stream to accept the request
            // getInput byte -> inputStream char -> bufferReader
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Stream for the output
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startReading(){
        Runnable r1 = () -> {
            System.out.println("Reader Started...");
            try {
                while (true && !socket.isClosed()){
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client Terminated chat...");
                        break;
                    }
                    System.out.println("Client :" + msg);
                }
            } catch (Exception e) {
//                throw new RuntimeException(e);
                System.out.println("Connection is Closed !!");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        Runnable r2 = () -> {
            System.out.println("Writer Started...");
            try{
                while (true){
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String content = br.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e){
//                e.printStackTrace();
                System.out.println("Connection is Closed !!");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server is starting...");
        new Server();
    }
}
