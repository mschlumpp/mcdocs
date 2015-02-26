package de.theunknownxy.jgui

import de.theunknownxy.jgui.event.MouseButton

import scala.collection.mutable

class Root {
  var named_widgets: mutable.HashMap[String, Widget] = new mutable.HashMap()


  def injectMouseClick(x: Int, y: Int, button: MouseButton.MouseButton): Unit = {

  }
}
