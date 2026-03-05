package ginseng.maths.linalg.matrices

import ginseng.maths.*
import ginseng.maths.linalg.*
import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.vectors.Vec.*
import SqrMat.*


type Rotation2 = SqrMat[2]

object Rotation2 {
    def apply[M <: Int](theta: Angle)(using ValueOf[M]): Rotation2 = {
        SqrMat(
            Vec2(math.cos(theta), math.sin(theta)), 
            Vec2(-math.sin(theta), math.cos(theta))
        )
    }
}


type Rotation3 = SqrMat[3]

object Rotation3 {
    def apply[M <: Int](theta: Angle, axis: Vec3): Rotation3 = {
        axis match {
            case Vec3(1, 0, 0) => Rotation3.x(theta) 
            case Vec3(0, 1, 0) => Rotation3.y(theta) 
            case Vec3(0, 0, 1) => Rotation3.z(theta) 
        }
    }

    private def x(theta: Angle): Rotation3 = ???
    private def y(theta: Angle): Rotation3 = ???
    private def z(theta: Angle): Rotation3 = ???
}


type Rotation4 = SqrMat[4]

object Rotation4 {
    def apply[M <: Int](theta: Angle, axis: Vec4): Rotation4 =
        Rotation3(theta, axis.take[3]).extend[4]

    private def x(theta: Angle): Rotation3 = ???
    private def y(theta: Angle): Rotation3 = ???
    private def z(theta: Angle): Rotation3 = ???
}
