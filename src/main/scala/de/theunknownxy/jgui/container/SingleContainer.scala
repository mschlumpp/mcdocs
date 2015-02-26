package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.Widget

abstract class SingleContainer extends Container {
  var child: Option[Widget] = None

  def setChild(widget: Widget): Unit = {
    child = Some(widget)
  }
}
