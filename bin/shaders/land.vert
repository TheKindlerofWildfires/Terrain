#version 400 core
layout (location = 0) in vec3 position; // The position variable has attribute position 0
layout (location = 1) in vec3 colour;
layout (location = 2) in vec3 normal;

out vec3 mvVertexNormal;
out vec3 mvVertexPos;
out vec3 vertexColour;

uniform mat4 projection;
uniform mat4 modelView;

uniform vec4 clipPlane = vec4(0,0,-1,1);

void main(){
    vec4 mvPos = modelView * vec4(position, 1.0);
    mvVertexPos = mvPos.xyz;
   
    gl_Position = projection * mvPos;
    gl_ClipDistance[0] = dot(vec4(position,1),clipPlane);
    mvVertexNormal = normalize(modelView * vec4(normal, 0.0)).xyz;
    vertexColour=colour;
}


