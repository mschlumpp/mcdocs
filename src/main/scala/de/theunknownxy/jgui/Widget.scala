package de.theunknownxy.jgui

import de.theunknownxy.jgui.event.MouseButton
import MouseButton.MouseButton
import de.theunknownxy.jgui.base.{Rectangle, Point}
import de.theunknownxy.jgui.container.Container

abstract class Widget {
  var area: Rectangle = new Rectangle(0, 0, 0, 0)
  var parent: Option[Container] = None

  def draw()
  def mouseClick(pos: Point, button: MouseButton) : Option[Widget] = None
}
