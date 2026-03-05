package ginseng.core.transformations

import ginseng.maths.linalg.vectors.*


trait Translate {
    infix def translate(v: Vec3): Translate
}
