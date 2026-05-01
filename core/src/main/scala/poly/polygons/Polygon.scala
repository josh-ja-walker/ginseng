package ginseng.core.poly.polygons

import ginseng.core.poly.*


trait Polygon[N <: Int] extends Poly[N] { require(valueOf[N] >= 3) }
