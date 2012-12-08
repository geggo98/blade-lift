package de.schwetschke.bna2.model.helper

import net.liftweb.mapper.{Mapper, MappedDateTime}
import xml.{NodeSeq, Text}
import java.util.Date
import net.liftweb.http.S
import net.liftweb.common.{Full, Box}
import net.liftweb.util.Helpers.tryo

/**
 * Extends a MappedDateTime so it shows a JQuery date picker widget in CRUDify mode
 * <p>
 * Based on an idea from {https://groups.google.com/forum/?fromgroups=#!topic/liftweb/xymk4hxhINo}
 * User: stefan.schwetschke@googlemail.com
 * Date: 08.12.12
 * Time: 20:24
 *
 */
trait JQueryDatePicker [T <: Mapper[T]] {
  this : MappedDateTime[T] =>

  type DateToString = {def format(d : Date) : String}
  type StringToDate = {def parse(s : String) : Date}

  def javaDateFormat=java.text.DateFormat.MEDIUM
  def dateParser : StringToDate=java.text.DateFormat.getDateInstance(javaDateFormat, S.locale)
  def dateFormatter : DateToString=java.text.DateFormat.getDateInstance(javaDateFormat, S.locale)
  def jQueryDateFormat="dd.mm.yy"

  lazy val _dateParser=dateParser
  lazy val _dateFormatter=dateFormatter

  override def fieldId = Some(Text(name))
  override def setFromAny(f : Any): Date = f match {
    case v :: vs =>
      tryo {_dateParser.parse(v.toString)} map(d => this.set(d)) openOr(this.is)
    case d:Date => this.set(d)
    case _ => setFromAny(f)
  }

  override def _toForm: Box[NodeSeq] = {
    val onLoad={selector : String =>
      """
        |$(function() {
        |    $( "%s" ).datepicker(
        |       {
        |           dateFormat: "%s"
        |       }
        |     );
        | });
      """.stripMargin.format(selector,jQueryDateFormat)
    }
    S.fmapFunc({s: List[String] => this.setFromAny(s)}){funcName =>
      Full(
        <xml:group>
          <head>
            <meta charset="utf-8" />
            <link rel="stylesheet" href="http://code.jquery.com/ui/1.7.2/themes/base/jquery-ui.css" />
            <script src="http://code.jquery.com/ui/1.7.2/jquery-ui.js"></script>
            <link rel="stylesheet" href="/resources/demos/style.css" />
            <script>
              {onLoad(fieldId.map("#"+_).getOrElse("""[name="%s"]""".format(funcName)))}
            </script>
          </head>
          <input type='text' id={fieldId}
                 name={funcName}
                 class="datepicker"
                 value={is match {case null => "" case d => ""+_dateFormatter.format(d)}}/>
        </xml:group>
      )
    }
  }


}
