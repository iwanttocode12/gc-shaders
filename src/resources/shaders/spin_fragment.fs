#version 330

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 colour;
    // Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct DirectionalLight
{
    vec3 colour;
    vec3 direction;
    float intensity;
};

struct Material
{
    vec3 colour;
    int useColour;
    float reflectance;
};

uniform int currentTime;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform DirectionalLight directionalLight;

vec4 calcLightColour(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColour = vec4(light_colour, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = light_intensity  * specularFactor * material.reflectance * vec4(light_colour, 1.0);

    return (diffuseColour + specColour);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_colour = calcLightColour(light.colour, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
        light.att.exponent * distance * distance;
    return light_colour / attenuationInv;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColour(light.colour, light.intensity, position, normalize(light.direction), normal);
}

void main()
{
    vec4 baseColour;
    int time = abs(int(floor(currentTime/50)));
    int time_b = abs(int(floor(currentTime/1000)));
    int t = (int(floor((outTexCoord.x + outTexCoord.y)*30)));
    if (time_b % 6 == 1 || time_b % 6 == 2 || time_b % 6 == 3){
        if (time % 7 == 0){
            if (t % 7 == 0){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else{
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
        }
        else if (time % 7 == 1){
            if (t % 7 == 0){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else{
                baseColour = vec4(0.0f,0.26f,0,1);
            }
        }
        else if (time % 7 == 2){
            if (t % 7 == 0){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }   
            else{
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
        }
        else if (time % 7 == 3){
            if (t % 7 == 0){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else{
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
        }
        else if (time % 7 == 4){
            if ( t % 7 == 0){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else{
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
        }
        else if (time % 7 == 5){
            if (t % 7 == 0){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else{
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
        }
        else{
            if (t % 7 == 0){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else{
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
        }
    }
    else{
        if (time % 7 == 0){
            if (t % 7 == 0){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }   
            else if (t % 7 == 2){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else{
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
        }
        else if (time % 7 == 6){
            if (t % 7 == 0){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else{
                baseColour = vec4(0.0f,0.26f,0,1);
            }
        }
        else if (time % 7 == 5){
            if (t % 7 == 0){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else{
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
        }
        else if (time % 7 == 4){
            if (t % 7 == 0){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else{
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
        }
        else if (time % 7 == 3){
            if ( t % 7 == 0){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else{
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
        }
        else if (time % 7 == 2){
            if (t % 7 == 0){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 2){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 4){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
            else{
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
        }
        else{
            if (t % 7 == 0){
                baseColour = vec4(0.62f,0.0f,0.2f,1);
            }
            else if (t % 7 == 1){
                baseColour = vec4(0.8,0.39f,0.0f,1);
            }   
            else if (t % 7 == 2){
                baseColour = vec4(1.0f,0.87f,0.0f,1);
            }
            else if (t % 7 == 3){
                baseColour = vec4(0.0f,0.26f,0.39f,1);
            }   
            else if (t % 7 == 4){
                baseColour = vec4(0.0f,0.26f,0,1);
            }
            else if (t % 7 == 5){
                baseColour = vec4(0.0f,0.0f,0.26f,1);
            }
            else{
                baseColour = vec4(0.92f,0.31f,0.60f,1);
            }
        }
    }
    
    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal);
    totalLight += calcPointLight(pointLight, mvVertexPos, mvVertexNormal); 
    
    fragColor = baseColour * totalLight;
}