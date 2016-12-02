#version 400 core
#include lighting.lib

in vec4 clipSpace;
in vec2 texCoord;
in vec3 norm;
in vec3 mvVertexPos;

out	vec4 fragColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;


uniform float moveFactor;

uniform float waveStrength;
uniform float normalStrength;

uniform float fresnelPower;

uniform float near; 
uniform float far;

uniform float waterClarity;
uniform float maxDistortion;
uniform vec4 waterColour;

void main(){
	vec2 ndc = (clipSpace.xy/clipSpace.w)/2 + .5;
	vec2 refractTexCoord = ndc;
	vec2 reflectTexCoord = vec2(ndc.x,-ndc.y);
	
	float depth = texture(depthMap, refractTexCoord).r;
	float floorDistance = 2*near*far/(far + near - (2*depth - 1) * (far-near));
	
	depth = gl_FragCoord.z;
	float waterDistance = 2*near*far/(far + near - (2*depth - 1) * (far-near));
	
	float waterDepth = floorDistance-waterDistance;
	
	vec2 totalDistortion = texture(dudvMap,vec2(texCoord.x+moveFactor,texCoord.y)).rg;
	totalDistortion += texture(dudvMap,vec2(texCoord.x-2*moveFactor,texCoord.y+moveFactor+2)).rg;
	totalDistortion *= waveStrength*waterDepth;
	totalDistortion = clamp(totalDistortion,-maxDistortion,maxDistortion);
	
	refractTexCoord+=totalDistortion;
	refractTexCoord=clamp(refractTexCoord,0.001,0.999);
	
	reflectTexCoord+=totalDistortion;
	reflectTexCoord.x=clamp(reflectTexCoord.x,0.001,0.999);
	reflectTexCoord.y=clamp(reflectTexCoord.y,-0.999,-0.001);
	
	vec4 reflectColour = texture(reflectionTexture,reflectTexCoord);
	vec4 refractColour = texture(refractionTexture,refractTexCoord);
	
	vec3 toCamera = normalize(-mvVertexPos);
	float refractiveFactor = max(dot(toCamera,norm),0);
	refractiveFactor = pow(refractiveFactor,fresnelPower);
	
	vec3 normal1 = texture(normalMap,vec2(texCoord.x+moveFactor,texCoord.y)).xyz;
	normal1.x = normal1.x*2-1;
	normal1.y = normal1.y*2-1;
	vec3 normal2 = texture(normalMap,vec2(texCoord.x-2*moveFactor,texCoord.y+moveFactor+2)).xyz;
	normal2.x = normal2.x*2-1;
	normal2.y = normal2.y*2-1;
	vec3 normal = normal1 + normal2;
	normal.x *= normalStrength;
	normal.y *= normalStrength;
	normal = normalize(normal);

	refractColour = mix(refractColour,waterColour,waterDepth/waterClarity);
	reflectColour = mix(reflectColour,waterColour,1/waterClarity);
	
	fragColor = mix(reflectColour,refractColour,refractiveFactor);

 	vec4 lightColour = calcDirectionalLight(directionalLight, mvVertexPos, norm, normal);

    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += lightColour;

    fragColor = totalLight*fragColor;
    
	if ( fog.activeFog == 1 ){ 
		fragColor = calcFog(mvVertexPos, fragColor, fog, ambientLight, directionalLight);
	}
	
//	fragColor = vec4(refractiveFactor);
} 

