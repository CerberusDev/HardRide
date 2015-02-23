attribute vec4 a_Position;
attribute vec3 a_EndTranslation;

uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

uniform float u_Lifetime;

void main() 
{
	float Scale = 2.0 * u_Lifetime;
	
	vec3 InterpolatedTranslation = a_EndTranslation * u_Lifetime;
	
	mat4 TranslationMatrix = mat4(1.0);
	TranslationMatrix[3].xyz = InterpolatedTranslation;
	
	mat4 ModelViewMatrix = u_ViewMatrix * u_ModelMatrix * TranslationMatrix;
	
	ModelViewMatrix[0].xyz = vec3(-1.0 * Scale, 0.0, 0.0);
	ModelViewMatrix[1].xyz = vec3(0.0, 1.0 * Scale, 0.0);
	ModelViewMatrix[2].xyz = vec3(0.0, 0.0, -1.0 * Scale);
	
	gl_Position = u_ProjectionMatrix * ModelViewMatrix * a_Position;
}