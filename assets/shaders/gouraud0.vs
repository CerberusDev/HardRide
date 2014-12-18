attribute vec4 a_Position;
attribute vec3 a_Normal;

uniform mat4 u_MVPMatrix;
uniform vec4 u_Color;
uniform mat4 u_ModelMatrix;

varying vec4 p_Color;

void main() 
{  
	gl_Position = u_MVPMatrix * a_Position;
	
	vec3 FixedNormal = normalize(vec3(u_ModelMatrix * vec4(a_Normal, 0.0)));
	vec3 LightVector = normalize(vec3(-1.0, 0.3, 0.5));
	
	float AmbientWeight = 0.25;
	float AmbientFactor = 1.0;
	vec4 AmbientLight = u_Color * AmbientFactor * AmbientWeight;
	
	float DiffuseWeight = 1.0 - AmbientWeight;
	float DiffuseFactor = max(dot(FixedNormal, LightVector), 0.0);  
	vec4 DiffuseLight = u_Color * DiffuseFactor * DiffuseWeight;
	
	p_Color = AmbientLight + DiffuseLight;
}