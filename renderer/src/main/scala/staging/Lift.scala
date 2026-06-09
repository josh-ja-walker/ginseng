package ginseng.renderer.staging

import scala.quoted.*

import ginseng.core.colours.*
import ginseng.core.shared.{ Shader as ShaderAST }
import ginseng.core.mesh.MeshAST.*

import ginseng.maths.geometry.*



object Lift {
    
    import scala.quoted.ToExpr


    given PosToExpr: ToExpr[Pos] {
        def apply(p: Pos)(using Quotes): Expr[Pos] = {
            val Pos(x, y, z, w) = p
            '{ Pos ( ${Expr(x)}, ${Expr(y)}, ${Expr(z)}, ${Expr(w)})}
        }
    }

    given DirToExpr: ToExpr[Dir] {
        def apply(d: Dir)(using Quotes): Expr[Dir] = {
            val Dir(x, y, z) = d
            '{ Dir ( ${Expr(x)}, ${Expr(y)}, ${Expr(z)}) }
        }
    }

    given ColourToExpr: ToExpr[Colour] {
        def apply(x: Colour)(using Quotes): Expr[Colour] = {
            val Colour(r, g, b, a) = x
            '{ Colour( ${Expr(r)}, ${Expr(g)}, ${Expr(b)}, ${Expr(a)}) }
        }
    }

    given ShaderToExpr: ToExpr[Colour] => ToExpr[ShaderAST] {
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
    }

    given ToExpr[Primitive] {
        def apply(x: Primitive)(using Quotes): Expr[Primitive] = x match {
            case Point(pos, size) => '{ Point( ${ Expr(pos) }, ${ Expr(size) } ) } 
            // case Direct(a, b, width) => '{ Point( ${ Expr(pos) }, ${ Expr(size) } ) }
            // case Path(positions, width) => '{ Point( ${ Expr(pos) }, ${ Expr(size) } ) }
            // case Loop(positions, width) => '{ Point( ${ Expr() }, ${ Expr(size) } ) }
            case Tri(a, b, c) => '{ Tri( ${ Expr(a) }, ${ Expr(b) }, ${ Expr(c) } ) }
        }
    }

    given ToExpr[Point] {
        def apply(x: Point)(using Quotes): Expr[Point] = 
            '{ Point( ${ Expr(x.pos) }, ${ Expr(x.size) } ) } 
    }

    given ToExpr[Tri] {
        def apply(x: Tri)(using Quotes): Expr[Tri] = 
            '{ Tri( ${ Expr(x.a) }, ${ Expr(x.b) }, ${ Expr(x.c) } ) }
    }

    given RenderInfoToExpr: ToExpr[ShaderAST] => ToExpr[Dir] => ToExpr[ShaderMap] => ToExpr[RenderInfo] {
        def apply(x: RenderInfo)(using Quotes): Expr[RenderInfo] =
            '{ RenderInfo ( ${ Expr(x.shader) }, ${ Expr(x.offset) } ) }
    }

    // given [T <: Primitive] => ToExpr[T] => ToExpr[Batch[T]] {
    //     def apply(x: Batch[T])(using Quotes): Expr[Batch[T]] = 
    //         '{ Batch( ${ Expr(x.shader) }, ${ Expr(x.primitives) } )  }
    // }

    // given ToExpr[Batch[? <: Primitive]] {
    //     def apply(x: Batch[?])(using Quotes): Expr[Batch[?]] = 
    //         '{ Batch( ${ Expr(x.shader) }, ${ Expr(x.primitives) } )  }
    // }

}
