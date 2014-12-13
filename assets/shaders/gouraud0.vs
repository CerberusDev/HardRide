attribute vec4 a_Position;
attribute vec3 a_Normal;

uniform mat4 u_MVPMatrix;
uniform mat4 u_MVMatrix;
uniform vec4 u_Color;

varying vec4 p_Color;

void main() 
{  
	gl_Position = u_MVPMatrix * a_Position;
	
	vec3 FixedNormal = vec3(u_MVMatrix * vec4(normalize(a_Normal), 0.0));
	vec3 LightVector = normalize(vec3(-1.0, 0.3, 0.5));
	
	float AmbientWeight = 0.2;
	float AmbientFactor = 1.0;
	vec4 AmbientLight = u_Color * AmbientFactor * AmbientWeight;
	
	float DiffuseWeight = 1.0 - AmbientWeight;
	float DiffuseFactor = max(dot(FixedNormal, LightVector), 0.0);  
	vec4 DiffuseLight = u_Color * DiffuseFactor * DiffuseWeight;
	
	p_Color = AmbientLight + DiffuseLight;
}