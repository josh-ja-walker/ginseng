package ginseng.renderer

import scala.scalanative.unsafe.*

import ginseng.renderer.shaders.*

import ginseng.core.shared.{ Shader as ShaderAST }
import ginseng.core.mesh.MeshAST.*

import ginseng.maths.geometry.*


package object utils {

    // Construct shader from AST shader information
    extension (shader: ShaderAST) def create(using Zone): ShaderProg = shader match {
        case ShaderAST.Flat(col) => Shaders.flatShader(col)
        case ShaderAST.Tri(a, b, c) => Shaders.interpolateShader(a, b, c)
        case ShaderAST.Interpolate(colours*) => Shaders.interpolateShader(colours*)
    }


    // Apply offset to primitives
    extension (mesh: Primitive) def offsetBy(offset: Dir): Primitive = mesh match {

        // TODO: change to use translation transformation
            // case p: Primitive => p.translate(offset)
            
        case Point(pos, size) => Point(pos + offset, size)
        
        case Direct(a, b, width) => Direct(a + offset, b + offset, width)

        // FIXME: requires using ValueOf[N]
        // case Path(positions, width) => Path(positions.map(_ + offset), width)
        // case Loop(positions, width) => Loop(positions.map(_ + offset), width)
        
        case Tri(a, b, c) => Tri(a + offset, b + offset, c + offset)

    }

}

