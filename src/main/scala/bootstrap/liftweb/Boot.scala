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


class Boot {
  def boot() {
    ServerManager.configuration.vend.bootLift()
  }
}
