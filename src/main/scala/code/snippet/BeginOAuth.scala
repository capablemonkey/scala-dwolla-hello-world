package code
package snippet

import net.liftweb.http.CurrentReq

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import java.net.URLEncoder
import net.liftweb.http.rest._


class BeginOAuth {
  def getLink = {
    // craft and fill in permission URL as href of element with id "begin"
    "#begin [href]" #> getAuthenticator.apply.getBeginLink
  }
}
