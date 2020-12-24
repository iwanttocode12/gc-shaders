#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;

out float vNdotL;
out vec3 vReflectDir;
out vec3 vViewDir;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 colour;
    vec3 position;
    float intensity;
    Attenuation att;
};

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform PointLight pointLight;

void main()
{
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    vec3 normal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    vec3 lightDir = normalize(pointLight.position - mvPos.xyz);
    vReflectDir = normalize(reflect(-lightDir , normal));
    vViewDir = normalize(-mvPos.xyz);
    vNdotL = dot(normal, lightDir) * 0.5 + 0.5;
    gl_Position = projectionMatrix * mvPos;
}