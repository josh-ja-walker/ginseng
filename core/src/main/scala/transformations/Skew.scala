package ginseng.core.transformations

import ginseng.maths.linalg.vectors.*


trait Skew {
    infix def skew(v: Vec3): Skew
}
