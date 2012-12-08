package de.schwetschke.bna2.model

import helper.JQueryDatePicker
import net.liftweb.mapper._
import java.util.Date
import net.liftweb.util.FieldError

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 07.12.12
 * Time: 23:59
 *
 */
class Event extends LongKeyedMapper[Event] with IdPK {
  override def getSingleton = Event

  object start extends MappedDateTime(this) with JQueryDatePicker[Event] {
    override def dbColumnName = "start"
  }

  object end extends MappedDateTime(this) with JQueryDatePicker[Event] {
    override def dbColumnName = "end"
    override def validations = isAfterStart _ :: super.validations
    def isAfterStart(endDate : Date) : List[FieldError] =
      if (start.is !=null && endDate != null && start.is.before(endDate)) Nil else List(FieldError(this,"Start date must be before end date"))

    override def defaultValue = new Date()
  }

  object weatherDecision extends  MappedEnum(this, WeatherDecision) {
    override def dbColumnName = "weather_decission"
  }


}

object WeatherDecision extends Enumeration {
  Value("not_decided_yet")
  Value("takes_place")
  Value("canceled")
}

object Event extends Event with LongKeyedMetaMapper[Event] with CRUDify[Long, Event] {
  override def dbTableName = "events"
}