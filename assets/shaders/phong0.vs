attribute vec4 a_Position;
attribute vec3 a_Normal;

uniform mat4 u_MVPMatrix;
uniform mat4 u_ModelMatrix;

varying vec3 p_Normal;

void main() 
{  
	p_Normal = normalize(vec3(u_ModelMatrix * vec4(a_Normal, 0.0)));
	gl_Position = u_MVPMatrix * a_Position;
}