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

struct DirectionalLight
{
    vec3 colour;
    vec3 direction;
    float intensity;
};

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;

uniform Fog fog;
uniform DirectionalLight directionalLight;
uniform vec3 ambientLight;
uniform float reflectance;

uniform vec3 camera_pos;

uniform float moveFactor;

const float waveStrength = 0.02;
const float normalStrength = 10;

const float near = .1; //perspective matrix
const float far = 300;

const float depthDistortionModifier = 10;
const float depthBlendModifier = 10;

const vec4 waterColour = vec4(0,0.3,.5,1);

vec4 calcFog(vec3 pos, vec4 colour, Fog fog)
{
    float distance = length(pos);
    float fogFactor = 1.0 / exp( pow((distance * fog.density),fog.exponent));
    fogFactor = clamp( fogFactor, 0.0, 1.0 );

    vec3 resultColour = mix(fog.colour, colour.xyz, fogFactor);
    return vec4(resultColour.xyz, 1);
}

vec4 calcLightColour(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(norm, to_light_dir), 0.0);
    diffuseColour = vec4(light_colour, 1.0) * light_intensity * diffuseFactor;

    // Specular Light DOESN"T FUCKING WORK
    vec3 camera_direction = normalize(camera_pos - position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, reflectance);
    specColour = light_intensity  * specularFactor * vec4(light_colour, 1.0);

    return (diffuseColour);
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    vec4 light_colour = calcLightColour(light.colour, light.intensity, position, light.direction, normal);
    return light_colour;
}

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
	totalDistortion *= waveStrength*(waterDepth/depthDistortionModifier);
	
	//vec2 distortedTexCoords = texture(dudvMap, vec2(texCoord.x+moveFactor,texCoord.y)).rg*waveStrength;
	//distortedTexCoords += vec2(texCoord.x-2*moveFactor,texCoord.y+moveFactor+2);
	//vec2 totalDistortion = (texture(dudvMap,distortedTexCoords).rg * 2 - 1) * waveStrength;
	
	refractTexCoord+=totalDistortion;
	refractTexCoord=clamp(refractTexCoord,0.001,0.999);
	
	reflectTexCoord+=totalDistortion;
	reflectTexCoord.x=clamp(reflectTexCoord.x,0.001,0.999);
	reflectTexCoord.y=clamp(reflectTexCoord.y,-0.999,-0.001);
	
	vec4 reflectColour = texture(reflectionTexture,reflectTexCoord);
	vec4 refractColour = texture(refractionTexture,refractTexCoord);
	
	float refractiveFactor = max(dot(toCamera,norm),0);
	refractiveFactor = pow(refractiveFactor,.5);
	
	vec4 normalColour = texture(normalMap, totalDistortion + ndc);
	vec3 normal = vec3((normalColour.r*2-1)*normalStrength, (normalColour.g*2-1)*normalStrength,normalColour.b);
	normal = normalize(normal);
	
	refractColour = mix(refractColour,waterColour,waterDepth/20);
	
	fragColor = mix(reflectColour,refractColour,refractiveFactor);
	fragColor = mix(fragColor,vec4(0,0.3,.5,1),.1);


 	vec4 lightColour = calcDirectionalLight(directionalLight, mvVertexPos, normal);

    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += lightColour;

    fragColor = totalLight*fragColor; 
    
	if ( fog.activeFog == 1 ){ 
		fragColor = calcFog(mvVertexPos, fragColor, fog);
	}
	
	//fragColor = refractColour;
} 

