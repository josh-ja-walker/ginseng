package ginseng.core.poly.polylines

import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import Vec.*
import Mat.*


case class Loop[N <: Int](positions: Pos*)(using ValueOf[N])
    extends Polyline[N] {

    require(valueOf[N] == positions.length)

}


object Loop {

}
