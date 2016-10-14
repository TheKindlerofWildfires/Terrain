#version 400 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 textCoord;

out vec4 clipSpace;

uniform mat4 projection;
uniform mat4 modelView;

void main(){
    vec4 mvPos = modelView * vec4(position, 1.0);
    clipSpace = projection*mvPos;
    gl_Position = projection * mvPos;
}


