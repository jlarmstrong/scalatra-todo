package todo

import todo.models._

import _root_.akka.dispatch._
import org.scalatra.akka.AkkaSupport

import org.slf4j.{LoggerFactory}

trait TodosServlet extends AppTrait {
  
  get("/todo/?") {
    Future {
    logger.info("inside get(todo)")

    contentType = "text/html"

    layoutTemplate("/WEB-INF/views/todo.ssp",("layout" -> "/WEB-INF/layouts/default.ssp"))

    }
  }
  
  get("/todo/list/?"){
    Future {
      logger.info("inside get(todo/list)")
      // return json
    }
  }
  
  put("/todo/?"){
    Future {

      logger.info("inside put(todo)")

    }
  }
  
}