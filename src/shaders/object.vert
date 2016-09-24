#version 400 core
layout (location = 0) in vec3 position; // The position variable has attribute position 0
layout	(location=1) in	vec2 texCoord;


out	vec2 outTexCoord;

uniform mat4 pv;

void main(){
    gl_Position = pv*vec4(position.x,position.y,position.z, 1.0); // See how we directly give a vec3 to vec4's constructor
   	outTexCoord	= texCoord; 
}