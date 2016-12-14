#version 400 core
#include lighting.lib

in vec3 vertexColour;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out	vec4 fragColor;

void main(){
    vec4 baseColour = vec4(vertexColour,1); 
   
    vec4 lightColour = calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal, mvVertexNormal);

    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += lightColour;
    totalLight = vec4(20);

    fragColor = totalLight*baseColour; 
    
	if ( fog.activeFog == 1 ){ 
		fragColor = calcFog(mvVertexPos, fragColor, fog, ambientLight, directionalLight);
	}
} 

