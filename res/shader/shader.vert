#version 150
in vec2 inParamPos;
out vec3 vertColor;
uniform mat4 mat;

vec3 surface(vec2 paramPos)
{
    return vec3(
		inParamPos.x,
                inParamPos.y,
                0
	);
}
		
void main() {
	vec3 position = surface(inParamPos);
	gl_Position = mat * vec4(position, 1.0);
        vertColor = vec3(inParamPos,0);
}