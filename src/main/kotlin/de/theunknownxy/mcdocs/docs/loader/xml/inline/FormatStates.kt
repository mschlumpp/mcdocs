package de.theunknownxy.mcdocs.docs.loader.xml.inline

import de.theunknownxy.mcdocs.docs.FormatElement
import de.theunknownxy.mcdocs.docs.FormatStyle
import de.theunknownxy.mcdocs.docs.loader.xml.XMLParserHandler

private class UnderlineState(handler: XMLParserHandler, parent: InlineContainerState?) : InlineContainerState(handler, parent) {
    override val container = FormatElement(FormatStyle.UNDERLINE)
    override val name = "u"
}

private class BoldState(handler: XMLParserHandler, parent: InlineContainerState?) : InlineContainerState(handler, parent) {
    override val container = FormatElement(FormatStyle.BOLD)
    override val name = "b"
}

private class ItalicState(handler: XMLParserHandler, parent: InlineContainerState?) : InlineContainerState(handler, parent) {
    override val container = FormatElement(FormatStyle.ITALIC)
    override val name = "i"
}
