package de.theunknownxy.jgui

import de.theunknownxy.jgui.base.{Point, Rectangle}
import de.theunknownxy.jgui.container.Container
import de.theunknownxy.jgui.event.MouseButton.MouseButton

abstract class Widget {
  var area: Rectangle = new Rectangle(0, 0, 0, 0)
  var parent: Option[Container] = None

  def draw()
  def mouseClick(pos: Point, button: MouseButton) : Option[Widget] = None
}
