attribute vec4 vPosition;
attribute vec3 vNormal;

uniform mat4 uMVPMatrix;

varying vec3 v_Normal;

void main() 
{  
	v_Normal = normalize(vNormal);
	gl_Position = uMVPMatrix * vPosition;
}