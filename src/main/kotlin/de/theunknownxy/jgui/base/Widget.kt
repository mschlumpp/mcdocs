package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.event.MouseButton
import de.theunknownxy.jgui.layout.Constraint
import de.theunknownxy.jgui.layout.ExpandingPolicy
import java.util.ArrayList
import de.theunknownxy.jgui.layout.Policy

public abstract class Widget {
    public var suspended: Boolean = false
        set(value) {
            $suspended = value
            if(!value) areaChanged()
        }
    public var x: Float = 0f
        set(value) {
            $x = value
            areaChanged()
        }
    public var y: Float = 0f
        set(value) {
            $y = value
            areaChanged()
        }
    public var width: Float = 0f
        set(value) {
            $width = value
            areaChanged()
        }
    public var height: Float = 0f
        set(value) {
            $height = value
            areaChanged()
        }
    public var rect: Rectangle
        get() = Rectangle(x, y, width, height)
        set(value) {
            $x = value.x
            $y = value.y
            $width = value.width
            $height = value.height
            areaChanged()
        }
    private var _constraint: Constraint? = Constraint(ExpandingPolicy(1f), ExpandingPolicy(1f))

    public fun setConstraint(constraint: Constraint?) {
        _constraint = constraint
    }

    public fun setVerticalPolicy(policy: Policy) {
        if(_constraint != null) {
            _constraint!!.vertical = policy
        } else {
            _constraint = Constraint(ExpandingPolicy(1f), policy)
        }
    }

    public fun setHorizontalPolicy(policy: Policy) {
        if(_constraint != null) {
            _constraint!!.horizontal = policy
        } else {
            _constraint = Constraint(policy, ExpandingPolicy(1f))
        }
    }

    public open fun getConstraints(): MutableList<Constraint> {
        val ar = ArrayList<Constraint>()
        if(_constraint != null) {
            ar.add(_constraint)
        }
        return ar
    }

    protected fun areaChanged() {
        if(!suspended) onAreaChanged()
    }

    public abstract fun draw()
    public open fun onMouseClick(pos: Point, button: MouseButton): Widget? = null
    protected open fun onAreaChanged() {}
}