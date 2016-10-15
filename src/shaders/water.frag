#version 400 core

in vec4 clipSpace;
in vec2 texCoord;

out	vec4 fragColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;

void main(){
	vec2 ndc = (clipSpace.xy/clipSpace.w)/2 + .5;
	vec2 refractTexCoord = ndc;
	vec2 reflectTexCoord = vec2(ndc.x,-ndc.y);
	
	vec2 distortion = texture(dudvMap,vec2(texCoord.x,texCoord.y)).rg * 2 - 1;
	refractTexCoord+=distortion;
	reflectTexCoord+=distortion;
	
	vec4 reflectColour = texture(reflectionTexture,reflectTexCoord);
	vec4 refractColour = texture(refractionTexture,refractTexCoord);
	fragColor = mix(reflectColour,refractColour,.5);
} 

