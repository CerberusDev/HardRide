precision mediump float;

uniform vec4 vColor;

varying vec3 v_Normal;

void main() 
{
	vec3 CorrectNormal = normalize(v_Normal);
	float diffuse = max(dot(CorrectNormal, vec3(1.0, 0.0, 0.0)), 0.1);  
	gl_FragColor = vColor * diffuse;
}