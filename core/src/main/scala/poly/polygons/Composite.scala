package ginseng.core.poly.polygons

import scala.compiletime.ops.int.*

import ginseng.core.primitives.*
import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


// TODO: make individual triangles into modifiable components

case class Composite[N <: Int](mat: Mat[4, N * 3])(using ValueOf[N], ValueOf[N * 3])
    extends Primitive 
        with Freeform[Composite[N]] {

    // TODO: uncomment this after testing -- see constructor with triangle
    // require(valueOf[N] > 1) // TODO: is this possible do at compiletime?

    // Index into a component triangle
    def apply(index: Int): Triangle = Triangle(new Mat[4, 3](mat.toSeq.grouped(3).toSeq(index)))


    // Transformations

    override def translate(v: Dir): Composite[N] = 
        Composite[N](TranslateMat(v.take[3]) * mat)

    override def rotate(theta: Angle, around: Pos, axis: Dir): Composite[N] = {
        val translation = TranslateMat(around.take[3])
        val transformation = translation * RotateMat4(theta, axis) * translation.inverse
        Composite[N](transformation * mat)
    }

    override def scale(v: Vec[3]): Composite[N] = Composite[N](ScaleMat(v) * mat)

    override def skew(f: Double, plane: Dir): Composite[N] = {
        val skewMat = plane match {
            case Dir.right => SkewMat.x(f)
            case Dir.up => SkewMat.y(f)
            case Dir.forward => ??? // TODO: support skew in Z direction
        }

        Composite[N](skewMat * mat)
    }

    // Area preserving with unmodified Z axis 
    override def squeeze(f: Double): Composite[N] = {
        val squeezeMat = ScaleMat(Vec[3](f, 1/f, 1))
        Composite[N](squeezeMat * mat)
    }

    // Volume preserving with full X, Y, Z degrees of freedom 
    override def squeeze(f: Vec[2]): Composite[N] = {
        val squeezeMat = ScaleMat(f :+ 1 / (f.x * f.y))
        Composite[N](squeezeMat * mat)
    }

    override def reflect(normal: Dir, point: Pos): Composite[N] = 
        Composite[N](HouseholderMat(normal.take[3]) * mat)

    // TODO: add trait 
    def reposition(p: Pos, anchor: Pos): Composite[N] = 
        Composite[N](TranslateMat((p - anchor).take[3]) * mat)


    // Join with other composites

    // FIXME: mildly worried about the asInstanceOf calls
    // FIXME: also mildly worried about required using ValueOfs...


    def compose(tri: Triangle)(using ValueOf[N + 1], ValueOf[(N * 3) + 3], ValueOf[(N + 1) * 3]) = {
        val compMat: Mat[4, (N * 3) + 3] = mat ++ tri.mat 
        Composite[N + 1](compMat.asInstanceOf[Mat[4, (N + 1) * 3]])
    }

    def compose[M <: Int](comp: Composite[M])(using ValueOf[N + M], ValueOf[M * 3], ValueOf[(N * 3) + (M * 3)], ValueOf[(N + M) * 3]): Composite[N + M] = {
        val compMat: Mat[4, (N * 3) + (M * 3)] = mat ++ comp.mat
        Composite[N + M](compMat.asInstanceOf[Mat[4, (N + M) * 3]])
    }

    // TODO: add more positional groupings and add to trait     
    def rightOf[M <: Int](comp: Composite[M])
        (using ValueOf[N + M], ValueOf[M * 3], ValueOf[(N * 3) + (M * 3)], ValueOf[(N + M) * 3]): Composite[N + M] = {
            
        val (min, max) = AABB
        // move such that comp.min == this.min + this.max.x 
        compose(comp.reposition(min + (Dir.right * max.x), comp.AABB._1))
    }


    // TODO: make cube of points and name vertices
    private val AABB: (Pos, Pos) = {
        val rows = mat.rows.dropRight(1)
            
        val min = new Vec[3](rows.map(_.toSeq.min)) :+ 1
        val max = new Vec[3](rows.map(_.toSeq.max)) :+ 1

        (min.toPos, max.toPos)
    }


}


object Composite {

    // TODO: remove this as not actually a composite - just to avoid
    // having to redefine AABB, rightOf, etc., for testing
    def apply(tri: Triangle): Composite[1] = Composite[1](tri.mat)

    def apply(tri: Triangle, tri2: Triangle): Composite[2] = 
        Composite[2](tri.mat ++ tri2.mat)
    
    def apply(tri: Triangle, tri2: Triangle, tri3: Triangle): Composite[3] = 
        Composite(tri, tri2).compose(tri3)

    // TODO: add other constructors ...

}