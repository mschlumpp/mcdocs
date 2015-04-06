package de.theunknownxy.mcdocs.gui.base

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasToString
import org.testng.annotations.Test

class PointTest {
    Test fun testEquals() {
        assertThat(Point(10f, 23f), equalTo(Point(10f, 23f)))
    }

    Test fun testToString() {
        assertThat(Point(3f, 25f), hasToString("(3.0|25.0)"))
    }
}

