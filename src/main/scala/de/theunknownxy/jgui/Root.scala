package de.theunknownxy.jgui

import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton

import scala.collection.mutable

class Root {
  var named_widgets: mutable.HashMap[String, Widget] = new mutable.HashMap()
  var root_widget: Option[Widget] = None

  def injectMouseClick(x: Int, y: Int, button: MouseButton.MouseButton): Unit = {
    root_widget map (_ mouseClick(new Point(x, y), button))
  }

  def draw(): Unit = {
    root_widget map (_ draw())
  }
}
