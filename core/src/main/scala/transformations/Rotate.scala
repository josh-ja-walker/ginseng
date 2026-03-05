package ginseng.core.transformations

import ginseng.maths.*
import ginseng.maths.geometry.*


trait Rotate {
    infix def rotate(theta: Angle, around: Pos, axis: Dir): Rotate
}
