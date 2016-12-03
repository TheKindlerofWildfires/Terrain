#version 400 core
#include lighting.lib

in vec2 texCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out	vec4 fragColor;


uniform sampler2D texture_sampler;

void main(){
   vec4 baseColour; 
    if ( material.useColour == 1 ) {
        baseColour = vec4(material.colour, 1);
    }
    else {
        baseColour = texture(texture_sampler, texCoord);
    }
    
    vec4 lightColour = calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal, mvVertexNormal); 
    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += lightColour;

    fragColor = baseColour * totalLight; 
    
    //if ( fog.activeFog == 1 ){ 
		fragColor = calcFog(mvVertexPos, fragColor, fog, ambientLight, directionalLight);
	//}
} 

