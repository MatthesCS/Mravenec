#version 150
in vec2 textCoord;
out vec4 outColor;
uniform sampler2D texture;
uniform float dilku;
uniform float stop;

void main() {
    vec4 barva;
    if ( stop == 0.0 )
    {
    float dilek = 1/dilku;
    barva = texture2D(texture, textCoord);
    barva.r = 1.0;
    barva.g = 1.0;

    //vlevo
    vec2 textCoordVlevo = textCoord - vec2(dilek, 0.0);
    if(textCoordVlevo.x < 0.0)
    {
        textCoordVlevo = textCoordVlevo + vec2(1.0, 0.0);
    }
    vec4 barvaVlevo = texture2D(texture, textCoordVlevo);
    if(barvaVlevo.r == 1.0 && barvaVlevo.g == 0.0)
    {
        barva.r = 0.0;
        barva.g = 0.5;
    }

    //vpravo
    vec2 textCoordVpravo = textCoord + vec2(dilek, 0.0);
    if(textCoordVpravo.x > 1.0)
    {
        textCoordVpravo = textCoordVpravo - vec2(1.0, 0.0);
    }
    vec4 barvaVpravo = texture2D(texture, textCoordVpravo);
    if(barvaVpravo.r == 0.5 && barvaVpravo.g == 0.0)
    {
        barva.r = 0.0;
        barva.g = 1.0;
    }

    //dole
    vec2 textCoordDole = textCoord - vec2(0.0, dilek);
    if(textCoordDole.y < 0.0)
    {
        textCoordDole = textCoordDole + vec2(0.0, 1.0);
    }
    vec4 barvaDole = texture2D(texture, textCoordDole);
    if(barvaDole.r == 0.0 && barvaDole.g == 1.0)
    {
        barva.r = 1.0;
        barva.g = 0.0;
    }

    //nahore
    vec2 textCoordNahore = textCoord + vec2(0.0, dilek);
    if(textCoordNahore.y > 1.0)
    {
        textCoordNahore = textCoordNahore - vec2(0.0, 1.0);
    }
    vec4 barvaNahore = texture2D(texture, textCoordNahore);
    if(barvaNahore.r == 0.0 && barvaNahore.g == 0.5)
    {
        barva.r = 0.5;
        barva.g = 0.0;
    }

    }
    else
    {
        barva = texture2D(texture, textCoord);
    }
    outColor = barva;
} 
