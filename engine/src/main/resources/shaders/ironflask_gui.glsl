#version 330

in vec2 position;
in vec2 textureCords;

out vec4 color;
out vec4 bc;

uniform mat4 projection;
uniform vec2 pixelScale;
uniform vec2 screenPos;
uniform vec4 backgroundColor;

void main()
{	
	gl_Position = projection * vec4((position * pixelScale) + screenPos, 0, 1.0);
	bc = backgroundColor;
}

///#ENDVERTEX

#version 330

out vec4 fragColor;
in vec4 bc;

void main()
{
	fragColor = bc;
}