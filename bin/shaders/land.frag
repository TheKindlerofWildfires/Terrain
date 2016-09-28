#version 400 core

in vec3 vertexColour;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out	vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 colour;
    vec3 position; // Light position is assumed to be in view coordinates
    float intensity;
    Attenuation att;
};

uniform vec3 ambientLight;
uniform float specularPower;
uniform PointLight pointLight;
uniform vec3 camera_pos;

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    vec3 light_direction = light.position - position;
    vec3 to_light_source  = normalize(light_direction);
    float diffuseFactor = max(dot(normal, to_light_source ), 0.0);
    diffuseColour = vec4(light.colour, 1.0) * light.intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_source = -to_light_source;
    vec3 reflected_light = normalize(reflect(from_light_source, normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = specularFactor * .8f * vec4(light.colour, 1.0);

    // Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
        light.att.exponent * distance * distance;
    return (diffuseColour + specColour) / attenuationInv;
}

void main(){
    vec4 baseColour = vec4(vertexColour,1); 
   
    vec4 lightColour = calcPointLight(pointLight, mvVertexPos, mvVertexNormal); 

    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += lightColour;

    fragColor = baseColour * totalLight; 
} 

