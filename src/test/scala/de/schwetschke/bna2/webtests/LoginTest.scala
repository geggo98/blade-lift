package de.schwetschke.bna2.webtests

import org.specs2.mutable.Specification
import org.specs2.specification.AroundExample
import org.specs2.execute.Result
import bootstrap.liftweb.{ServerManager, TestConfiguration}
import net.liftweb.common.Logger
import org.openqa.selenium.By

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 17.12.12
 * Time: 09:08
 *
 */
class LoginTest extends Specification with AroundExample with Logger {

  val serverManager = new TestConfiguration
  protected def around[T <% Result](body: => T) = {
    ServerManager.configuration.doWith(serverManager) {
      try{
        serverManager.startServer()
        info("Using server port %s".format(serverManager.port))
        body
      }finally{
        serverManager.stopServer()
      }
    }
  }

  "Login dialog" should {
    "Accept user with correct password" in {
      val serverUrl=serverManager.serverUrl

      val selenium=TestDependencyFactory.webDriver.vend
      selenium.get(trace("Open URL",serverUrl))
      selenium.findElement(By.xpath("""//a[@href="/user_mgt/login"]""")).click()

      selenium.getCurrentUrl must endWith("/user_mgt/login")
      selenium.findElement(By.name("username")).sendKeys("admin@istrator.invalid")
      selenium.findElement(By.name("password")).sendKeys(trace("Generated admin passsword",serverManager.adminPassword))
      selenium.findElement(By.xpath("""//form[@action="/user_mgt/login"]""")).submit()

      selenium.close()

      "OK" must have length(2)
    }
  }

}
