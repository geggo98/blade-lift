package bootstrap.liftweb

import de.schwetschke.bna2.model.{UserAdministration, Event, User}
import net.liftweb.mapper.{DB, Schemifier, BaseMetaMapper}
import net.liftweb.sitemap.Loc.If
import net.liftweb.sitemap.{Loc, SiteMap, Menu}
import net.liftweb.http.{PlainTextResponse, Html5Properties, Req, LiftRules}
import net.liftweb.common.{Logger, Full}
import net.liftweb.http.js.jquery.JQueryArtifacts
import net.liftmodules.JQueryModule
import net.liftweb.widgets.logchanger.LogLevelChanger
import net.liftweb.db.{ConnectionManager, DefaultConnectionIdentifier}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.webapp.WebAppContext
import java.net.ServerSocket
import java.io.IOException
import net.liftweb.util.SimpleInjector
import Loc._

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 13.12.12
 * Time: 17:07
 *
 */
abstract class ServerManager extends Logger {
  val warPath = "src/main/webapp"
  val generatePasswordLength=15

  def generatePassword(length : Int = generatePasswordLength) : String

  def startServer()
  def startServer(port : Int)
  def stopServer()
  def serverUrl : String

  def createCoreData()
  def databaseConnectionManager() : DatabaseConnectionManager


  def initDatabase() {
    info("Init database")
    if (!DB.jndiJdbcConnAvailable_?) {
      info("Creating own database connection")
      DB.defineConnectionManager(DefaultConnectionIdentifier, databaseConnectionManager())
    }else{
      info("Using database connection from container")
    }
  }

  val schemaObjects : List[BaseMetaMapper] = List(User,Event)
  def createDatabaseSchema() {
    info("Creating database schema")
    Schemifier.schemify(true, Schemifier.infoF _, schemaObjects: _*)
  }

  def createSiteMap() {
    info("Creating site map")
    val isLoggedIn = If(() => User.currentUser.isDefined, "Not logged in")
    val isAdmin = If(() => User.currentUser.dmap(false)(_ isAdmin), "Must be logged in as admin")

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

  }

  def configureLift()  {
    // add the log level changer widget
    LogLevelChanger.init
    // where to search snippet
    LiftRules.addToPackages("de.schwetschke.bna2")

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

  def bootLift() {
    initDatabase()
    createDatabaseSchema()
    createCoreData()

    configureLift()
    createSiteMap()
  }
}

abstract class DatabaseConnectionManager extends ConnectionManager {
  def closeAll()
}

abstract class BaseConfiguration extends ServerManager with
  JDBCDatabaseWithPooling with JettyServer with GeneratePronounceablePasswords

object DevelopmentConfiguration extends BaseConfiguration with
  H2TransientDatabase with StartServerWithDeveloperPort with GenerateUsers with GenerateDevelopmentUsers

object ProductionConfiguration extends BaseConfiguration with
  H2PersistentDatabase with StartServerWithProductionPort with GenerateProductionAdmin


class TestConfiguration extends BaseConfiguration with
  H2TransientDatabase with StartServerWithRandomPort with GenerateUsers with GenerateTestUsers

object ServerManager extends SimpleInjector {
  val configuration = new Inject(DevelopmentConfiguration) {}
}
