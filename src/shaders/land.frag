#version 400 core

in vec3 vertexColour;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out	vec4 fragColor;

struct DirectionalLight
{
    vec3 colour;
    vec3 direction;
    float intensity;
};

struct Material
{
   vec3 colour;
   int useColour;
    float reflectance;
};

struct Fog
{
    int activeFog;
    vec3 colour;
    float density;
    float exponent;
};

uniform vec3 ambientLight;
uniform DirectionalLight directionalLight;
uniform Material material;
uniform vec3 camera_pos;
uniform Fog fog;

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
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColour = vec4(light_colour, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(camera_pos - position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, material.reflectance);
    specColour = light_intensity  * specularFactor * vec4(light_colour, 1.0);

    return (diffuseColour + specColour);
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    vec4 light_colour = calcLightColour(light.colour, light.intensity, position, light.direction, normal);
    return light_colour;
}

void main(){
    vec4 baseColour = vec4(vertexColour,1); 
   
    vec4 lightColour = calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal);

    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += lightColour;

    fragColor = totalLight*baseColour; 
    
	if ( fog.activeFog == 1 ){ 
		fragColor = calcFog(mvVertexPos, fragColor, fog);
	}
} 

