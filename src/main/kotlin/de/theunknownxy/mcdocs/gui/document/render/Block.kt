package de.theunknownxy.mcdocs.gui.document.render

public abstract class Block {
    public open var width: Float = 1f
    public open var height: Float
        private set(v: Float) {
        }
        get() {
            throw UnsupportedOperationException()
        }

    abstract fun draw(x: Float, y: Float, z: Float)
}