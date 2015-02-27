package de.theunknownxy.jgui.layout

sealed trait Policy
case class Expanding(importance: Float)
case class Fixed(value: Float)
