#version 400 core

in vec2 texCoord;

out	vec4 fragColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

void main(){
	vec4 reflectColour = texture(reflectionTexture,texCoord);
	vec4 refractColour = texture(refractionTexture,texCoord);
	fragColor = mix(reflectColour,refractColour,.5);
} 

