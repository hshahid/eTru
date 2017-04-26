
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author esnyde & hshah
 */
public class NewsServer extends AbstractHandler {
	String metaURL;
	Server jettyServer;
	View view;

	Vector<NewsMessage> messageList = new Vector<>();
	private JdbcConnectionSource db;
	private Dao<NewsMessage, String> fakeNews;

	public NewsServer(String baseURL, int port) throws IOException, SQLException {
		view = new View();
		this.metaURL = "<base href=\"" + baseURL + "\">";
		jettyServer = new Server(port);
		
		// constructor of model
		this.db = new JdbcConnectionSource("jdbc:h2:./fake-news");
		TableUtils.createTableIfNotExists(db, NewsMessage.class);

		// member of the model
		this.fakeNews = DaoManager.createDao(db, NewsMessage.class);

		// We create a ContextHandler, since it will catch requests for us under
		// a specific path.
		// This is so that we can delegate to Jetty's default ResourceHandler to
		// servse static files, e.g. CSS & images.
		ContextHandler staticCtx = new ContextHandler();
		staticCtx.setContextPath("/static");
		ResourceHandler resources = new ResourceHandler();
		resources.setBaseResource(Resource.newResource("static/"));
		staticCtx.setHandler(resources); 

		// This context handler just points to the "handle" method of this
		// class.
		ContextHandler defaultCtx = new ContextHandler();
		defaultCtx.setContextPath("/");
		defaultCtx.setHandler(this);

		// Tell Jetty to use these handlers in the following order:
		ContextHandlerCollection collection = new ContextHandlerCollection();
		collection.addHandler(staticCtx);
		collection.addHandler(defaultCtx);
		jettyServer.setHandler(collection);
	}

	/**
	 * Once everything is set up in the constructor, actually start the server
	 * here:
	 * 
	 * @throws Exception
	 *             if something goes wrong.
	 */
	public void run() throws Exception {
		jettyServer.start();
		jettyServer.join(); // wait for it to finish here! We're using threads
							// behind the scenes; so this keeps the main thread
							// around until something can happen!
	}

	/**
	 * The main callback from Jetty.
	 * 
	 * @param resource
	 *            what is the user asking for from the server?
	 * @param jettyReq
	 *            the same object as the next argument, req, just cast to a
	 *            jetty-specific class (we don't need it).
	 * @param req
	 *            http request object -- has information from the user.
	 * @param resp
	 *            http response object -- where we respond to the user.
	 * @throws IOException
	 *             -- If the user hangs up on us while we're writing back or
	 *             gave us a half-request.
	 * @throws ServletException
	 *             -- If we ask for something that's not there, this might
	 *             happen.
	 */
	@Override
	public void handle(String resource, Request jettyReq, HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		// System.out.println(jettyReq);

		String method = req.getMethod();
		String path = req.getPathInfo();
		if ("GET".equals(method) && "/submit".equals(path)) {
			handleForm(req, resp);
			return;
		}
		
		// pull from the database by id
		//NewsMessage forId = fakeNews.queryForId(headline);

		try (PrintWriter html = resp.getWriter()) {
			// System.out.println(metaURL);
			view.printNewsPageStart(html, "SnS News", metaURL);

			// Print the form at the top of the page
			view.printNewsForm(html);

			// Print all of our messages
			html.println("<div class=\"messages\">");
			html.println("<div class=\"latest\"> HOTT NEWS </div>");

			
			// get a copy to sort:
			List<NewsMessage> messages;
			try {
				messages = fakeNews.queryForAll();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			Collections.sort(messages);
			Collections.reverse(messages);

			StringBuilder messageHTML = new StringBuilder();
			
			
			if(messages.size() < 10) {
				for (NewsMessage newsMessage : messages) {
					newsMessage.appendHTML(messageHTML);
				}
			} else {
				
				for (int i = 0; i < 10; i++) {
					messages.get(i).appendHTML(messageHTML);
				}
			}
			
			
			html.println(messageHTML);
			html.println("</div>");

			// when we have a big page,
			if (messages.size() > 25) {
				// Print the submission form again at the bottom of the page
				view.printNewsForm(html);
			}
			 String uri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getRequestURI() + "?" + req.getQueryString();
			view.printNewsPageEnd(html, uri );
		}
	}

	/**
   * When a user submits (enter key) or pressed the "Write!" button, we'll get their request in here. This is called explicitly from handle, above.
   * @param req -- we'll grab the form parameters from here.
   * @param resp -- where to write their "success" page.
   * @throws IOException again, real life happens.
   */
  private void handleForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Map<String, String[]> parameterMap = req.getParameterMap();

    // if for some reason, we have multiple "message" fields in our form, just put a space between them, see Util.join.
    // Note that message comes from the name="message" parameter in our <input> elements on our form.
    String headline = Util.join(parameterMap.get("headline"));
    String headlineParam = req.getParameter("headline");

    System.out.println("headline: " + headline);
    if(headlineParam.equals("")) {
    	System.out.println("caught");
    	
    	 try (PrintWriter html = resp.getWriter()) {
    		 view.displayInvalidPage(html, metaURL, headline, "");	
    	 }
    	
    }
    
    else if ( headline != null || headline != "" || headline != " ") {
    	System.out.println("We're going in");
      String uri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getRequestURI() + "?" + req.getQueryString();
      // Good, got new message from form.
      resp.setStatus(HttpServletResponse.SC_ACCEPTED);
      
      
      messageList.add(new NewsMessage(uri, headline));
      System.out.println(headline);
      // Respond!
      try (PrintWriter html = resp.getWriter()) {
    	  //String uri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getRequestURI() + "?" + req.getQueryString();
    	  
    	System.out.println("URL:" + uri);
    	
    	
    	 NewsMessage newHeadline = new NewsMessage();
         newHeadline.setMessageHeadline(headline);
         newHeadline.setMessageURL(uri);
         
         try {
			this.fakeNews.create(newHeadline);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
        view.displayFakeNewsPage(html, metaURL, headline, uri);
      } catch (IOException ignored) {
        // Don't consider a browser that stops listening to us after submitting a form to be an error.
      }

      return;
    }

    // user submitted something weird.
    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad user.");
  }
}
