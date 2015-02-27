package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.{Widget, Point}
import de.theunknownxy.jgui.event.MouseButton.MouseButton

abstract class SingleContainer extends Container {
  var child: Option[Widget] = None

  def setChild(widget: Widget): Unit = {
    child = Some(widget)
  }

  override def mouseClick(pos: Point, button: MouseButton): Option[Widget] = {
    child filter(_.area contains pos) flatMap(_.mouseClick(pos, button))
  }

  override def draw(): Unit = {
    child map (_ draw())
  }
}
