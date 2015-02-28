package de.theunknownxy.jgui.container

public class VerticalBox {
}
/* Old Scala code
import de.theunknownxy.jgui.layout.{Expanding, Fixed}
class VerticalBoxSc extends MultiContainer {
  override protected def recalculateChildren(): Unit = {

    // Remove space used by Fixed and collect the total importance of Expanding elements
    var available_space = this.area.height
    var expanding_sum = 0.0f
    for((widget, constraint) <- children) {
      val policy = constraint.verticalPolicy

      // The scala 'match' seems to be broken
      if(policy.isInstanceOf[Expanding]) expanding_sum += policy.asInstanceOf[Expanding].importance
      else if(policy.isInstanceOf[Fixed]) available_space -= policy.asInstanceOf[Fixed].value
    }

    // Partition remaining space to the expanding widgets
    for((widget, constraint) <- children) {
      constraint.verticalPolicy match {
        case e: Expanding => widget.area.height = e.importance/expanding_sum * available_space
      }
    }

    // Adjust positions
    var lasty = 0.0f
    for((widget, _) <- children) {
      // Set common properties
      widget.area.x = this.area.x
      widget.area.width = this.area.width

      // Set calculated properties
      widget.area.y = lasty
      lasty += widget.area.height
    }
  }
}
*/