#version 400 core
layout (location = 0) in vec3 position;
layout (location = 3) in float instance;
out vec4 colour;

void main(){
	gl_Position = vec4(position+gl_InstanceID/6.0,1);
	colour = vec4(instance);
}