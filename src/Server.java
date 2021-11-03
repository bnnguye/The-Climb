import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {
    public static void createServer() throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        int playerNo = 1;

        while(true) {
            Socket s = ss.accept();
            playerNo++;
            System.out.println(String.format("Player" + playerNo + " connected!", playerNo));
            // Player player = new Player(playerNo);

            PrintWriter pr = new PrintWriter(s.getOutputStream());
            pr.println("yes");
            pr.flush();
        }
    }
}
