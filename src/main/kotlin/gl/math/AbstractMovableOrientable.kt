package gl.math

import math.linearalgebra.Vector3

abstract class AbstractMovableOrientable
constructor(override var position: Vector3 = Vector3(0.0, 0.0, 10.0),
            override var direction: Vector3 = Vector3(0.0, 0.0, -1.0),
            override var upVector: Vector3 = Vector3(0.0, 1.0, 0.0)) : MovableOrientable {
}
