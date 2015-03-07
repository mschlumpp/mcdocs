package de.theunknownxy.mcdocs.docs

import java.util.ArrayList

open class InlineCommand {

}

class TextCommand(var text: String) : InlineCommand()
class BoldCommand(var enable: Boolean) : InlineCommand()
class ItalicCommand(var enable: Boolean) : InlineCommand()
class UnderlineCommand(var enable: Boolean) : InlineCommand()
class LinkCommand(var text: String, var ref: String) : InlineCommand()

open class BlockElement {

}

class ImageElement(var src: String) : BlockElement()
class HeadingElement(val level: Int, val text: String) : BlockElement()

class ParagraphElement : BlockElement() {
    var commands: MutableList<InlineCommand> = ArrayList()
}

class Content {
    var blocks: MutableList<BlockElement> = ArrayList()
}