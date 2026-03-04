package ginseng.maths.linalg

import slash.vector.{Vec as SlashVec}


class Vec[N <: Int](private val vec: SlashVec[N]) 
    extends VectorOps[N, Double, Vec[N]] {

    override def apply(i: Int): Double = vec(i)

    override def unary_- : Vec[N] = new Vec[N](-vec)

    override def +(u: Vec[N]): Vec[N] = new Vec[N](vec + u.vec)
    override def -(u: Vec[N]): Vec[N] = new Vec[N](vec - u.vec)

    override def *(u: Double): Vec[N] = new Vec[N](vec * u)
    override def /(u: Double): Vec[N] = new Vec[N](vec / u)

    override def norm: Double = vec.norm
    override def normalized: Vec[N] = new Vec(vec / vec.norm)

    override infix def dot(u: Vec[N]): Double = vec.dot(u.vec)

}


//TODO: extend a VectorFactory
object Vec {
    // Unknown why ValueOf is needed (summonFrom error)
    def apply[N <: Int](ds: Double*)(using ValueOf[N]): Vec[N] = new Vec(SlashVec(ds.toArray))
}


transparent trait VectorOps[N <: Int, T <: Double | Float, V <: VectorOps[N, T, V]] {

    def apply(i: Int): T

    // Element-wise negation
    def unary_- : V

    // Pairwise addition/subtraction 
    infix def +(n: V): V
    infix def -(n: V): V

    // Scalar multiplication/division
    infix def *(n: T): V
    infix def /(n: T): V
    extension (n: T) {
        infix def *(m: V): V = this * n
    }

    def norm: T
    def normalized: V

    infix def dot(u: V): T

}

