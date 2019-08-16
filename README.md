AndroidKotlinLearnOpenGL
==========================

history
---------

Implementing some of the excercises from:

https://learnopengl.com/Getting-started/OpenGL

reference:
-----------------

https://github.com/JoeyDeVries/LearnOpenGL

Android Framework
-----------------
The Android application framework is one Activity and multiple Fragments - one for each major section
in the learnopengl "coursework".  The framework also includes modern Android techniques - more
can be learned about these techniques in the excellent Udacity course:

https://classroom.udacity.com/courses/ud9012

- Databinding
- ViewModel with LiveData controls

build requirements
------------------
Android Studio 3.4 or higher

current status
---------------
Just getting started

testing
---------

One sample Junit test added as placeholder

Other references:
------------------

Twitter handle for Joey de Vries
<br>
https://twitter.com/JoeyDeVriez

dependencies:
--------------

glm:<br>
https://github.com/kotlin-graphics/glm


notes:
--------------
Renderer4102InstancingHacking
an experiement with instancing - after difficulties with the
rendering rocks, move back to what works - the 2D colored patches.
Curious that one renders in the center after rendering only 4 off to the side?

This works for hacking the patches based on instance:

    void main()
    {
        fColor = aColor;
        float myfloat = float(gl_InstanceID+1);
        vec2 pos = aPos * myfloat;
        gl_Position = vec4(pos + aOffset, 0.0, 1.0);
    }

