precision mediump float;

uniform vec4 u_Color;

varying vec3 p_Normal;

void main() 
{
	vec3 FixedNormal = normalize(p_Normal);
	float DiffuseFactor = max(dot(FixedNormal, vec3(1.0, 0.0, 0.0)), 0.1);  
	gl_FragColor = u_Color * DiffuseFactor;
}