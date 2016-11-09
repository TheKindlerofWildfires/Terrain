#version 400 core

in vec4 clipSpace;
in vec2 texCoord;
in vec3 toCamera;
in vec3 norm;

out	vec4 fragColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;

uniform float moveFactor;

const float waveStrength = 0.02;

void main(){
	vec2 ndc = (clipSpace.xy/clipSpace.w)/2 + .5;
	vec2 refractTexCoord = ndc;
	vec2 reflectTexCoord = vec2(ndc.x,-ndc.y);
	
	vec2 totalDistortion = texture(dudvMap,vec2(texCoord.x+moveFactor,texCoord.y)).rg*waveStrength;
	totalDistortion += texture(dudvMap,vec2(texCoord.x-2*moveFactor,texCoord.y+moveFactor+2)).rg*waveStrength;
	
	//distortedTexCoords = texCoord + vec2(distortedTexCoords.x,distortedTexCoords.y+moveFactor);
	//vec2 totalDistortion = (texture(dudvMap,distortedTexCoords).rg *2- 1)*waveStrength;
	
	refractTexCoord+=totalDistortion;
	refractTexCoord=clamp(refractTexCoord,0.001,0.999);
	
	reflectTexCoord+=totalDistortion;
	reflectTexCoord.x=clamp(reflectTexCoord.x,0.001,0.999);
	reflectTexCoord.y=clamp(reflectTexCoord.y,-0.999,-0.001);
	
	vec4 reflectColour = texture(reflectionTexture,reflectTexCoord);
	vec4 refractColour = texture(refractionTexture,refractTexCoord);
	
	float refractiveFactor = max(dot(toCamera,norm),0);
	refractiveFactor = pow(refractiveFactor,.5);
	
//	vec4 normalMapColour = texture(normalMap,distortedTexCoords);
//	vec3 normal = normalize(vec3(normalMapColour.r*2-1,normalMapColour.g*2-1,normalMapColour.b));
	
	fragColor = mix(reflectColour,refractColour,refractiveFactor);
	fragColor = mix(fragColor,vec4(0,0.3,.5,1),.1);
	//fragColor = vec4(totalDistortion,0,1);
} 

