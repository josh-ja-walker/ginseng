val ScalaNativeVersion =
  sys.env.getOrElse("SCALA_NATIVE_VERSION", "0.5.10")

val VcpkgVersion =
  sys.env.getOrElse("SBT_VCPKG_VERSION", "0.0.21")


addSbtPlugin("org.scala-native" % "sbt-scala-native" % ScalaNativeVersion)
addSbtPlugin("com.indoorvivants.vcpkg" % "sbt-vcpkg-native" % VcpkgVersion)
