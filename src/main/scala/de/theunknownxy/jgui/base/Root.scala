package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.container.SingleContainer

import scala.collection.mutable

class Root extends SingleContainer {
  var named_widgets: mutable.HashMap[String, Widget] = new mutable.HashMap()

  override protected def recalculateChildren(): Unit = {
    child map (_.area = this.area)
  }
}
