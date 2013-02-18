package code
package snippet

import lib._

import net.liftweb.http.CurrentReq
import net.liftweb._
import util.Helpers._
import common._
import http._
import sitemap._
import code.lib._

class AccountOperations {
  def isLoggedIn:Boolean = {
    storedToken.is != "Default"
  }
  
  def login = {
    /*
     * Step 2: extract code from GET parameter and get accessToken
     */
    if (CurrentReq.value.params contains "code") {
    // extract parameter "code" from current GET request (following redirect from Dwolla):
    val verification_code:String = CurrentReq.value.params("code")(0)
    
    // get access Token, store it in session var:
    val result:Option[String] = getAuthenticator.apply.getAccessToken(verification_code)
    if (result != None) {
      val accessToken = result.get.toString
      // store access Token for later use:
      storedToken.set(accessToken)
      "#auth_status" #> ("Authenticated!  (Token: " + accessToken + ")")
    }
    else "#auth_status" #> "Failed to get Access Token from Dwolla!"
    }
    // if code not present in GET params, user might already be authenticated.
    else if (isLoggedIn) "#auth_status" #> ("Authenticated!  (Token: " + storedToken.is + ")")
    else "#auth_status" #> "Missing code parameter from Dwolla - perhaps request for permission rejected by user!"
  }
  
  def getBalance = {
    if (isLoggedIn == true) {
    // retrieve access token from session storage
    val accessToken = storedToken.is.toString
    val mgr = new AccountManager(accessToken)
    
    val operationResult:Option[String] = mgr.checkBalance
      // operationResult will get None if call was unsuccessful
      if (operationResult != None) {
        val balance = operationResult.get.toString
        "#balance" #> ("$" + balance)
      }
      else "#balance" #> "Failed to retrieve balance!"
    }
    // if access token not found
    else "#balance" #> "Couldn't retrieve balance because you're not authenticated."
  }
}


