attribute vec4 a_Position;
attribute vec3 a_Normal;

uniform mat4 u_MVPMatrix;

void main() 
{  
	vec3 FixedNormal = normalize(a_Normal);
	gl_Position = u_MVPMatrix * a_Position + 0.00001 * FixedNormal.x;
}