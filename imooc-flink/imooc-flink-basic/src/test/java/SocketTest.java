import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Galaxy
 * @since 2022/2/22 0:51
 */
public class SocketTest {
  public static void main(String[] args) throws IOException, InterruptedException {
    ServerSocket serverSocket = new ServerSocket(9527);
    Socket clientSocket = serverSocket.accept();
    PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
    while (true) {
      Thread.sleep(3000L);
      printWriter.println("pk,red,yellow,try,try,try");
    }
  }
}
