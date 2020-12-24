#version 330

in vec4 mvVertexColor;

out vec4 fragColor;

void main()
{

    fragColor = mvVertexColor;
}