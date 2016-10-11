#version 400 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 textCoord;

out vec2 texCoord;

uniform mat4 projection;
uniform mat4 modelView;

uniform vec4 plane;

void main(){
    vec4 mvPos = modelView * vec4(position, 1.0);
    gl_Position = projection * mvPos;
    gl_ClipDistance[0] = dot(position, plane);
	texCoord = vec2(textCoord);
}


