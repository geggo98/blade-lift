package bootstrap.liftweb

import net.liftweb._

import sitemap._
import Loc._
import widgets.logchanger.{LogbackLoggingBackend, LogLevelChanger}
import de.schwetschke.bna2.model.User


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
object logLevel extends LogLevelChanger with LogbackLoggingBackend {
  override def menuLocParams : List[Loc.AnyLocParam] =
    List(If(() => User.currentUser.dmap(false)(_ isAdmin),"Must be logged in as admin"))

  override def path = List("admin", "loglevel")
}

class Boot {
  def boot {
    ServerManager.configuration.vend.bootLift()
  }
}
