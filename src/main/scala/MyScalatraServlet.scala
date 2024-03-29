package todo


import org.scalatra._
import java.net.URL
import todo.models._

import _root_.akka.dispatch._
import org.scalatra.akka.AkkaSupport

class MyScalatraServlet extends AppTrait
 with TodosServlet
 {

  /*
  override def initialize(config: ServletConfig): Unit = {
    MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("127.0.0.1", 27017), "scalatra-todo"))
    
    super.initialize(config)
  }   */
  
  before() {
    if (!isAuthenticated) {
      scentry.authenticate('RememberMe)
    }
  }

  asyncGet("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="/login">hello to Scalatra</a>.
        Test Auth <a href="register">Register</a>.
      </body>
    </html>
  }

  asyncGet("/login") {
    redirectIfAuthenticated
    
    contentType = "text/html"

    <html>
    <body>
      <h1>Login</h1>
      <div style="color: #F00;">{flash.getOrElse("error", "")}</div>
      <form method="post" action="/login">
      <div><label>User:</label><input type="text" name="userName" value="test@test.com" /></div>
      <div><label>Password:</label><input type="password" name="password" /></div>
      <div><label>Remember Me:</label><input type="checkbox" name="rememberMe" value="true" /></div>
      <div><input type="submit" value="submit" /></div>
      </form>
    </body>
    </html>
  }

  asyncPost("/login") {
    scentry.authenticate('UserPassword)
    
    if (isAuthenticated) {
      redirect("/loggedin")
    }else{
      flash += ("error" -> "login failed")
      redirect("/login")
    }
  }

  asyncGet("/loggedin") {
    redirectIfNotAuthenticated
    
    contentType = "text/html"
    
    <html>
      <body>
      <h1>Hello, world!</h1>
      Welcome {user.username} you are logged in <a href="/todo">todos</a>. <a href="/logout">Logout</a>
      </body>
    </html>
  }

  asyncGet("/register") {
    contentType = "text/html"

    <html>
    <body>
      <h1>Register</h1>
      <form method="post" action="/register">
      <div><label>User</label><input name="userName" value="test@test.com" /></div>
      <div><label>password</label><input name="password" /></div>
      <div><input type="submit" value="submit" /></div>
      </form>
    </body>
    </html>	
  }

  asyncPost("/register") {
    val u = User.createRecord
      .username(params("userName"))
      .password(params("password"))
      
    //save
    u.save
    
    redirect("/login")
  }

  asyncGet("/logout/?") {
    logOut
    
    redirect("/logout_step")
  }
  
  // this step is used to verify the cookies are erased
  get("/logout_step/?") {
    redirect("/login")
  }

  notFound {


      resourceNotFound()

      // If no route matches, then try to render a Scaml template
      val templateBase = requestPath match {
        case s if s.endsWith("/") => s + "index"
        case s => s
      }
      val templatePath = "/WEB-INF/scalate/views/" + templateBase + ".ssp"

      servletContext.getResource(templatePath) match {
        case url: URL =>
          contentType = "text/html"
          templateEngine.layout(templatePath)
        case _ =>
          response.sendError(404)
      }
    }
}