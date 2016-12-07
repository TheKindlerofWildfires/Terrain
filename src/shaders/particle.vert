#version 400 core
layout (location = 0) in vec3 position;
layout (location = 3) in mat4 model;

out vec4 colour;

uniform vec4 clipPlane;
uniform mat4 view;
uniform mat4 projection;

void main(){
   // gl_ClipDistance[0] = dot(model * vec4(position,1),clipPlane);
    gl_Position = projection * view * model * vec4(position,1);
	colour = vec4(model[0][0]);
}