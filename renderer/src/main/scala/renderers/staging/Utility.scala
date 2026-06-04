package ginseng.renderer.renderers.staging

import scala.quoted.*


object Utility {

    extension (code: Iterable[Expr[Unit]]) 
        def sequential(using Quotes): Expr[Unit] = Expr.block(code.toList, '{})
        
}
