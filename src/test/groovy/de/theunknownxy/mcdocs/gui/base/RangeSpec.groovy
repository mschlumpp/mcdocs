package de.theunknownxy.mcdocs.gui.base

import spock.lang.Specification

public class RangeSpec extends Specification {
    def range = new Range(10f, 30f)

    def "move range's bottom to 20"() {
        when:
        range.moveStop(20)
        then:
        range.start == 0
        range.stop == 20
        range.distance() == 20
    }

    def "move range's top to 30"() {
        when:
        range.moveStart(30)
        then:
        range.start == 30
        range.stop == 50
        range.distance() == 20
    }

    def "set range's top to 0"() {
        when:
        range.start = 0
        then:
        range.stop == 30
        range.distance() == 30
    }

    def "set range's bottom to 20"() {
        when:
        range.stop = 20
        then:
        range.start == 10
        range.distance() == 10
    }
}