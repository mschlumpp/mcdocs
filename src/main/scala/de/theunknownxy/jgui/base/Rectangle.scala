package de.theunknownxy.jgui.base

class Rectangle(var x: Float, var y: Float, var width: Float, var height: Float) {
  def topleft(): Point = new Point(x, y)
  def bottomRight(): Point = new Point(x + width, y + height)

  def x2() : Float = x + width
  def y2() : Float = y + height

  def contains(point: Point): Boolean = {
    topleft() <= point && point <= bottomRight()
  }
}
