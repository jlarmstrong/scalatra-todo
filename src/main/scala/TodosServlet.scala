package todo

import todo.models._

import _root_.akka.dispatch._
import org.scalatra.akka.AkkaSupport

trait TodosServlet extends AppTrait {
  
  get("/todo/?") {
    Future {
    println("inside todo")

    contentType = "text/html"

    layoutTemplate("/WEB-INF/views/todo.ssp",("layout" -> "/WEB-INF/layouts/default.ssp"))

    }
  }
  
}