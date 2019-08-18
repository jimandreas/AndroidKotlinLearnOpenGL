#version 300 es
precision mediump float;

layout (location = 0) in vec3 aPos;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in mat4 aInstanceMatrix;

out vec2 TexCoords;

uniform mat4 projection;
uniform mat4 view;

// hack

uniform mat4 model;

void main()
{
    TexCoords = aTexCoords;
    gl_Position = projection * view * aInstanceMatrix * vec4(aPos, 1.0f);
    // note: Use this one for the non-instanced
    //       iterative rendering for comparison
    //gl_Position = projection * view * model * vec4(aPos, 1.0f);
}