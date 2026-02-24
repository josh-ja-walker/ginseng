import com.indoorvivants.vcpkg.sbtplugin
Global / onChangedBuildSource := ReloadOnSourceChanges


/* Global project settings */
ThisBuild / scalaVersion := "3.8.1"

lazy val ginseng = project
    .in(file("."))
    .dependsOn(core, maths, renderer)
    .enablePlugins(commonPlugins*)
    .enablePlugins(VcpkgNativePlugin)
    .aggregate(core, maths, renderer)
    .settings(nativeSettings)


lazy val commonPlugins = Seq(ScalaNativePlugin)

lazy val commonSettings = Seq()
lazy val nativeSettings = Seq(
    vcpkgDependencies := VcpkgDependencies("glfw3"),
    nativeConfig := {
        nativeConfig.value
            .withLinkingOptions(_ :+ "-lshell32")
            .withLinkingOptions(_ ++ vcpkgConfigurator.value.pkgConfig.linkingFlags("glfw3"))
    },
)


lazy val core = project
    .in(file("core"))
    .dependsOn(maths)
    .enablePlugins(commonPlugins*)
    .settings(commonSettings)

lazy val maths = project
    .in(file("maths"))
    .enablePlugins(commonPlugins*)
    .settings(commonSettings)
    
lazy val renderer = project
    .in(file("renderer"))
    .dependsOn(core, maths)
    .enablePlugins(commonPlugins*)
    .enablePlugins(VcpkgNativePlugin)
    .settings(
        libraryDependencies  += "io.github.josh-ja-walker" %%% "opengl-bindings" % "0.1.4"
    )
    .settings(commonSettings)
    .settings(nativeSettings)

