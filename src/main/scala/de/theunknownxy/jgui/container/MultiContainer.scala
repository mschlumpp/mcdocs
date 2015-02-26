package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.event.MouseButton.MouseButton
import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.Widget
import de.theunknownxy.jgui.layout.Constraint

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

abstract class MultiContainer extends Container {
  var children: ArrayBuffer[Widget] = ArrayBuffer()
  var childrenConstraints: mutable.HashMap[Widget, Constraint] = mutable.HashMap()

  def addChild(widget: Widget, constraint: Constraint): Unit = {
    children += widget
    childrenConstraints += (widget -> constraint)
  }

  def removeChild(widget: Widget): Unit = {
    children -= widget
    childrenConstraints -= widget
  }

  override def mouseClick(pos: Point, button: MouseButton): Option[Widget] = {
    children find(_.area contains pos) flatMap(_.mouseClick(pos, button))
  }
}
