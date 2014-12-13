attribute vec4 a_Position;
attribute vec3 a_Normal;

uniform mat4 u_MVPMatrix;
uniform mat4 u_MVMatrix;

varying vec3 p_Normal;

void main() 
{  
	p_Normal = vec3(u_MVMatrix * vec4(normalize(a_Normal), 0.0));
	gl_Position = u_MVPMatrix * a_Position;
}