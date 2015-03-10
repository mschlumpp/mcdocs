package de.theunknownxy.mcdocs.gui.base

import spock.lang.Specification

class RectangleSpec extends Specification {
    def rect = new Rectangle(10, 10, 20, 10)

    def "x position"() {
        expect:
        rect.x == 10
    }

    def "y position"() {
        expect:
        rect.y == 10
    }

    def "contains a point"() {
        expect:
        rect.contains(new Point(15, 15))
    }

    def "doesn't contain a point"() {
        expect:
        !rect.contains(new Point(0, 0))
    }

    def "topleft point"() {
        expect:
        rect.topleft() == new Point(10, 10)
    }

    def "bottomright point"() {
        expect:
        rect.bottomright() == new Point(30, 20)
    }

    def "modify width"() {
        expect:
        rect.width == 20
        when:
        rect.width = 10
        then:
        rect.width == 10
        rect.bottomright() == new Point(20, 20)
    }

    def "modify height"() {
        expect:
        rect.height == 10
        when:
        rect.height = 20
        then:
        rect.height == 20
        rect.bottomright() == new Point(30, 30)
    }
}