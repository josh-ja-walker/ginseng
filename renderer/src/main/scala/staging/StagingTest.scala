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


    // val scene = checked(5, 5)

    // val scene = Origin.anchors(
    //     Square(1.u).shaded(Shader.Flat(Colours.magenta)).vertex(3).anchors(
    //         Tris.equilateral(1.u)
    //             .shaded(Shader.Tri(Colours.cyan, Colours.magenta, Colours.yellow)),
    //         _.vertex(0)
    //     ),
    //     _.vertex(0)
    // )


    val tri1Shader = Shader.Tri(Colours.red, Colours.green, Colours.blue)
    val tri2Shader = Shader.Tri(Colours.cyan, Colours.magenta, Colours.yellow)

    val scene = 
        // Tetra(1.u)
        // Pyramid(1.u) 
        // Cuboid(0.375.u, 0.5.u, 0.75.u)
        Cube(0.5.u)
            // .rotated(Deg(-30), Dir.up)
            // .rotated(Deg(45), Dir.right)
            // .vertex(VertexIndex.A).anchors(
            //     Tetra(0.5.u)
            //         .rotated(Deg(30), Dir.up)
            //         .vertex(VertexIndex.A).anchors(
            //             Pyramid(0.1.u), 
            //             from =_.vertex(VertexIndex.E)
            //         ),
            //     from = _.aabb(AnchorType.Top)
            // )    
            .shaded(tri1Shader)

    val mesh = scene

    def renderCode(z: Expr[Zone])(using Quotes): Expr[Unit] = mesh.renderCode(using z)
    inline def render()(using z: Zone): Unit = ${ renderCode('z) }

    // def renderCode(z: Expr[Zone])(using Quotes): Expr[Unit] = '{ println(mesh) }
    // inline def render()(using z: Zone): Unit = ${ renderCode('z) }

}
