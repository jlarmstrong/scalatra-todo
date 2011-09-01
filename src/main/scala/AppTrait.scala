import org.scalatra._
import java.net.URL
import scalate.ScalateSupport

import net.liftweb.mongodb._
import net.liftweb.json._
import net.liftweb.mongodb.record.MongoRecord
import javax.servlet._

class AppTrait extends ScalatraServlet 
 with AuthenticationSupport
 with FlashMapSupport
 with ScalateSupport 
 {
 }