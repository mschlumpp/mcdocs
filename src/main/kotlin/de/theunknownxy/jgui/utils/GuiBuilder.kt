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
import de.theunknownxy.jgui.container.HorizontalBox

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
    fun vpolicy(init: BPolicy.() -> Unit) {
        val policy = BPolicy()
        policy.init()
        widget.setVerticalPolicy(policy.policy)
    }

    fun hpolicy(init: BPolicy.() -> Unit) {
        val policy = BPolicy()
        policy.init()
        widget.setHorizontalPolicy(policy.policy)
    }

    fun fixed(width: Float, height: Float) {
        widget.setConstraint(Constraint(FixedPolicy(width), FixedPolicy(height)))
    }
}

abstract class BContainer(widget: Widget) : BWidget(widget) {
    abstract fun add(widget: Widget)

    fun initWidget<T : BWidget>(widget: T, init: T.() -> Unit) {
        widget.init()
        add(widget.widget)
    }

    fun spacer(init: BSpacer.() -> Unit) = initWidget(BSpacer(), init)
    fun vbox(init: BVBox.() -> Unit) = initWidget(BVBox(), init)
    fun hbox(init: BHBox.() -> Unit) = initWidget(BHBox(), init)
}

abstract class BMultiContainer(widget: Widget) : BContainer(widget) {
    override fun add(widget: Widget) {
        (this.widget as MultiContainer).children.add(widget)
    }
}

abstract class BSingleContainer(widget: Widget) : BContainer(widget) {
    override fun add(widget: Widget) {
        val container = this.widget as SingleContainer
        container.child = widget
    }
}

class BVBox : BMultiContainer(VerticalBox())
class BHBox : BMultiContainer(HorizontalBox())
class BRoot : BSingleContainer(Root())
class BSpacer : BWidget(Spacer())

fun root(init: BRoot.() -> Unit): Root {
    val root = BRoot()
    root.init()
    return root.widget as Root
}