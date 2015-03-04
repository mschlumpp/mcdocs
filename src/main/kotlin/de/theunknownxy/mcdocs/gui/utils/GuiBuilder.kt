package de.theunknownxy.mcdocs.gui.utils

import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.layout.Constraint
import de.theunknownxy.mcdocs.gui.layout.ExpandingPolicy
import de.theunknownxy.mcdocs.gui.layout.Policy
import de.theunknownxy.mcdocs.gui.layout.FixedPolicy
import de.theunknownxy.mcdocs.gui.widget.Spacer
import de.theunknownxy.mcdocs.gui.container.VerticalBox
import de.theunknownxy.mcdocs.gui.container.MultiContainer
import de.theunknownxy.mcdocs.gui.container.SingleContainer
import de.theunknownxy.mcdocs.gui.container.HorizontalBox

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
        widget.constraint.vertical = policy.policy
    }

    fun hpolicy(init: BPolicy.() -> Unit) {
        val policy = BPolicy()
        policy.init()
        widget.constraint.horizontal = policy.policy
    }

    fun fixed(width: Float, height: Float) {
        widget.constraint = Constraint(FixedPolicy(width), FixedPolicy(height))
    }
}

abstract class BContainer(widget: Widget) : BWidget(widget) {
    abstract fun add(widget: Widget)

    fun initWidget<T : BWidget>(widget: T, init: T.() -> Unit) {
        widget.init()
        add(widget.widget)
    }

    fun spacer(init: BSpacer.() -> Unit) = initWidget(BSpacer(this.widget.root), init)
    fun vbox(init: BVBox.() -> Unit) = initWidget(BVBox(this.widget.root), init)
    fun hbox(init: BHBox.() -> Unit) = initWidget(BHBox(this.widget.root), init)
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

class BVBox(root: Root?) : BMultiContainer(VerticalBox(root))
class BHBox(root: Root?) : BMultiContainer(HorizontalBox(root))
class BSpacer(root: Root?) : BWidget(Spacer(root))
class BRoot : BSingleContainer(Root()) {
    public fun setRoot() {
        this.widget.root = this.widget as Root
    }
}

fun root(init: BRoot.() -> Unit): Root {
    val root = BRoot()
    root.setRoot()
    root.init()
    return root.widget as Root
}