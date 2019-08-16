#version 300 es
precision mediump float;

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aOffset;

out vec3 fColor;

void main()
{
    fColor = aColor;
    float myfloat = float(gl_InstanceID+1);
    vec2 pos = aPos * myfloat;
    gl_Position = vec4(pos + aOffset, -myfloat*0.1, 1.0);
}