#version 400 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 textCoord;

out vec2 texCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;

uniform mat4 projection;
uniform mat4 modelView;

void main(){
    vec4 mvPos = modelView * vec4(position, 1.0);
    gl_Position = projection * mvPos;
    mvVertexNormal = normalize(modelView * vec4(normal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
	texCoord = vec2(textCoord);
}


