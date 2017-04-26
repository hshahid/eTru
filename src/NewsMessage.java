
import java.sql.SQLException;

import javax.annotation.Nonnull;

import org.h2.jdbc.JdbcConnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

/**
 * @author jfoley
 */
@DatabaseTable(tableName="FakeNews")
public class NewsMessage implements Comparable<NewsMessage> {
  /**
   * This is the timestamp of when this message was added -- when the constructor gets called.
   * We assume this is close enough to when the user presses the submit button for our uses.
   *
   * It's a long because it's the number of milliseconds since 1960... how computers tell time.
   */

  @DatabaseField(columnName = "time")
  long timeStamp;
  public String getMessageURL() {
	return messageURL;
}

public void setMessageURL(String messageURL) {
	this.messageURL = messageURL;
}

public String getMessageHeadline() {
	return messageHeadline;
}

public void setMessageHeadline(String messageHeadline) {
	this.messageHeadline = messageHeadline;
}

/** The text the user typed in. */
  @DatabaseField(columnName = "url")
  String messageURL;
  @DatabaseField(columnName = "id",id=true)
  String messageHeadline;

  
  public NewsMessage() {
	  
  }

  /**
   * Create a message and init its time stamp.
   * @param text the text of the message.
   */
  public NewsMessage(String URL, String Headline) {
    messageURL = URL;
    messageHeadline = Headline;
    timeStamp = System.currentTimeMillis();
  }

  /**
   * Rather than give a PrintWriter here, we'll use a StringBuilder, so we can quickly build up a string from all of the messages at once. I mostly did this a different way just to show it.
   * @param output a stringbuilder object, to which we'll add our HTML representation.
   */
  public void appendHTML(StringBuilder output) {
    output
        .append("<div class=\"message\">")
        .append("<span class=\"datetime\">").append(Util.dateToEST(timeStamp)).append("</span>")
        //.append(messageURL)
        .append("<span class = \"newsHeadline\"> <a href=\"" + messageURL + "\">" + messageHeadline + "</a> </span>")
        .append("</div>");
  }

  /**
   * Sort newer messages to top by default. Maybe someday we'll sort in other ways.
   *
   * @param o the other message to compare to.
   * @return comparator of (this, o).
   */
  @Override
  public int compareTo(@Nonnull NewsMessage o) {
    return -Long.compare(timeStamp, o.timeStamp);
  }
}
