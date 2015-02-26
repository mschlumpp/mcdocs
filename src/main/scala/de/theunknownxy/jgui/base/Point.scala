package de.theunknownxy.jgui.base

class Point(var x: Float, var y: Float) {
  def <(that: Point) : Boolean = this.x < that.x && this.y < that.y
  def <=(that: Point) : Boolean = this.x <= that.x && this.y <= that.y
  def >(that: Point) : Boolean = this.x > that.x && this.y > that.y
  def >=(that: Point) : Boolean = this.x >= that.x && this.y >= that.y

  override def equals(o: Any) : Boolean = {
    o match {
      case that: Point => x == that.x && y == that.y
      case _ => false
    }
  }
}
