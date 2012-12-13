package bootstrap.liftweb

import net.liftweb._
import db.{DefaultConnectionIdentifier, ConnectionIdentifier, ConnectionManager}

import common._
import http._
import mapper.{Schemifier, DB}
import sitemap._
import Loc._
import net.liftmodules.JQueryModule
import net.liftweb.http.js.jquery._
import java.sql.Connection
import org.h2.jdbcx.JdbcConnectionPool
import util.Props
import widgets.logchanger.{LogbackLoggingBackend, Log4jLoggingBackend, LogLevelChanger}
import de.schwetschke.bna2.model.{Event, UserAdministration, User}


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
