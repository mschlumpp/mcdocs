package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.layout.ExpandingPolicy
import de.theunknownxy.jgui.layout.FixedPolicy

public class VerticalBox : MultiContainer() {

    override fun recalculateChildren() {
        // Step 1: Remove space used by Fixed and collect the total importance of Expanding elements
        var available_space = this.height
        var expanding_sum = 0f
        for (widget in children) {
            // Suspend all widgets
            widget.suspended = true

            // Now calculate expanding_sum/available_space
            for(constraint in widget.getConstraints()) {
                var policy = constraint.vertical
                when(policy) {
                    is ExpandingPolicy -> expanding_sum += (policy as ExpandingPolicy).importance
                    is FixedPolicy -> available_space -= (policy as FixedPolicy).value
                }
            }
        }

        // Step 2: Partition remaining space to the expanding widgets and set the height of fixed widgets
        for (widget in children) {
            widget.height = 0f
            for(constraint in widget.getConstraints()) {
                var policy = constraint.vertical
                if(policy is ExpandingPolicy) {
                    widget.height += (policy as ExpandingPolicy).importance / expanding_sum * available_space
                } else if(policy is FixedPolicy) {
                    widget.height += (policy as FixedPolicy).value
                }
            }
        }

        // Step 3: Adjust widget positions
        var lasty = this.y
        for (widget in children) {
            // Adjust left side
            widget.x = this.x

            // Set width depending on policy
            for(constraint in widget.getConstraints()) {
                val policy = constraint.horizontal
                if(policy is FixedPolicy) {
                    widget.width = Math.max(widget.width, (policy : FixedPolicy).value)
                } else if(policy is ExpandingPolicy) {
                    widget.width = this.width
                }
            }

            // Stack widget on previous
            widget.y = lasty
            lasty += widget.height

            // Unsuspend the widget
            widget.suspended = false
        }
    }
}