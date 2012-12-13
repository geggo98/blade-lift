package bootstrap.liftweb

import de.schwetschke.bna2.tools.GeneratePassword
import de.schwetschke.bna2.model.User
import net.liftweb.mapper.By

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 13.12.12
 * Time: 22:58
 *
 */

trait GeneratePronounceablePasswords {
  this : ServerManager =>
  def generatePassword(length: Int) = GeneratePassword.generate(length)
}

trait GenerateUsers {
  this : ServerManager =>

  val adminPassword : String
  val userPassword : String

  override def createCoreData() {
    User.create.
      firstName("Admin").lastName("istrator").email("admin@istrator.invalid").
      validated(true).superUser(true).password(adminPassword).
      save()

    User.create.
      firstName("User").lastName("Validated").email("user@validated.invalid").
      validated(true).superUser(false).password(userPassword).
      save()

    User.create.
      firstName("User").lastName("Notalidated").email("user@notalidated.invalid").
      validated(false).superUser(false).password(userPassword).
      save()
  }

}

trait GenerateTestUsers {
  this : ServerManager with GenerateUsers =>

  val adminPassword=generatePassword()
  val userPassword=generatePassword()
}

trait GenerateDevelopmentUsers {
  this : GenerateUsers =>

  val adminPassword="ChruxTutt6"
  val userPassword="EdPhesHij6"
}

trait GenerateProductionAdmin {
  this : ServerManager =>

  override def createCoreData() {
    val adminPassword=generatePassword()
    val adminMail="geggo98@gmx.de"

    info("Delete old server admin user")
    User.findAll(By(User.email, adminMail)).foreach( _ delete_!)
    info("Create new server admin")
    User.create.
      firstName("Stefan").lastName("Schwetschke").email(adminMail).
      validated(true).superUser(true).password(adminPassword).
      save()
    List(warn(_ : String), Console.print _).foreach(_("""Server Admin: eMail "%s", password "%s"""".format(adminMail,adminPassword)))

  }
}
