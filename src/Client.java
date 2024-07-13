import java.io.*;
import java.net.Socket;

public class Client {

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    // constructor
    public Client(){
        try{

            System.out.println("Sending request to the server...");
            socket = new Socket("127.0.0.1" , 3000);
            System.out.println("Connection done...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                while (true){
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Server Terminated chat...");
                        socket.close();
                        break;
                    }
                    System.out.println("Server :" + msg);
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
                while (true && !socket.isClosed()){

                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String content = br.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
                System.out.println("Connection is Closed !!");
            } catch (Exception e){
                    e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Client is Starting...");
        new Client();
    }
}
