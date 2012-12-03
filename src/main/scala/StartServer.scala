import _root_.org.eclipse.jetty.server.handler.ContextHandlerCollection
import _root_.org.eclipse.jetty.webapp.WebAppContext
import _root_.org.eclipse.jetty.server.Server

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
  Console println "Started Jetty on localhost:%s\nPress ENTER to exit".format(GUI_PORT)
  Console.readLine()
  server.stop()
}
