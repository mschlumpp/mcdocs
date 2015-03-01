package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.layout.ExpandingPolicy
import de.theunknownxy.jgui.layout.FixedPolicy

public class HorizontalBox : MultiContainer() {
    // TODO: Deduplicate this code
    override fun recalculateChildren() {
        // Step 1: Remove space used by Fixed and collect the total importance of Expanding elements
        var available_space = this.area.width
        var expanding_sum = 0f
        for ((widget, constraint) in children) {
            val policy = constraint.horizontal

            when (policy) {
                is ExpandingPolicy -> expanding_sum += policy.importance
                is FixedPolicy -> available_space -= policy.value
            }
        }

        // Step 2: Partition remaining space to the expanding widgets and set the width of fixed widgets
        for ((widget, constraint) in children) {
            val policy = constraint.horizontal
            if (policy is ExpandingPolicy) {
                widget.area.width = policy.importance / expanding_sum * available_space
            } else if(policy is FixedPolicy) {
                widget.area.width = policy.value;
            }
        }

        // Step 3: Adjust widget positions
        var lastx = area.x
        for ((widget, constraint) in children) {
            // Adjust top side
            widget.area.y = this.area.y

            // Set height depending on policy
            val vertical = constraint.vertical
            if(vertical is FixedPolicy) {
                widget.area.height = vertical.value
            } else if(vertical is ExpandingPolicy) {
                widget.area.height= this.area.width
            }

            // Stack widget on previous
            widget.area.x = lastx
            lastx += widget.area.width

            // Call area changed method
            widget.areaChanged()
        }
    }
}