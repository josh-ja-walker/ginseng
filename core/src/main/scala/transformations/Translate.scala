package ginseng.core.transformations

import ginseng.maths.linalg.*


trait Translate {
    infix def translate(v: Vector): Translate
}
