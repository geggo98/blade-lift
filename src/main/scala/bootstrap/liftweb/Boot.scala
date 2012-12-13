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
    val isLoggedIn = If(() => User.currentUser.isDefined, "Not logged in")
    val isAdmin = If(() => User.currentUser.dmap(false)(_ isAdmin), "Must be logged in as admin")

    // add the log level changer widget
    LogLevelChanger.init
    // where to search snippet
    LiftRules.addToPackages("de.schwetschke.bna2")

    // Configure database connection
    if (!DB.jndiJdbcConnAvailable_?) {
      DB.defineConnectionManager(DefaultConnectionIdentifier, Database)
    }
    // Init database
    Schemifier.schemify(true, Schemifier.infoF _, User, Event)
    // Create initial values
    User.create.
      firstName("Admin").lastName("istrator").email("admin@istrator.invalid").
      validated(true).superUser(true).password("ChruxTutt6").
      save()


    // Build SiteMap
    // Menu.i("Home") / "admin" / "index" means admin/index.html with link name "Home"
    val entries = List[Menu](
      Menu.i("Home")        / "index",
      logLevel.menu,
      Menu.i("adminloc")       / "admin" / "index" >> isAdmin submenus (
          Menu.i("useradmin")  / "useradmin" submenus (
          UserAdministration.menus: _*
          ),
        Menu.i("serveradmin")  / "serveradmin"
        ),
      Menu.i("tracks")      / "tracks" / "index" >> isAdmin,
      Menu.i("eventadmin")  / "tracks" / "events" >> isAdmin submenus (
          Event.menus: _*
        )
      //Menu(Loc("Static", Link(List("static"), true, "/static/index"),"Static Content"))
    )  ::: User.menus
    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery=JQueryModule.JQuery172
    JQueryModule.init()

  }
}

object Boot extends Boot{

}