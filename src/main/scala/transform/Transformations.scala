package ginseng.transform

import ginseng.maths.*


trait Translate {
    infix def translate(v: Vector): Translate
}

trait Rotate {
    def rotate(v: Vector): Rotate
}

trait Skew {
    def skew(v: Vector): Skew
}

trait Scale {
    def scale(v: Vector): Scale
}


trait Freeform extends Translate with Rotate with Skew with Scale