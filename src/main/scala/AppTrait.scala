package todo

import org.scalatra._
import java.net.URL
import scalate.ScalateSupport

import net.liftweb.mongodb._
import net.liftweb.json._
import net.liftweb.mongodb.record.MongoRecord
import javax.servlet._

import _root_.akka.dispatch._
import org.scalatra.akka.AkkaSupport

class AppTrait extends ScalatraServlet
  with AkkaSupport
 with AuthenticationSupport
 with FlashMapSupport
 with ScalateSupport 
 {
 }