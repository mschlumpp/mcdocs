package de.theunknownxy.jgui.layout

sealed trait Policy
case class Expanding()
case class Fixed(value: Float)
case class Ratio(value: Float)
