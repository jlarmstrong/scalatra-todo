package todo

import javax.servlet._

import com.mongodb.DBAddress
import net.liftweb.mongodb._

/**
 * Created by IntelliJ IDEA.
 * User: jarmstrong
 * Date: 12/16/11
 * Time: 8:48 AM
 * To change this template use File | Settings | File Templates.
 */

class ServletListener extends ServletContextListener {

  var context: ServletContext = null

  /*This method is invoked when the Web Application has been removed
  and is no longer able to accept requests
  */

  def contextDestroyed(event: ServletContextEvent)
  {
    //Output a simple message to the server's console
    System.out.println("The Simple Web App. Has Been Removed");
    this.context = null;
    MongoDB.close
  }


  def contextInitialized( event:ServletContextEvent)
  {
    this.context = event.getServletContext();

    println("context started")
    
    // Use this for single instance mongo
    MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("127.0.0.1", 27017), "scalatra-todo"))
    
    println("mongo connected")

  }

}
