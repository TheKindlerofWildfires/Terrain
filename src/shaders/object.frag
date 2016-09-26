#version 400 core

in vec4 vertexColor;
out	vec4 fragColor;

uniform	sampler2D	texture_sampler;
uniform	vec3 colour;
uniform	int	useColour;
void main()
{
	if(useColour ==1)
	{
		fragColor = vec4(colour,1);
	}
   // fragColor=texture(texture_sampler,outTexCoord); 
} 