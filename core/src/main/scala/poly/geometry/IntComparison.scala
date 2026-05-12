package ginseng.core.poly.geometry


@scala.annotation.implicitNotFound("${N} < ${M}")
type >=[N <: Int, M <: Int] = (scala.compiletime.ops.int.>=[N, M]) =:= true

@scala.annotation.implicitNotFound("${N} > ${M}")
type <=[N <: Int, M <: Int] = (scala.compiletime.ops.int.<=[N, M]) =:= true
