
import java.io.IOException;
import java.sql.SQLException;

public class Main {
  /**
   * This is the entrance point of the Writr server. It parses arguments and starts up the application.
   * @param args command line arguments -- you shouldn't have to fiddle with these; mostly so that I can run multiple copies (if I need to) of final results.
   * @throws IOException the server looks for files on disk, so this could crash!
 * @throws SQLException 
   */
  public static void main(String[] args) throws IOException, SQLException {
    int port = 1234;
    String baseURL = "/";


    if(args.length > 0) {
      // use port 1234 or the first argument specified to this program.
      port = Integer.parseInt(args[0]);
    }
    if(args.length > 1) {
      // hosting configuration: is this at the root of a webserver? default -- yes.
      baseURL = args[1];
    }

    NewsServer app = new NewsServer(baseURL, port);
    try {
      app.run();
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}
