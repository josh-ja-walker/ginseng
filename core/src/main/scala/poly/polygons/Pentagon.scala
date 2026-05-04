package ginseng.core.poly.polygons

import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.*
import ginseng.maths.geometry.given

type Pentagon = RegPolygon[5]

object Pentagon {

    type A = 0; type B = 1; type C = 2; type D = 3; type E = 4

    extension (p: Pentagon) {
        // helpers for referencing edges
        def ab: Edge[Pentagon] = p.edge[Pentagon.A, Pentagon.B]
        def bc: Edge[Pentagon] = p.edge[Pentagon.B, Pentagon.C]
        def cd: Edge[Pentagon] = p.edge[Pentagon.C, Pentagon.D]
        def de: Edge[Pentagon] = p.edge[Pentagon.D, Pentagon.E]
        def ea: Edge[Pentagon] = p.edge[Pentagon.E, Pentagon.A]

        // helpers for referencing angles
        def alpha: Arc[Pentagon]   = p.angle[Pentagon.E, Pentagon.A, Pentagon.B]
        def beta:  Arc[Pentagon]   = p.angle[Pentagon.A, Pentagon.B, Pentagon.C]
        def gamma: Arc[Pentagon]   = p.angle[Pentagon.B, Pentagon.C, Pentagon.D]
        def delta: Arc[Pentagon]   = p.angle[Pentagon.C, Pentagon.D, Pentagon.E]
        def epsilon: Arc[Pentagon] = p.angle[Pentagon.D, Pentagon.E, Pentagon.A]
    }

}
