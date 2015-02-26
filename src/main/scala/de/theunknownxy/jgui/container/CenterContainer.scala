package de.theunknownxy.jgui.container

class CenterContainer extends SingleContainer {
  override def draw(): Unit = {
    child map(_.draw())
  }

  override protected def recalculateChildren(): Unit = {
    //TODO: Implement recalculateChildren()
  }
}
