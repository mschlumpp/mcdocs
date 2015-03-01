package de.theunknownxy.jgui.utils

import de.theunknownxy.jgui.base.Root
import de.theunknownxy.jgui.base.Widget
import de.theunknownxy.jgui.layout.Constraint
import de.theunknownxy.jgui.layout.ExpandingPolicy
import de.theunknownxy.jgui.layout.Policy
import de.theunknownxy.jgui.layout.FixedPolicy
import de.theunknownxy.jgui.widget.Spacer
import de.theunknownxy.jgui.container.VerticalBox
import de.theunknownxy.jgui.container.MultiContainer
import de.theunknownxy.jgui.container.SingleContainer

class BPolicy {
    var policy: Policy = ExpandingPolicy(1f)

    fun expanding(importance: Float) {
        policy = ExpandingPolicy(importance)
    }

    fun fixed(value: Float) {
        policy = FixedPolicy(value)
    }
}

abstract class BWidget(var widget: Widget) {
    var constraint: Constraint = Constraint(ExpandingPolicy(1f), ExpandingPolicy(1f))

    fun vpolicy(init: BPolicy.() -> Unit) {
        val policy = BPolicy()
        policy.init()
        constraint.vertical = policy.policy
    }

    fun hpolicy(init: BPolicy.() -> Unit) {
        val policy = BPolicy()
        policy.init()
        constraint.horizontal = policy.policy
    }
}

abstract class BContainer(widget: Widget) : BWidget(widget) {
    abstract fun put(widget: Widget, constraint: Constraint)

    fun initWidget<T : BWidget>(widget: T, init: T.() -> Unit) {
        widget.init()
        put(widget.widget, widget.constraint)
    }

    fun spacer(init: BSpacer.() -> Unit) = initWidget(BSpacer(), init)
    fun vbox(init: BVBox.() -> Unit) = initWidget(BVBox(), init)
}

abstract class BMultiContainer(widget: Widget) : BContainer(widget) {
    override fun put(widget: Widget, constraint: Constraint) {
        (widget as MultiContainer).children.put(widget, constraint)
    }
}

abstract class BSingleContainer(widget: Widget) : BContainer(widget) {
    override fun put(widget: Widget, constraint: Constraint) {
        val container = widget as SingleContainer
        container.child = widget
        container.constraint = constraint
    }
}

class BVBox : BMultiContainer(VerticalBox())
class BRoot : BSingleContainer(Root())
class BSpacer : BWidget(Spacer())

fun root(init: BRoot.() -> Unit): Root {
    val root = BRoot()
    root.init()
    return root.widget as Root
}