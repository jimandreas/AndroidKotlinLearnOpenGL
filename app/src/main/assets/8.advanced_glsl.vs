#version 300 es
precision mediump float;

layout (location = 0) in vec3 aPos;

layout (std140) uniform Matrices
{
    mat4 projection;
    mat4 view;
};
uniform mat4 model;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    //gl_Position = model * vec4(aPos, 1.0);  // (debugging)

}