attribute vec4 a_Position;
attribute vec3 a_Normal;

uniform mat4 u_MVPMatrix;

varying vec3 p_Normal;

void main() 
{  
	p_Normal = normalize(a_Normal);
	gl_Position = u_MVPMatrix * a_Position;
}