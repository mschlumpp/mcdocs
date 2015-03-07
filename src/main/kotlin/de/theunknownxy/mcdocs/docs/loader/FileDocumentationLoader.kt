package de.theunknownxy.mcdocs.docs.loader

import de.theunknownxy.mcdocs.docs.DocumentationNodeRef
import de.theunknownxy.mcdocs.docs.DocumentationNode
import java.io.File
import javax.xml.parsers.SAXParserFactory
import java.util.ArrayList

public class FileDocumentationLoader(dir_path: String) : DocumentationLoader {
    private val root_path = File(dir_path)
    private val extension = ".xml"

    override fun load(ref: DocumentationNodeRef): DocumentationNode {
        var filepath: File = File(root_path, ref.path)

        // Check whether the path is valid
        filepath = filepath.getAbsoluteFile()
        if(!root_path.isDescendant(filepath)) {
            throw IllegalArgumentException("The path '" + filepath.toString() + "' is not within '" + root_path.toString())
        }

        var childs: MutableList<DocumentationNodeRef> = ArrayList()
        // Construct the real filesystem path
        if(filepath.isDirectory()) {
            // Search for childs
            for(child in filepath.list()) {
                val child_file = File(child)
                if(child_file.getName() != "index.xml") {
                    childs.add(DocumentationNodeRef(root_path.relativePath(child_file).replace(".xml", "")))
                }
            }

            // The actual content is within the folder
            filepath = File(filepath, "index" + extension)

        } else {
            // Append the .doc suffix if it's a file
            filepath = File(filepath.getParent(), filepath.getName() + extension)
        }

        // Read and parse the file
        val spf = SAXParserFactory.newInstance()
        val parser = spf.newSAXParser()
        val handler = XMLParserHandler()
        parser.parse(filepath, handler)

        val node = DocumentationNode(ref, handler.document.title, handler.document.content!!)
        node.children = childs
        return node
    }
}