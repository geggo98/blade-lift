import _root_.org.eclipse.jetty.server.handler.ContextHandlerCollection
import _root_.org.eclipse.jetty.webapp.WebAppContext
import _root_.org.eclipse.jetty.server.Server
import bootstrap.liftweb.Database
import de.schwetschke.bna2.model.User

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 03.12.12
 * Time: 16:47
 *
 */
object StartServer extends App {
  val GUI_PORT=8081
  val server  = new Server(GUI_PORT)
  server.setHandler(new ContextHandlerCollection)
  val context = new WebAppContext()
  context.setServer(server)
  context.setContextPath("/")
  context.setWar("src/main/webapp")
  server.getHandler match {
    case handler : ContextHandlerCollection => handler.addHandler(context)
  }
  server.start()
  Seq("Started Jetty on localhost:%s" format(GUI_PORT), "Press ENTER to exit").foreach (Console.println _)
  Console.readLine()
  server.stop()
  Database.closeAll()
}
