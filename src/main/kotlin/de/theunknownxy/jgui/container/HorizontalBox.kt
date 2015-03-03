package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.layout.ExpandingPolicy
import de.theunknownxy.jgui.layout.FixedPolicy

public class HorizontalBox : MultiContainer() {
    // TODO: Deduplicate this code
    override fun recalculateChildren() {
        // Step 1: Remove space used by Fixed and collect the total importance of Expanding elements
        var available_space = this.width
        var expanding_sum = 0f
        for (widget in children) {
            // Suspend all widgets
            widget.suspended = true

            // Now calculate expanding_sum/available_space
            for(constraint in widget.getConstraints()) {
                var policy = constraint.horizontal
                when(policy) {
                    is ExpandingPolicy -> expanding_sum += (policy as ExpandingPolicy).importance
                    is FixedPolicy -> available_space -= (policy as FixedPolicy).value
                }
            }
        }

        // Step 2: Partition remaining space to the expanding widgets and set the width of fixed widgets
        for (widget in children) {
            widget.width = 0f
            for(constraint in widget.getConstraints()) {
                var policy = constraint.horizontal
                if(policy is ExpandingPolicy) {
                    widget.width += (policy as ExpandingPolicy).importance / expanding_sum * available_space
                } else if(policy is FixedPolicy) {
                    widget.width += (policy as FixedPolicy).value
                }
            }
        }

        // Step 3: Adjust widget positions
        var lastx = this.x
        for (widget in children) {
            // Adjust top side
            widget.y = this.y

            // Set height depending on policy
            for(constraint in widget.getConstraints()) {
                val policy = constraint.vertical
                if(policy is FixedPolicy) {
                    widget.height = Math.max(widget.height, (policy : FixedPolicy).value)
                } else if(policy is ExpandingPolicy) {
                    widget.height = this.width
                }
            }

            // Stack widget on previous
            widget.x = lastx
            lastx += widget.width

            // Unsuspend the widget
            widget.suspended = false
        }
    }
}