package ginseng.core.poly.polygons

import ginseng.core.poly.*


trait Polygon[N <: Int](using ValueOf[N]) extends Poly[N] { require(valueOf[N] >= 3) }
