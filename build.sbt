Global / onChangedBuildSource := ReloadOnSourceChanges


/* Global project settings */
ThisBuild / scalaVersion := "3.8.1"

lazy val ginseng = project
    .in(file("."))
    .enablePlugins(ScalaNativePlugin, VcpkgNativePlugin)
    .settings(
        libraryDependencies  += "io.github.josh-ja-walker" %%% "opengl-bindings" % "0.1.4",

        vcpkgDependencies := VcpkgDependencies("glfw3"),
        nativeConfig := {
            nativeConfig.value
                .withLinkingOptions(_ :+ "-lshell32")
                .withLinkingOptions(_ ++ vcpkgConfigurator.value.pkgConfig.linkingFlags("glfw3"))
        },
    )

