package ginseng.maths.transformations

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


private[transformations] object TransformMats {

    object TranslateMat:
        def apply(t: Vec[3]): Mat[4, 4] = 
            Mat.identity[4, 4].take[3] :+ (t :+ 1)

    object RotateMat2:
        def apply(theta: Angle): Mat[2, 2] = {
            Mat(
                Vec[2](math.cos(theta.toRadians), math.sin(theta.toRadians)), 
                Vec[2](-math.sin(theta.toRadians), math.cos(theta.toRadians))
            )
        }

    object RotateMat3 {

        // TODO: handle case where axis is not Right/Up/Forward
        def apply(theta: Angle, axis: Vec[3]): Mat[3, 3] = {
            axis.normalized.map(_.abs.toInt) match {
                case Vec[3](1, 0, 0) => RotateMat3.x(theta) 
                case Vec[3](0, 1, 0) => RotateMat3.y(theta) 
                case Vec[3](0, 0, 1) | _ => RotateMat3.z(theta) 
            }
        }

        private def x(theta: Angle): Mat[3, 3] = {
            val id = Mat.identity[3, 3].underlying
            id.setMatrix[2, 2](1, 1, RotateMat2(theta).underlying)
            Mat.fromSlash(id)
        }

        // TODO: consolidate constructors into one
        private def y(theta: Angle): Mat[3, 3] = {
            val p = Vec[3](math.cos(theta.toRadians), 0, -math.sin(theta.toRadians))
            val r = Vec[3](math.sin(theta.toRadians), 0, math.cos(theta.toRadians))
            Mat(p, Vec.up[3], r)
        }

        private def z(theta: Angle): Mat[3, 3] = {
            val id = Mat.identity[3, 3].underlying
            id.setMatrix[2, 2](0, 0, RotateMat2(theta).underlying)
            Mat.fromSlash(id)
        }

    }

    object RotateMat4:
        def apply(theta: Angle, axis: Vec[4]): Mat[4, 4] =
            RotateMat3(theta, axis.take[3]).extend[4]

    object ScaleMat:
        def apply(s: Vec[3]): Mat[4, 4] = {
            val Mat(a, b, c, d) = Mat.identity[4, 4]
            Mat(a * s(0), b * s(1), c * s(2), d)
        }

    object SkewMat {
        // TODO: support creating more generic Skew matrix 
        // i.e., support Z skew and possibly combine constructors

        def x(f: Double): Mat[4, 4] = {
            val Mat(a, b, c, d) = Mat.identity[4, 4]
            Mat(a, Vec[4](f, 1, 0, 0), c, d)
        }

        def y(f: Double): Mat[4, 4] = {
            val Mat(a, b, c, d) = Mat.identity[4, 4]
            Mat(Vec[4](1, f, 0, 0), b, c, d)
        }

    }

    object HouseholderMat:
        // Compute the Householder matrix using normal of reflection plane
        def apply(n: Vec[3]): Mat[4, 4] = {
            val nUnit = n.normalized
            (Mat.identity[3, 3] - 2 * (Mat(nUnit) * nUnit.transpose)).extend[4] 
        }

}