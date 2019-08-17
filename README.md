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


more notes:
-------------

vec2 instvec = vec2(gl_InstanceID);
    vec2 pos = aPos * instvec;


2019-08-16 08:04:07.196 6059-6100/com.androidkotlin.opengl E/Shader: Error compiling shader (10.1.instancingHack.vs): ERROR: 0:13: '/' :  wrong operand types  no operation '/' exists that takes a left-hand operand of type 'InstanceID highp int' and a right operand of type 'const float' (or there is no acceptable conversion)
    ERROR: 0:13: '*' :  wrong operand types  no operation '*' exists that takes a left-hand operand of type 'in mediump 2-component vector of float' and a right operand of type 'InstanceID highp int' (or there is no acceptable conversion)

void main()
{
    fColor = aColor;
    vec2 pos = aPos * (gl_InstanceID / 100.0);
    gl_Position = vec4(pos + aOffset, 0.0, 1.0);
}




descriptions
=============

Renderer480AdvancedGlslUBO
--------------------------

Tutorial URL: https://learnopengl.com/Advanced-OpenGL/Advanced-GLSL

Code link URL: https://learnopengl.com/code_viewer_gh.php?code=src/4.advanced_opengl/8.advanced_glsl_ubo/advanced_glsl_ubo.cpp

Use of simple Uniform Buffer Object  - in this case to pass in the
projection and view matrices to the common shader.

    layout (std140) uniform Matrices
    {
        mat4 projection;
        mat4 view;
    };

Notes:  code needs further cleanup as I left parts of the instancing project in the
original for use in debugging.   This should get stripped out at some point.

GoogleSampleRenderer
---------------------

Android OpenGL Triangle and Square sample

Tutorial URL:  https://developer.android.com/training/graphics/opengl

This is the basic "Hello Opengl" introduction - just the basics.   It is included for
comparisons to code styling to the learnopengl methodology.
