package de.schwetschke.bna2.webtests

import net.liftweb.http.Factory
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.WebDriver

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 17.12.12
 * Time: 13:50
 *
 */
object TestDependencyFactory extends Factory {
  implicit object webDriver extends FactoryMaker[WebDriver](() => newHtmlUnitWebDriver(false))

  private def init() {
    List(webDriver)
  }
  init()


  def newHtmlUnitWebDriver(enableJs : Boolean = true) = {
    val driver=new HtmlUnitDriver()
    driver.setJavascriptEnabled(enableJs)
    driver
  }

}

