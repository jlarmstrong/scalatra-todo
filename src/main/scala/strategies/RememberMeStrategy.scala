package todo.strategies

import org.scalatra.auth.ScentryStrategy
import org.scalatra.{CookieSupport, ScalatraKernel, CookieOptions, Cookie}
import net.liftweb.common._
import javax.servlet.http.{Cookie ⇒ ServletCookie}
import net.liftweb.common.{Box, Empty, Failure, Full}
import org.slf4j.{LoggerFactory}

import todo.models._

/**
 * Authentication strategy to authenticate a user from a cookie.
 */
class RememberMeStrategy(protected val app: ScalatraKernel with CookieSupport)
  extends ScentryStrategy[User]
{
  val logger = LoggerFactory.getLogger(getClass)

  val COOKIE_KEY = "rememberMe"
  private val oneWeek = 7 * 24 * 3600

  override def isValid = {
    app.cookies.get(COOKIE_KEY).isDefined
  }

  /**
   * After authentication, sets the remember-me cookie on the response.
   */
  override def afterAuthenticate(winningStrategy: Symbol, user: User) = {
    logger.info("rememberMe: afterAuth fired")
    if (winningStrategy == 'RememberMe ||
      (winningStrategy == 'UserPassword && checkbox2boolean(app.params.get("rememberMe").getOrElse("").toString))) {

      val token = user.rememberMe.value
      logger.info("rememberMe: set Cookie["+token+"]")
      app.response.addHeader("Set-Cookie",
        Cookie(COOKIE_KEY, token)(CookieOptions(secure = false, maxAge = oneWeek, httpOnly = true)).toCookieString)
    }
  }

  /**
   * Authenticates a user by validating the remember-me cookie.
   */
  def authenticate = {

    val token: String = app.cookies.get(COOKIE_KEY) match {
      case Some(v) => v.toString
      case None => ""
    }

    logger.info("rememberMe: trying for token["+token+"]")

    User.validateRememberToken(token) match {
      case None => {
        None
      }
      case Some(usr) ⇒ {
        logger.info("rememberMe: validated["+token+"]")
        Some(usr)
      }
    }

  }

  /**
   * Clears the remember-me cookie for the specified user.
   */
  override def beforeLogout(user: User) = {
    logger.info("rememberMe: beforeLogout")
    if (user != null){
      user.forgetMe
    }
    app.cookies.get(COOKIE_KEY) foreach {
      _ => app.cookies.update(COOKIE_KEY, null)
    }
  }

  /**
  * Used to easily match a checkbox value
  */
  def checkbox2boolean(s: String): Boolean = {
    s match {
      case "yes" => true
      case "y" => true
      case "1" => true
      case "true" => true
      case _ => false
    }
  }
}