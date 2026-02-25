# AndroidKotlinLearnOpenGL

Updated to: Android Studio Otter 3 Feature Drop | 2025.2.3

## Building

Written in Kotlin. **Build the project before looking at the code closely in Android Studio.** Both the Data Binding and Navigator frameworks generate code that is called directly by the project — these calls will show as undefined before the project is built.

## Emulators and Devices

When displaying in an emulator, enable the higher level of OpenGL ES by adding the following to `~/.android/advancedFeatures.ini`:

```
GLESDynamicVersion = on
```

For more information on this emulator configuration see the MISC.md file.

## Current Status

Exercises are operational through Instancing. Basic camera manipulation is included and is implemented only for movement in the x and z direction. The camera movement leverages the Quaternion implementation of Rajawali.

## Architecture Diagram

![Architecture](Screenshots/OpenGLwithLiveDataArchitecture.png)

## Sample Screenshot

![Camera Exercise](Screenshots/CameraExercise.png)

## Exercise Descriptions

The project implements the following exercises in descending complexity, adapted for Android, OpenGL ES 3+, and implemented in Kotlin. It also borrows from [camera.h](https://learnopengl.com/code_viewer_gh.php?code=includes/learnopengl/camera.h).

### Renderer4103AdvancedAsteroidsInstanced

- Tutorial: https://learnopengl.com/Advanced-OpenGL/Instancing
- Code: https://learnopengl.com/code_viewer_gh.php?code=src/4.advanced_opengl/10.3.asteroids_instanced/asteroids_instanced.cpp

Uses a Matrix4 instance array and the associated hacks to set up 4 vector4 attributes to import the Matrix4 into the shader. TODO: replace this with a modern buffer if that works in the emulator and phone.

### Renderer4102InstancingHacking

No tutorial — this is a variation on `Renderer4101InstancingQuads` to add OBJ-based rendering with an instanced renderer and work through the shader mechanics.

This works for hacking the patches based on instance:

```glsl
void main()
{
    fColor = aColor;
    float myfloat = float(gl_InstanceID+1);
    vec2 pos = aPos * myfloat;
    gl_Position = vec4(pos + aOffset, 0.0, 1.0);
}
```

### Renderer480AdvancedGlslUBO

- Tutorial: https://learnopengl.com/Advanced-OpenGL/Advanced-GLSL
- Code: https://learnopengl.com/code_viewer_gh.php?code=src/4.advanced_opengl/8.advanced_glsl_ubo/advanced_glsl_ubo.cpp

Use of a simple Uniform Buffer Object to pass the projection and view matrices to a common shader:

```glsl
layout (std140) uniform Matrices
{
    mat4 projection;
    mat4 view;
};
```

### Renderer242LightingMapsPlusCubeObject

No tutorial — a variation on `Renderer242LightingMapsSpecular` that adds an OBJ-based cube to test integration of OBJ rendering into the scene.

### Renderer242LightingMapsSpecular

- Tutorial: https://learnopengl.com/Lighting/Lighting-maps
- Code: https://learnopengl.com/code_viewer_gh.php?code=src/2.lighting/4.2.lighting_maps_specular_map/lighting_maps_specular.cpp

Lighting combined with textures.

### Renderer174Camera

- Tutorial: https://learnopengl.com/Getting-started/Camera
- Code: https://learnopengl.com/code_viewer_gh.php?code=src/1.getting_started/7.4.camera_class/camera_class.cpp

### Renderer163CoordinateSystems

- Tutorial: https://learnopengl.com/Getting-started/Coordinate-Systems
- Code: https://learnopengl.com/code_viewer_gh.php?code=src/1.getting_started/6.3.coordinate_systems_multiple/coordinate_systems_multiple.cpp

### Renderer121HelloTriangle

- Tutorial: https://learnopengl.com/Getting-started/Hello-Triangle
- Code: https://learnopengl.com/code_viewer_gh.php?code=src/1.getting_started/2.1.hello_triangle/hello_triangle.cpp

The "hello world" basics adapted for Android, OpenGL ES, and Kotlin.

### GoogleSampleRenderer

- Tutorial: https://developer.android.com/training/graphics/opengl

Android OpenGL triangle and square sample — the basic "Hello OpenGL" introduction, included for comparison to the learnopengl.com code style.

## Project Notes

### Imported Content

The project does not use any supporting graphics libraries aside from what is built into the Android Framework. Math support is provided by the Rajawali Math modules, converted to Kotlin and located in the `java/org/rajawali3d` subtree. See also the [Rajawali GitHub repo](https://github.com/Rajawali/Rajawali).
