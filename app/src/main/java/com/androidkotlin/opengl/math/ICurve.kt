/*
 * Copyright 2013 Dennis Ippel
 * Copyright 2018 Jim Andreas kotlin conversion
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
package com.androidkotlin.opengl.math

/**
 * Copyright 2013 Dennis Ippel
 * Copyright 2013 Jim Andreas kotlin conversion
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

// import com.androidkotlin.opengl.math.Vector3;

class ICurve {

    interface ICurve3D {

        val currentTangent: Vector3

        fun calculatePoint(result: Vector3, t: Double)

        fun setCalculateTangents(calculateTangents: Boolean)
    }

}
