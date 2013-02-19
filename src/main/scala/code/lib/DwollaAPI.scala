package code.lib

import net.liftweb.http.CurrentReq
import net.liftweb._
import util.Helpers._
import common._
import http._
import sitemap._
import java.util.Date
import dispatch._
import net.liftweb.json._
import java.net.URLEncoder

/* Dwolla REST API Wrapper
 * 
 * Authenticator Class
 * 	- contains methods that facilitate the OAuth process
 * 		- generate permission redirect link (step 1)
 * 		- gets access token (step 3)
 *  - inherits Dispatch connection functionality from Connector trait to make calls
 *  
 * Account Class
 *  - requires access token
 *  - performs REST calls to check balance, etc.
 */

trait Connector {
  /*
   * Handles outgoing HTTP requests
   */
  def makeCall(target_url:String):String = {
    /*
     *  Send request to target_url, return response
     */
    val req = url(target_url)
    val getrsp = Http(req OK as.String)
    getrsp()
  }
}

class AccountManager(accessToken:String) extends Connector {
  /*
   * Handles account operations (i.e. check balance, make payment, etc.)
   */
  def checkBalance:Option[String] = {
    val endpointURL:String = "https://www.dwolla.com/oauth/rest/balance/?oauth_token=%s"
    // make call:
    val response:String = makeCall(endpointURL.format(URLEncoder.encode(accessToken, "UTF-8")))
    // parse JSON:
    val parsedResponse = parse(response).asInstanceOf[JObject].values
    //check if operation was successful and return balance, else return None
    if ((parsedResponse get "Success").get.toString.toBoolean == true) {
      if (parsedResponse contains "Response") {
	    val balance:String = (parsedResponse get "Response").get.toString
	    Some(balance)
      }
      else None
    }
    else None
  }
}

class Authenticator(clientKey:String, secret:String, redirect:String) extends Connector {
  /*
   * Handles the OAuth process steps
   */
  val DwollaBaseURL:String = "https://www.dwolla.com/oauth/v2/"
  // scope of permissions requested by app:
  val scope:String = "balance"
    
  def getBeginLink:String = {
    /*
     * Crafts and returns the Dwolla permission page URL (Step 1)
     */
    val response_type:String = "code"
    val url_format:String = "authenticate?client_id=%s&response_type=%s&redirect_uri=%s&scope=%s"
    DwollaBaseURL + url_format.format(URLEncoder.encode(clientKey, "UTF-8"), response_type, redirect, scope)
  }
  
  def getAccessToken(verification_code:String):Option[String] = {
    /*
     * Given a verification code from Step 2, getAccessToken will send the 
     * code along with the client key, secret, redirect uri, and grant
     * type.
     */
    // assemble request URL:
    val grant_type:String = "authorization_code"
    val callURLFormat:String = "token?client_id=%s&client_secret=%s&grant_type=%s&redirect_uri=%s&code=%s"
    var url:String = DwollaBaseURL + callURLFormat.format(
        URLEncoder.encode(clientKey, "UTF-8"), 
        URLEncoder.encode(secret, "UTF-8"), 
        grant_type, 
        redirect, 
        URLEncoder.encode(verification_code, "UTF-8"))
    
    // make request to url, store response:
    val response = makeCall(url)
    
    // parse JSON response:
    val parsedResponse = parse(response).asInstanceOf[JObject].values
    
    //check if auth was successful and return token, else return None
    if (parsedResponse contains "access_token") {
	    val accessToken:String = (parsedResponse get "access_token").get.toString
	    Some(accessToken)
    }
    else None
  }
}
