package ginseng.renderer.staging
import scala.quoted.*


package object utils {

    extension (code: Iterable[Expr[Unit]]) 
        def sequential(using Quotes): Expr[Unit] = Expr.block(code.toList, '{})
        
}
