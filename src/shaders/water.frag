#version 400 core

in vec4 clipSpace;
in vec2 texCoord;
in vec3 toCamera;
in vec3 norm;
in vec3 mvVertexPos;

out	vec4 fragColor;

struct Fog
{
    int activeFog;
    vec3 colour;
    float density;
    float exponent;
};

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform Fog fog;

uniform float moveFactor;

const float waveStrength = 0.02;

vec4 calcFog(vec3 pos, vec4 colour, Fog fog)
{
    float distance = length(pos);
    float fogFactor = 1.0 / exp( pow((distance * fog.density),fog.exponent));
    fogFactor = clamp( fogFactor, 0.0, 1.0 );

    vec3 resultColour = mix(fog.colour, colour.xyz, fogFactor);
    return vec4(resultColour.xyz, 1);
}

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

	if ( fog.activeFog == 1 ){ 
		fragColor = calcFog(mvVertexPos, fragColor, fog);
	}
} 

