package ginseng.renderer.staging

object Test {
        
    import scala.quoted.*
    import scala.scalanative.unsafe.*

    import ginseng.core.*
    import ginseng.core.colours.*
    import ginseng.core.shared.*
    import ginseng.core.mesh.*
    import ginseng.core.scene.SceneAST.*
    import ginseng.core.scene.conversion.*
    import ginseng.core.scene.conversion.given

    import ginseng.maths.angle.*
    import ginseng.maths.units.*
    import ginseng.maths.geometry.*

    import ginseng.renderer.staging.*


    def checked(m: Int, n: Int) = {
        val width = Length(2d / m)
        val height = Length(2d / n)

        val black = Shader.Flat(Colours.black)
        val white = Shader.Flat(Colours.white)

        LazyList.tabulate(n) {
            i => LazyList.fill(m)(Rect(width, height))
                .zipWithIndex
                .map((rect, j) => rect.shaded { if (i % 2 == j % 2) then black else white } )
                .reduceRight[Scene]((a,b) => Below(a, b))
        }
        .reduceRight((a, b) => LeftOf(a, b))
        .anchoredTo(Origin, _.aabb(AnchorType.A))
    }

    val tri1Shader = Shader.Tri(Colours.red, Colours.green, Colours.blue)
    val tri2Shader = Shader.Tri(Colours.cyan, Colours.magenta, Colours.yellow)

    val scene = checked(5, 5)
    val mesh = scene.computeMesh

    def renderCode(z: Expr[Zone])(using Quotes): Expr[Unit] = mesh.renderCode(using z)
    inline def render()(using z: Zone): Unit = ${ renderCode('z) }

    // def renderCode(z: Expr[Zone])(using Quotes): Expr[Unit] = '{ println(mesh) }
    // inline def render()(using z: Zone): Unit = ${ renderCode('z) }

}
