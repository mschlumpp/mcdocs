package de.theunknownxy.mcdocs.gui.utils

import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.layout.*
import de.theunknownxy.mcdocs.gui.container.*
import de.theunknownxy.mcdocs.gui.widget.*
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation

/****************
 * Base classes *
 ****************/
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

    fun id(name: String) {
        widget.root!!.named_widgets[name] = this.widget
    }
}

abstract class BContainer(widget: Widget) : BWidget(widget) {
    abstract fun add(widget: Widget)

    fun initWidget<T : BWidget>(widget: T, init: T.() -> Unit) {
        widget.init()
        add(widget.widget)
    }

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

/********************************
 * Classes for concrete Widgets *
 ********************************/
class BVBox(root: Root?) : BMultiContainer(VerticalBox(root))

class BHBox(root: Root?) : BMultiContainer(HorizontalBox(root))
class BSpacer(root: Root?) : BWidget(Spacer(root))
class BImage(root: Root?) : BWidget(Image(root)) {
    fun path(res_path: String) {
        val img = widget as Image
        img.tex = ResourceLocation(res_path)
    }
}

class BTextField(root: Root?) : BWidget(TextField(root)) {
    fun content(str: String) {
        val field = widget as TextField
        field.content = str
    }
}

class BButton(root: Root?) : BWidget(Button(root)) {
    fun text(str: String) {
        val button = widget as Button
        button.text = str
    }

    fun onClick(callback: (Button) -> Unit) {
        val button = widget as Button
        button.callback = callback
    }
}

class BRoot(gui: GuiScreen) : BSingleContainer(Root(gui)) {
    public fun setRoot() {
        this.widget.root = this.widget as Root
    }
}

fun BContainer.image(init: BImage.() -> Unit) = initWidget(BImage(this.widget.root), init)
fun BContainer.textfield(init: BTextField.() -> Unit) = initWidget(BTextField(this.widget.root), init)
fun BContainer.spacer(init: BSpacer.() -> Unit) = initWidget(BSpacer(this.widget.root), init)
fun BContainer.vbox(init: BVBox.() -> Unit) = initWidget(BVBox(this.widget.root), init)
fun BContainer.hbox(init: BHBox.() -> Unit) = initWidget(BHBox(this.widget.root), init)
fun BContainer.button(init: BButton.() -> Unit) = initWidget(BButton(this.widget.root), init)

fun root(gui: GuiScreen, init: BRoot.() -> Unit): Root {
    val root = BRoot(gui)
    root.setRoot()
    root.init()
    return root.widget as Root
}