package ginseng.core.poly.polylines

import ginseng.core.poly.*


trait Polyline[N <: Int] extends Poly[N] { require(valueOf[N] >= 2) }
