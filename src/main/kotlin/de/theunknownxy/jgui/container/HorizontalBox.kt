package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.layout.ExpandingPolicy
import de.theunknownxy.jgui.layout.FixedPolicy

public class HorizontalBox : MultiContainer() {
    // TODO: Deduplicate this code
    override fun recalculateChildren() {
        // Step 1: Remove space used by Fixed and collect the total importance of Expanding elements
        var available_space = this.width
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
                widget.width = policy.importance / expanding_sum * available_space
            } else if(policy is FixedPolicy) {
                widget.width = policy.value;
            }
        }

        // Step 3: Adjust widget positions
        var lastx = x
        for ((widget, constraint) in children) {
            // Adjust top side
            widget.y = this.y

            // Set height depending on policy
            val vertical = constraint.vertical
            if(vertical is FixedPolicy) {
                widget.height = vertical.value
            } else if(vertical is ExpandingPolicy) {
                widget.height= this.width
            }

            // Stack widget on previous
            widget.x = lastx
            lastx += widget.width

            // Call area changed method
            widget.onAreaChanged()
        }
    }
}