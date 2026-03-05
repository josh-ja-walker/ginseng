package ginseng.core.transformations

import ginseng.maths.linalg.vectors.*


trait Scale {
    infix def scale(v: Vec3): Scale
}
