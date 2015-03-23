package de.theunknownxy.mcdocs.docs

import java.util.ArrayList

/*
 * Inline Elements
 */
open class InlineElement

class TextElement(val text: String) : InlineElement()
class LinkElement(val text: String, val ref: String) : InlineElement()

open class InlineContainerElement : InlineElement() {
    val childs: MutableList<InlineElement> = ArrayList()
}

enum class FormatStyle {
    BOLD
    ITALIC
    UNDERLINE
}

class FormatElement(val style: FormatStyle) : InlineContainerElement()
class ParagraphElement() : InlineContainerElement()

/*
 * Block elements
 */
open class BlockElement

class ImageBlock(var src: String, val width: Int, val height: Int) : BlockElement()
class HeadingBlock(val level: Int, val text: String) : BlockElement()
class ParagraphBlock(val value: ParagraphElement) : BlockElement()

class Content {
    var blocks: MutableList<BlockElement> = ArrayList()
}