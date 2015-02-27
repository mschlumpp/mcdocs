package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.Widget
import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton.MouseButton
import de.theunknownxy.jgui.layout.Constraint

import scala.collection.mutable

abstract class MultiContainer extends Container {
  var children: mutable.HashMap[Widget, Constraint] = mutable.HashMap()

  def addChild(widget: Widget, constraint: Constraint): Unit = {
    children += (widget -> constraint)
  }

  def removeChild(widget: Widget): Unit = {
    children -= widget
  }

  override def mouseClick(pos: Point, button: MouseButton): Option[Widget] = {
    children find(_._1.area contains pos) flatMap(_._1.mouseClick(pos, button))
  }

  override def draw(): Unit = {
    children map(_._1 draw())
  }
}
