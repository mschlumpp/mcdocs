package de.theunknownxy.mcdocs.docs

import Utils
import de.theunknownxy.mcdocs.docs.loader.DocumentationLoader
import mockit.Mocked
import mockit.StrictExpectations
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.testng.annotations.Test

public class DocumentationBackendTest {
    Mocked var loader: DocumentationLoader = Utils.uninitialized()

    Test fun testNavigate() {
        val backend = DocumentationBackend(loader)

        object : StrictExpectations() {init {
            loader.load(DocumentationNodeRef("mcdocs/hello"))
            returns(DocumentationNode(DocumentationNodeRef("mcdocs/hello"), "title", null))
        }
        }

        backend.navigate(DocumentationNodeRef("mcdocs/hello"))
    }

    Test fun testGetters() {
        val backend = DocumentationBackend(loader)
        val test_ref = DocumentationNodeRef("mcdocs/test")
        val test_title = "TestTitle"
        val test_content = Content()

        object : StrictExpectations() {init {
            loader.load(test_ref)
            returns(DocumentationNode(test_ref, test_title, test_content))
        }
        }

        val returned_content = backend.getContent(test_ref)
        val returned_title = backend.getTitle(test_ref)

        assertThat(returned_content, equalTo(test_content))
        assertThat(returned_title, equalTo(test_title))
    }

    Test fun testChildren() {
        val backend = DocumentationBackend(loader)

        val test_b_ref = DocumentationNodeRef("mcdocs/hello")
        val test_b_title = "Child"

        val test_a_ref = test_b_ref.parent()
        val test_a_title = "Parent"
        val test_a_node = DocumentationNode(test_a_ref, test_a_title, null)
        test_a_node.children.add(test_b_ref)

        object : StrictExpectations() {init {
            loader.load(test_a_ref)
            returns(test_a_node)

            loader.load(test_b_ref)
            returns(DocumentationNode(test_b_ref, test_b_title, null))
        }
        }

        assertThat(backend.getChildren(test_a_ref), contains(test_b_ref))
        assertThat(backend.getTitle(test_a_ref), equalTo(test_a_title))
        assertThat(backend.getTitle(test_b_ref), equalTo(test_b_title))
    }
}