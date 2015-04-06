package de.theunknownxy.mcdocs.docs

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.testng.annotations.Test

public class DocumentationNodeTest {
    val ref = DocumentationNodeRef("modx/tutorial/01tut.xml")

    Test fun testParentRef() {
        assertThat(ref.parent(), equalTo(DocumentationNodeRef("modx/tutorial")))
    }

    Test fun testComponents() {
        val components = ref.components().toArrayList()
        assertThat(components, contains("modx", "tutorial", "01tut.xml"))
    }
}