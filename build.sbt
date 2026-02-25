Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "3.8.1"


// Root project aggregating subprojects
lazy val ginseng = project
    .in(file("."))
    .dependsOn(core, maths, renderer)
    .enablePlugins(ScalaNativePlugin, VcpkgNativePlugin)
    .settings(vcpkgDependencies := VcpkgDependencies("glfw3"))
    .settings(openglSettings)


// Note: required to duplicate between projects
// ScalaNative config options required for compiling and linking opengl
lazy val openglSettings: Seq[Def.Setting[_]] = Seq(
    nativeConfig := {
        nativeConfig.value
            .withLinkingOptions(_ :+ "-lshell32")
            .withLinkingOptions(_ ++ vcpkgConfigurator.value.pkgConfig.linkingFlags("glfw3"))
    }
)


// Project containing DSL, primitives, colour, etc.,
lazy val core = project
    .in(file("core"))
    .dependsOn(maths)
    .enablePlugins(ScalaNativePlugin, VcpkgNativePlugin)

// Project containing mathematics helpers for Vectors, Matrices, etc.,
lazy val maths = project
    .in(file("maths"))
    .enablePlugins(ScalaNativePlugin, VcpkgNativePlugin)

// Project containing renderers and OpenGL handlers
lazy val renderer = project
    .in(file("renderer"))
    .dependsOn(core, maths)
    .enablePlugins(ScalaNativePlugin, VcpkgNativePlugin)
    .settings(vcpkgDependencies := VcpkgDependencies("glfw3"))
    .settings(libraryDependencies += "io.github.josh-ja-walker" %%% "opengl-bindings" % "0.1.4")
    .settings(openglSettings)
