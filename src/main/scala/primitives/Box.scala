package ginseng.primitives

import ginseng.graphics.*
import ginseng.transform.*
import ginseng.maths.* 


case class Box(private val topLeft: Point, private val topRight: Point, private val bottomLeft: Point, private val bottomRight: Point) extends Primitive with Freeform
{
    // Trait implementations
    def recolor(colour: Colour): Primitive = ???

    // Freeform allows all translations
    def translate(vector: Vector): Translate = ???
    def rotate(vector: Vector): Rotate = ???
    def skew(vector: Vector): Skew = ???
    def scale(vector: Vector): Scale = ???


    // TODO: decide on convention for vertices

    // TODO: create new box with line transformations to ensure pure functions
    // however maintain ability to specify transformation for trapezium
    // e.g., can transform top with scale 0.5x for trapezium 
    // = Box

    // reference outlines of box
    def top: Line = ???
    def bottom: Line = ???
    def left: Line = ???
    def right: Line = ???
}


object Box {
    
    // TODO: provide factory method for centering at point? 
    // or transformation function to center box at point p
    // need factory method for placing box of size x with p1 = p
    // these are same function signatures

    // Box with sides length x with bottom left vertex at point p
    def apply(p1: Point, size: Float): Box = ???

    // Box with sides length x centered at point
    def centered(center: Point, size: Float): Box = ???


    // TODO: should boxes by default be centered at origin, or p1 = origin??
    def apply(size: Float): Box = ???

    def unital: Box = Box(1)

    //TODO: bounding box construction
    def apply(left: Double, top: Double, right: Double, bottom: Double): Box = ???
}

