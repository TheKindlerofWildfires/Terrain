#version 400 core
layout (location = 0) in vec3 position;
layout (location = 3) in vec4 model;

out vec4 colour;

uniform vec4 clipPlane;
uniform mat4 view;
uniform mat4 projection;

uniform vec4 color;

void main(){
   	vec3 pos = position*model.w + model.xyz;
    gl_ClipDistance[0] = dot(vec4(pos,1),clipPlane);
    gl_Position = projection * view * vec4(pos,1);
	colour = color;
}