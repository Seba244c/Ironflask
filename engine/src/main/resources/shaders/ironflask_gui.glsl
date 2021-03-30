#version 330

in vec2 position;
in vec2 textureCords;

out vec4 color;
out vec4 bc;
out vec2 outTexCoord;

uniform mat4 projection;
uniform vec2 pixelScale;
uniform vec2 screenPos;
uniform vec4 backgroundColor;
uniform vec4 offset;
uniform int useColor;

void main() {	
	gl_Position = projection * vec4((position * pixelScale) + screenPos, 0, 1.0);
	bc = backgroundColor;
	outTexCoord = (textureCords * offset.zw) + offset.xy;
}

///#ENDVERTEX

#version 330

out vec4 fragColor;
in vec4 bc;
in vec2 outTexCoord;
uniform int useColor;
uniform sampler2D texture_sampler;

void main() {
	if ( useColor == 1 ) {
        fragColor = bc;
    } else {
		fragColor = texture(texture_sampler, outTexCoord);
	}
}