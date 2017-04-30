import java.io.PrintWriter;

public class View {
	
	
	public View () {
		
	}
	
	  public String getStaticURL(String resource) {
		    return "static/"+resource;
	  }
	
	/**
	   * Made this a function so that we can have the submit form at the top & bottom of the page.
	   * <a href="http://www.w3schools.com/html/html_forms.asp">Tutorial about Forms</a>
	   * @param output where to write our HTML to
	   */
	  public void printNewsForm(PrintWriter output) {
		output.println("<div class=\"container\">");
		output.println("<script src='/static/form.js'></script>");
	    output.println("<div class=\"form\">");
	    output.println("  <form id='search-form', class=\"search-container\" action=\"submit\" method=\"GET\">");
	    output.println("     <input type=\"text\" id=\"search-bar\" placeholder=\"Find SnS News\" name=\"headline\" />");
	    output.println("     <a href=\"javascript:submitForm('search-form')\"><img class=\"search-icon\" src=\"http://www.endlessicons.com/wp-content/uploads/2012/12/search-icon.png\"> </a>");
	    //output.println("     <a href=\"#\"> <img class=\"search-icon\" src=\"http://www.endlessicons.com/wp-content/uploads/2012/12/search-icon.png\"></a>");
	    output.println("  </form>");
	    output.println("</div>");
	    output.println("</div>");

	  }

	  /**
	   * HTML top boilerplate; put in a function so that I can use it for all the pages I come up with.
	   * @param html where to write to; get this from the HTTP response.
	   * @param title the title of the page, since that goes in the header.
	   */
	  public void printNewsPageStart(PrintWriter html, String title, String metaURL) {
	    html.println("<!DOCTYPE html>"); // HTML5
	    html.println("<html>");
	    html.println("  <head>");
	    html.println("    <title>"+title+"</title>");
	    html.println("    "+metaURL);
	    html.println("    <link type=\"text/css\" rel=\"stylesheet\" href=\""+getStaticURL("news.css")+"\">");
	    html.println("    <meta name=\"description\" content=\"Your Freshest Source on the World’s Latest News\"/>");
	    html.println("    <meta property=\"image\" content=\"../static/logo.jpg\"/>");
	    html.println("  </head>");
	    html.println("  <body>");
	    html.println("<div class=\"bar\">");
	    html.println("  <p class=\"barText\">SnSNews  SnSBusiness   SnSSports  SnSHealth</p>");
        html.println("</div>");
	    html.println("  <a href =\"\"> <h1 class=\"logo\">SNS News</h1> </a> ");
	  }
	  /**
	   * HTML bottom boilerplate; close all the tags we open in printNewsPageStart.
	   * @param html where to write to; get this from the HTTP response.
	   */
	  public void printNewsPageEnd(PrintWriter html, String url) {
		  
		html.println("<div class=\"share\">");
		  
		html.println("<div id=\"fb-root\"> </div>");
		html.println("<script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0]; if (d.getElementById(id)) return; js = d.createElement(s); js.id = id; "
				+ "js.src =\"//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.9\"; fjs.parentNode.insertBefore(js, fjs); }(document, 'script', 'facebook-jssdk'));</script>");
		html.println("<div class=\"fb-share-button\" <div class=\"fb-share-button\" data-href=\"" + url + "http://www.your-domain.com/your-page.html\" data-layout=\"button_count\"> </div>");  
		  
		html.println("<a href=\"https://twitter.com/intent/tweet?button_hashtag=SnSNews\" class=\"twitter-hashtag-button\" data-show-count=\"false\">Tweet #SnSNews</a><script async src=\"//platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>");
		
		
		html.println("</div>");
		
	    html.println("  </body>");
	    html.println("</html>");
	  }
	  
	  public void displayFakeNewsPage(PrintWriter html, String metaURL, String headline, String URL) {
		printNewsPageStart(html, "SnS News", metaURL);
		html.println("    <div class=\"latest\">"+headline+"</div>");

        // Thank you, link.
        //html.println("<div class=\"body\">");
        html.println("<div class=\"thanks\">");
        html.println("<title>"+headline+"</title>");
        html.println("</div>");
        
        html.println("<div class=\"infoContainer\">");
        html.println("<div class=\"infotext\">");
        
        html.println("<p> This is the real fake new. Educate yourself through some credible news sources. Here are some listed below: </p>");
        html.println("<p> </p>");
        //cnn
        //bbc
        //nytimes
        //abcnews
        //
        html.println("</div>");
        
        html.println("<div class=\"video\">");
        html.println("<iframe width=\"640\" height=\"360\" src=\"https://www.youtube.com/embed/dQw4w9WgXcQ?autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>");
        html.println("</div>");
        
        html.println("</div>");
        
        html.println("<div class=\"link\">");
        html.println("<a href=\"" + URL + "\">Copy your URL here</a>.");
        html.println("</div>");
       // html.println("</div>");

        printNewsPageEnd(html, URL);
	  }
	  
	  public void displayInvalidPage(PrintWriter html, String metaURL, String headline, String URL) {
		  printNewsPageStart(html, "SnS News", metaURL);
			html.println("    <div class=\"latest\">"+"This is invalid. Your life is invalid. Bad!"+"</div>");

	        // Thank you, link.
	        //html.println("<div class=\"body\">");
	        html.println("<div class=\"thanks\">");
	        html.println("<title>"+"Enter a valid headline."+"</title>");
	        html.println("</div>");
	        
	        html.println("Enter a valid headline.");
	        printNewsForm(html);
		  
		  printNewsPageEnd(html, URL);
	  }
	  
	
}
