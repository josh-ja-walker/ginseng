package ginseng.renderer.staging

import scala.quoted.*

import ginseng.renderer.*

import ginseng.core.colours.*
import ginseng.core.shared.{ Shader as ShaderAST }
import ginseng.core.mesh.MeshAST.*

import ginseng.maths.geometry.*


given liftPos: ToExpr[Pos] with
    def apply(p: Pos)(using Quotes): Expr[Pos] = {
        val Pos(x, y, z, w) = p
        '{ Pos ( ${Expr(x)}, ${Expr(y)}, ${Expr(z)}, ${Expr(w)})}
    }

given liftDir: ToExpr[Dir] with
    def apply(d: Dir)(using Quotes): Expr[Dir] = {
        val Dir(x, y, z) = d
        '{ Dir ( ${Expr(x)}, ${Expr(y)}, ${Expr(z)}) }
    }


given liftColour: ToExpr[Colour] with
    def apply(x: Colour)(using Quotes): Expr[Colour] = {
        val Colour(r, g, b, a) = x
        '{ Colour( ${Expr(r)}, ${Expr(g)}, ${Expr(b)}, ${Expr(a)}) }
    }

given liftShader: ToExpr[ShaderAST] with
    def apply(x: ShaderAST)(using Quotes): Expr[ShaderAST] = x match {
        
        case ShaderAST.Flat(col) => '{ 
            ShaderAST.Flat( ${Expr(col)} ) 
        }

        case ShaderAST.Tri(a, b, c) => '{ 
            ShaderAST.Tri( ${Expr(a)}, ${Expr(b)}, ${Expr(c)} ) 
        }

        case ShaderAST.Interpolate(colours*) => '{ 
            ShaderAST.Interpolate( ${Expr(colours)}* ) 
        }

    }


given RenderInfoToExpr: ToExpr[RenderInfo] with
    def apply(x: RenderInfo)(using Quotes): Expr[RenderInfo] =
        '{ RenderInfo ( ${ Expr(x.shader) }, ${ Expr(x.offset) } ) }



given liftPrimitive: ToExpr[Primitive] with
    def apply(x: Primitive)(using Quotes): Expr[Primitive] = x match {
        case p: Point => liftPoint(p)
        // case Direct(a, b, width) => '{ Point( ${ Expr(pos) }, ${ Expr(size) } ) }
        // case Path(positions, width) => '{ Point( ${ Expr(pos) }, ${ Expr(size) } ) }
        // case Loop(positions, width) => '{ Point( ${ Expr() }, ${ Expr(size) } ) }
        case tri: Tri => liftTri(tri)
    }

given liftPoint: ToExpr[Point] with
    def apply(x: Point)(using Quotes): Expr[Point] = 
        '{ Point( ${ Expr(x.pos) }, ${ Expr(x.size) } ) } 

given liftTri: ToExpr[Tri] with
    def apply(x: Tri)(using Quotes): Expr[Tri] = 
        '{ Tri( ${ Expr(x.a) }, ${ Expr(x.b) }, ${ Expr(x.c) } ) }
