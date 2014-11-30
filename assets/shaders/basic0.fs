precision mediump float;

uniform vec4 u_Color;

varying vec3 p_Normal;

void main() 
{
	vec3 FixedNormal = normalize(p_Normal);
	vec3 LightVector = normalize(vec3(-1.0, 0.3, 0.5));
	
	float AmbientWeight = 0.2;
	float AmbientFactor = 1.0;
	vec4 AmbientLight = u_Color * AmbientFactor * AmbientWeight;
	
	float DiffuseWeight = 1.0 - AmbientWeight;
	float DiffuseFactor = max(dot(FixedNormal, LightVector), 0.0);  
	vec4 DiffuseLight = u_Color * DiffuseFactor * DiffuseWeight;
	
	gl_FragColor = AmbientLight + DiffuseLight;
}