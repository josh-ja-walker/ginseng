package ginseng.core.poly.polylines

import ginseng.core.poly.*

import ginseng.maths.geometry.*


trait Polyline[N <: Int](using ValueOf[N]) extends Poly[N] { require(valueOf[N] >= 2) }

