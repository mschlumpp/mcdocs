package de.theunknownxy.mcdocs.gui.base

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

public class RangeTest {
    var range = Range(0f, 0f)

    BeforeMethod fun init() {
        range = Range(10f, 30f)
    }

    Test fun testMoveStop() {
        range.moveStop(20f)

        assertThat(range, equalTo(Range(0f, 20f)))
        assertThat(range.distance(), equalTo(20f))
    }

    Test fun testMoveStart() {
        range.moveStart(30f)

        assertThat(range, equalTo(Range(30f, 50f)))
        assertThat(range.distance(), equalTo(20f))
    }

    Test fun testSetStart() {
        range.start = 0f

        assertThat(range, equalTo(Range(0f, 30f)))
        assertThat(range.distance(), equalTo(30f))
    }

    Test fun testSetStop() {
        range.stop = 20f

        assertThat(range, equalTo(Range(10f, 20f)))
        assertThat(range.distance(), equalTo(10f))
    }
}