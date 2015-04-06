package de.theunknownxy.mcdocs.gui.base

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasToString
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

public class RectangleTest {
    var rect = Rectangle(10f, 10f, 20f, 10f)

    BeforeMethod fun init() {
        rect = Rectangle(10f, 10f, 20f, 10f)
    }

    Test fun testXPosition() {
        assertThat(rect.x, equalTo(10f))
    }

    Test fun testYPosition() {
        assertThat(rect.y, equalTo(10f))
    }

    Test fun testContainsPoint() {
        assertThat("contains point", rect.contains(Point(15f, 15f)))
    }

    Test fun testNotContainsPoint() {
        assertThat("doesn't contain point", !rect.contains(Point(0f, 0f)))
    }

    Test fun testTopLeftPoint() {
        assertThat(rect.topleft(), equalTo(Point(10f, 10f)))
    }

    Test fun testBottomRightPoint() {
        assertThat(rect.bottomright(), equalTo(Point(30f, 20f)))
    }

    Test fun testModifyWidth() {
        rect.width = 10f

        assertThat(rect.x2(), equalTo(20f))
        assertThat(rect.bottomright(), equalTo(Point(20f, 20f)))
    }

    Test fun testModifyHeight() {
        rect.height = 20f

        assertThat(rect.bottomright(), equalTo(Point(30f, 30f)))
        assertThat(rect.y2(), equalTo(30f))
    }

    Test fun testToString() {
        assertThat(rect, hasToString("[(10.0|10.0)-(30.0|20.0)]"))
    }
}