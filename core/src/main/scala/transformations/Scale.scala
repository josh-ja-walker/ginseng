package ginseng.core.transformations

import ginseng.maths.linalg.*


trait Scale {
    infix def scale(v: Vector): Scale
}
