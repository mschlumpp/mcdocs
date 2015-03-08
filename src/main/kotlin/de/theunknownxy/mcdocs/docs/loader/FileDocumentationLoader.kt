package de.theunknownxy.mcdocs.docs.loader

import de.theunknownxy.mcdocs.docs.DocumentationNodeRef
import de.theunknownxy.mcdocs.docs.DocumentationNode
import javax.xml.parsers.SAXParserFactory
import java.util.ArrayList
import de.theunknownxy.mcdocs.docs.Content
import de.theunknownxy.mcdocs.docs.ParagraphElement
import de.theunknownxy.mcdocs.docs.TextCommand
import java.nio.file.Path

public class FileDocumentationLoader(private val root_path: Path) : DocumentationLoader {
    private val extension = ".xml"

    override fun load(ref: DocumentationNodeRef): DocumentationNode {
        var filepath: Path = root_path.resolve(ref.path)

        // Check whether the path is valid
        if (!filepath.startsWith(root_path)) {
            throw IllegalArgumentException("The path '" + filepath.toString() + "' is not within '" + root_path.toString())
        }

        var childs: MutableList<DocumentationNodeRef> = ArrayList()
        // Construct the real filesystem path
        if (filepath.toFile().isDirectory()) {
            // Search for childs
            for (child in filepath.toFile().list()) {
                if (child != "index.xml") {
                    val childpath = filepath.resolve(child.replace(".xml", ""))
                    childs.add(DocumentationNodeRef(root_path.relativize(childpath).toString()))
                }
            }

            // The actual content is within the folder
            filepath = filepath.resolve("index" + extension)

        } else {
            // Append the .xml suffix if it's a file
            filepath = filepath.getParent().resolve((filepath.getFileName().toString() + extension))
        }

        if (!filepath.toFile().exists()) {
            val node = DocumentationNode(ref, "FIXME", null)
            node.children = childs
            return node
        }

        // Read and parse the file
        var title: String?
        var content: Content?
        try {
            val spf = SAXParserFactory.newInstance()
            val parser = spf.newSAXParser()
            val handler = XMLParserHandler()
            parser.parse(filepath.toFile(), handler)

            title = handler.document.title
            content = handler.document.content
        } catch(e: Exception) {
            title = "Invalid"

            // Create a page with the error
            content = Content()
            val p: ParagraphElement = ParagraphElement()
            p.commands.add(TextCommand("Invalid document!" + e.toString()))
            content?.blocks?.add(p)
        }

        val node = DocumentationNode(ref, title!!, content)
        node.children = childs
        return node
    }
}