/**
 * Copyright 2013 Dennis Ippel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
@file:Suppress("unused")
package org.rajawali3d.math.vector

class Vector2 {
    var x: Double = 0.toDouble()
    var y: Double = 0.toDouble()

    constructor() {

    }

    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    fun setAll(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    constructor(vals: Array<String>) {
        x = java.lang.Float.parseFloat(vals[0]).toDouble()
        y = java.lang.Float.parseFloat(vals[1]).toDouble()
    }
}
