#version 330

in float vNdotL;
in vec3 vReflectDir;
in vec3 vViewDir;

out vec4 fragColor;

struct Material
{
    vec3 colour;
    int useColour;
    float reflectance;
};

uniform Material material;
uniform vec3 uWarmColor;
uniform vec3 uCoolColor;
uniform float uDiffuseWarm;
uniform float uDiffuseCool;

void main()
{
    vec3 kcool = min(uCoolColor + uDiffuseCool * material.colour, 1.0);
    vec3 kwarm = min(uWarmColor + uDiffuseWarm * material.colour, 1.0);
    vec3 kfinal = mix(kcool, kwarm, vNdotL);
  
    vec3 nreflect = normalize(vReflectDir);
    vec3 nview = normalize(vViewDir);
  
    float spec = max(dot(nreflect, nview), 0.0);
    spec = pow(spec, 32.0);
  
    fragColor = vec4(min(kfinal + spec, 1.0), 1.0);   
}

