#version 400 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 textCoord;

out vec4 clipSpace;
out vec3 toCamera;
out vec2 texCoord;
out vec3 norm;
out vec3 mvVertexPos;

uniform mat4 projection;
uniform mat4 modelView;
uniform vec3 cameraPos;

const float tiling = 1.0f;

void main(){
    vec4 mvPos = modelView * vec4(position, 1.0);
    toCamera = normalize(cameraPos-mvPos.xyz);
    norm = normalize(vec3(modelView * vec4(normal,0)));
    mvVertexPos = vec3(mvPos);
    clipSpace = projection*mvPos;
    gl_Position = projection * mvPos;
    texCoord = vec2(textCoord)*tiling;
}

