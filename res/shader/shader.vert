#version 150
in vec2 inParamPos;
out vec3 vertColor;
uniform mat4 mat;

vec3 surface(vec2 paramPos)
{
    return vec3(
		inParamPos.x * 2 - 1,
                inParamPos.y * 2 - 1,
                0
	);
}
		
void main() {
	vec3 position = surface(inParamPos);
	gl_Position = vec4(position, 1.0);
        vertColor = vec3(inParamPos,0);
}