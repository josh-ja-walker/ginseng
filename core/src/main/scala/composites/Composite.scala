package ginseng.core.composites

import scala.compiletime.ops.int.*

import ginseng.core.primitives.*
import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.*

import ginseng.maths.geometry.vectors.*
import ginseng.maths.geometry.matrices.*

import Vec.*
import Mat.*
import Pos.*


// TODO: make individual triangles into modifiable components

case class Composite[N <: Int](mat: Mat[4, N * 3])(using ValueOf[N], ValueOf[N * 3])
    extends Primitive 
        with Freeform[Composite[N]] {

    // TODO: uncomment this after testing -- see constructor with triangle
    // require(valueOf[N] > 1) // TODO: is this possible do at compiletime?

    // Index into a component triangle
    def apply(index: Int): Triangle = Triangle(mat.subMatrix[4, 3](0, index))


    // Transformations

    override def translate(v: Dir): Composite[N] = 
        Composite[N](TranslateMat(v) * mat)

    override def rotate(theta: Angle, around: Pos, axis: Dir): Composite[N] = {
        val translation = TranslateMat(around)
        val transformation = translation * RotateMat4(theta, axis) * translation.inverse
        Composite[N](transformation * mat)
    }

    override def scale(v: Vec3): Composite[N] = 
        Composite[N](ScaleMat4(v) * mat)

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
        val squeezeMat = ScaleMat4(Vec3(f, 1/f, 1))
        Composite[N](squeezeMat * mat)
    }

    // Volume preserving with full X, Y, Z degrees of freedom 
    override def squeeze(f: Vec2): Composite[N] = {
        val squeezeMat = ScaleMat4(f :+ 1 / (f.x * f.y))
        Composite[N](squeezeMat * mat)
    }

    override def reflect(normal: Dir, point: Pos): Composite[N] = 
        Composite[N](HouseholderMat(normal.take[3]) * mat)

    // TODO: add trait 
    def reposition(p: Pos, anchor: Pos): Composite[N] = 
        Composite[N](TranslateMat(p - anchor) * mat)


    // Join with other composites

    // FIXME: mildly worried about the asInstanceOf calls
    // FIXME: also mildly worried about required using ValueOfs...


    def compose(tri: Triangle)(using ValueOf[N + 1], ValueOf[(N * 3) + 3], ValueOf[(N + 1) * 3]) = {
        val compMat: Mat[4, (N * 3) + 3] = mat.concatenateColumns[3](tri.mat) 
        Composite[N + 1](compMat.asInstanceOf[Mat[4, (N + 1) * 3]])
    }

    def compose[M <: Int](comp: Composite[M])(using ValueOf[N + M], ValueOf[M * 3], ValueOf[(N * 3) + (M * 3)], ValueOf[(N + M) * 3]): Composite[N + M] = {
        val compMat: Mat[4, (N * 3) + (M * 3)] = mat.concatenateColumns[M * 3](comp.mat)
        Composite[N + M](compMat.asInstanceOf[Mat[4, (N + M) * 3]])
    }

    
    def rightOf[M <: Int](comp: Composite[M])
        (using ValueOf[N + M], ValueOf[M * 3], ValueOf[(N * 3) + (M * 3)], ValueOf[(N + M) * 3]): Composite[N + M] = {
            
        val (min, max) = AABB
        // move such that comp.min == this.min + this.max.x 
        compose(comp.reposition(min + (Dir.right * max.x), comp.AABB._1))
    }


    // TODO: make cube of points and name vertices
    private val AABB: (Pos, Pos) = {
        val rows = mat.rowVectors.toSeq
            .dropRight(1)
            .map(_.asNativeArray)
            
        val min: Pos = Vec[3](rows.map(_.min).toSeq*) :+ 1
        val max: Pos = Vec[3](rows.map(_.max).toSeq*) :+ 1

        (min, max)
    }


}


object Composite {

    // TODO: remove this as not actually a composite - just to avoid
    // having to redefine AABB, rightOf, etc., for testing
    def apply(tri: Triangle): Composite[1] = Composite[1](tri.mat)

    def apply(tri: Triangle, tri2: Triangle): Composite[2] = 
        Composite[2](tri.mat.concatColumns(tri2.mat))
    
    def apply(tri: Triangle, tri2: Triangle, tri3: Triangle): Composite[3] = 
        Composite(tri, tri2).compose(tri3)

    // TODO: add other constructors ...

}