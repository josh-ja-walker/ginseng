package ginseng.maths.geometry.matrices

import ginseng.maths.*
import ginseng.maths.angle.*

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.*

import Vec.*
import SqrMat.*


type RotateMat2 = SqrMat[2]

object RotateMat2 {
    def apply(theta: Angle): RotateMat2 = {
        SqrMat(
            Vec2(math.cos(theta.toRadians), math.sin(theta.toRadians)), 
            Vec2(-math.sin(theta.toRadians), math.cos(theta.toRadians))
        )
    }
}


type RotateMat3 = SqrMat[3]

object RotateMat3 {
    // TODO: handle case where axis is not Right/Up/Forward
    def apply(theta: Angle, axis: Vec3): RotateMat3 = {
        axis.normalized.map(math.abs(_).round) match {
            case Vec3(1, 0, 0) => RotateMat3.x(theta) 
            case Vec3(0, 1, 0) => RotateMat3.y(theta) 
            case Vec3(0, 0, 1) | _ => RotateMat3.z(theta) 
        }
    }

    private def x(theta: Angle): RotateMat3 = {
        val id = SqrMat.identity[3]
        id.setMatrix[2, 2](1, 1, RotateMat2(theta))
        id
    }

    // TODO: consolidate constructors into one
    private def y(theta: Angle): RotateMat3 = {
        val p = Vec3(math.cos(theta.toRadians), 0, -math.sin(theta.toRadians))
        val r = Vec3(math.sin(theta.toRadians), 0, math.cos(theta.toRadians))
        SqrMat(p, Vec3.up, r)
    }

    private def z(theta: Angle): RotateMat3 = {
        val id = SqrMat.identity[3]
        id.setMatrix[2, 2](0, 0, RotateMat2(theta))
        id
    }

}


type RotateMat4 = SqrMat[4]

object RotateMat4 {
    def apply(theta: Angle, axis: Vec4): RotateMat4 =
        RotateMat3(theta, axis.take[3]).extend[4]
}
