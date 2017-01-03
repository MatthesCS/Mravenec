#version 150
in vec2 textCoord;
out vec4 outColor;
uniform sampler2D texture;
uniform float dilku;
uniform float kroku;

void main() {
    float dilek = 1/dilku;
    vec4 barva = texture2D(texture, textCoord);
    if(barva.r > 0.0)
    {
        barva.r = 0.0;
        barva.g = 1.0;
    }
    vec2 textCoordVlevo = textCoord - vec2(dilek, 0.0);
    vec4 barvaVlevo = texture2D(texture, textCoordVlevo);
    if(barvaVlevo.r > 0.0)
    {
        barva.r = 1.0;
        barva.g = 0.0;
    }
    
    outColor = barva;
} 
