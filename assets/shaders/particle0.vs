attribute vec4 a_Position;
attribute vec3 a_EndTranslation;

uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

uniform float u_Lifetime;

void main() 
{
	float Scale = max(1.0 * (log(min(u_Lifetime, 0.5) * 300.0 + 1.0) / 5.0) * ((3.0 + log(max(3.0 - max(u_Lifetime - 0.7, 0.0) * 10.0, 0.0))) / 4.0), 0.0);
	vec3 InterpolatedTranslation = a_EndTranslation * (log(min(u_Lifetime, 0.2) * 300.0 + 1.0) / 5.0 + max(u_Lifetime - 0.2, 0.0));
	
	mat4 TranslationMatrix = mat4(1.0);
	TranslationMatrix[3].xyz = InterpolatedTranslation;
	
	mat4 ModelViewMatrix = u_ViewMatrix * u_ModelMatrix * TranslationMatrix;
	
	ModelViewMatrix[0].xyz = vec3(-1.0 * Scale, 0.0, 0.0);
	ModelViewMatrix[1].xyz = vec3(0.0, 1.0 * Scale, 0.0);
	ModelViewMatrix[2].xyz = vec3(0.0, 0.0, -1.0 * Scale);
	
	gl_Position = u_ProjectionMatrix * ModelViewMatrix * a_Position;
}