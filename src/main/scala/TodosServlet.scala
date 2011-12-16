package todo

import todo.models._

trait TodosServlet extends AppTrait {
  
  asyncGet("/todo/?") {
    layoutTemplate("/todo.ssp")
  }
  
}