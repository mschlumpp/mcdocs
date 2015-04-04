package de.theunknownxy.mcdocs.docs.loader

import de.theunknownxy.mcdocs.docs.*
import de.theunknownxy.mcdocs.docs.loader.xml.XMLParserHandler
import java.io.File
import java.nio.file.Path
import java.util.ArrayList
import javax.xml.parsers.SAXParserFactory

public class FileDocumentationLoader(private val root_path: Path) : DocumentationLoader {
    private val extension = ".xml"

    private fun get_nodepath(ref: DocumentationNodeRef): Path {
        var filepath: Path = root_path.resolve(ref.path)

        // Check whether the path is valid
        if (!filepath.startsWith(root_path)) {
            throw IllegalArgumentException("The path '$filepath' is not within '$root_path'")
        }
        return filepath
    }

    private fun should_show_entry(p: File): Boolean {
        return p.getName() != "index.xml" &&
                (p.isDirectory() || p.extension.equalsIgnoreCase("xml"))
    }

    private fun collect_childs(p: Path): MutableList<DocumentationNodeRef> {
        var childs: MutableList<DocumentationNodeRef> = ArrayList()
        if (p.toFile().isDirectory()) {
            val files = p.toFile().list()
            files.sort()
            for (child in files) {
                if (should_show_entry(File(p.toFile(), child))) {
                    val childpath = p.resolve(child.replace(".xml", ""))
                    childs.add(DocumentationNodeRef(root_path.relativize(childpath).toString()))
                }
            }
        }
        return childs
    }

    private fun get_filepath(p: Path): Path {
        // Construct the real filesystem path
        if (p.toFile().isDirectory()) {
            // The actual content is within the folder
            return p.resolve("index" + extension)
        } else {
            // Append the .xml suffix if it's a file
            return p.getParent().resolve((p.getFileName().toString() + extension))
        }
    }

    private fun parse_file(filepath: Path): Pair<String, Content> {
        // Read and parse the file
        var title: String
        var content: Content
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
            val p: ParagraphBlock = ParagraphBlock(ParagraphElement())
            p.value.childs.add(TextElement("Invalid document($filepath): $e"))
            content.blocks.add(p)
        }
        return Pair(title, content)
    }

    override fun load(ref: DocumentationNodeRef): DocumentationNode {
        val nodepath: Path = get_nodepath(ref)
        val childs = collect_childs(nodepath)
        val filepath = get_filepath(nodepath)

        if (!filepath.toFile().exists()) {
            val node = DocumentationNode(ref, "FIXME", null)
            node.children = childs
            return node
        }

        var (title: String, content: Content) = parse_file(filepath)

        val node = DocumentationNode(ref, title, content)
        node.children = childs
        node.parent = ref.parent()
        return node
    }
}