package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.Widget

abstract class Container extends Widget {
  var suspended: Boolean = false

  protected def recalculateChildren()
}
