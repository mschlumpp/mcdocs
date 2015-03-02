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
            val policy = widget.constraint.vertical

            when (policy) {
                is ExpandingPolicy -> expanding_sum += policy.importance
                is FixedPolicy -> available_space -= policy.value
            }
        }

        // Step 2: Partition remaining space to the expanding widgets
        for (widget in children) {
            val policy = widget.constraint.vertical
            if (policy is ExpandingPolicy) {
                widget.height = policy.importance / expanding_sum * available_space
            } else if(policy is FixedPolicy) {
                widget.height = policy.value;
            }
        }

        // Step 3: Adjust widget positions
        var lasty = y
        for (widget in children) {
            // Adjust left side
            widget.x = this.x

            // Set width depending on policy
            val horizontal = widget.constraint.horizontal
            if(horizontal is FixedPolicy) {
                widget.width = horizontal.value
            } else if(horizontal is ExpandingPolicy) {
                widget.width = this.width
            }

            // Stack widget on previous
            widget.y = lasty
            lasty += widget.height

            // Unsuspend the widget
            widget.suspended = false
        }
    }
}