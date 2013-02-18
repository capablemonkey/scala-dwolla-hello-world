package code.lib

import code.lib._
import net.liftweb.http.SessionVar
import java.net.URLEncoder

object getAuthenticator {
  /*
   * Retrieves credentials from environment variables and returns an instance of Authenticator
   */
  def apply:Authenticator = {
	    // retrieve keys from environ:ment variables:
	    val key:String = sys.env("DWOLLA_CLIENT_KEY")
		val secret:String = sys.env("DWOLLA_SECRET")
		// automatically detect redirect URL from base URL:
		val redirect:String = URLEncoder.encode("http://127.0.0.1:8080/balance", "UTF-8")
		return new Authenticator(key, secret, redirect)
  	}
}

object storedToken extends SessionVar[String]("Default")