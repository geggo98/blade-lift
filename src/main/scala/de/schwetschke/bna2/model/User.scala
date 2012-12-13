package de.schwetschke.bna2.model

import net.liftweb.mapper._
import net.liftweb.sitemap.Loc.LocGroup

/**
 * Entity class for users
 */
class User extends MegaProtoUser[User] {
  override def getSingleton = User
  def isAdmin : Boolean = superUser.is
}

/**
 * Provides the meta model for User together with the self administration features for each user
 */
object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users"

  def currentUserIsAdmin : Boolean = currentUser.dmap(false)(_ isAdmin)


  val profileLocParams = LocGroup("userProfile") :: Nil
  val loginLocParams = LocGroup("userLogin") :: Nil

  override protected def loginMenuLocParams = loginLocParams ::: super.loginMenuLocParams

  override protected def logoutMenuLocParams = loginLocParams ::: super.logoutMenuLocParams

  override protected def lostPasswordMenuLocParams = loginLocParams ::: super.lostPasswordMenuLocParams

  override protected def createUserMenuLocParams = loginLocParams ::: super.createUserMenuLocParams


  override protected def resetPasswordMenuLocParams = profileLocParams ::: super.resetPasswordMenuLocParams

  override protected def editUserMenuLocParams = profileLocParams ::: super.editUserMenuLocParams

  override protected def changePasswordMenuLocParams = profileLocParams ::: super.changePasswordMenuLocParams

  override protected def validateUserMenuLocParams = profileLocParams ::: super.validateUserMenuLocParams
}

/**
 * Again the meta model for User, but this time without self administration but with the CRUD view for the administrator
 */
object UserAdministration extends User with KeyedMetaMapper[Long, User] with ProtoUser[User] with CRUDify[Long, User] {
  override def dbTableName = User.dbTableName

  val menuLocParams = LocGroup("userAdministration") :: Nil

  override protected def addlMenuLocParams = menuLocParams ::: super.addlMenuLocParams
}
