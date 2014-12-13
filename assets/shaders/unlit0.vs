attribute vec4 a_Position;
attribute vec3 a_Normal;

uniform mat4 u_MVPMatrix;
uniform mat4 u_MVMatrix;

void main() 
{  
	vec3 FixedNormal = vec3(u_MVMatrix * vec4(normalize(a_Normal), 0.0));
	gl_Position = u_MVPMatrix * a_Position + 0.00001 * FixedNormal.x;
}