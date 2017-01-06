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
    barva.b = 1.0;
    barva.a = 1.0;

    //vlevo
    vec2 textCoordVlevo = textCoord - vec2(dilek, 0.0);
    if(textCoordVlevo.x >= 0.0)
    {
        vec4 barvaVlevo = texture2D(texture, textCoordVlevo);
        if(barvaVlevo.r >= 0.8 && barvaVlevo.g < 0.5 && barvaVlevo.b < 0.5 && barvaVlevo.a < 0.5)
        {
            barva.r = 0.0;
            barva.g = 0.0;
            barva.b = 1.0;
            barva.a = 0.0;
        }
    }
    
    //vpravo
    vec2 textCoordVpravo = textCoord + vec2(dilek, 0.0);
    if(textCoordVpravo.x <= 1.0)
    {
        vec4 barvaVpravo = texture2D(texture, textCoordVpravo);
        if(barvaVpravo.r < 0.5 && barvaVpravo.g >= 0.8 && barvaVpravo.b < 0.5 && barvaVpravo.a < 0.5)
        {
            barva.r = 0.0;
            barva.g = 0.0;
            barva.b = 0.0;
            barva.a = 1.0;
        }
    }

    //dole
    vec2 textCoordDole = textCoord - vec2(0.0, dilek);
    if(textCoordDole.y > 0.0)
    {
        vec4 barvaDole = texture2D(texture, textCoordDole);
        if(barvaDole.r < 0.5 && barvaDole.g < 0.5 && barvaDole.b >= 0.8 && barvaDole.a < 0.5)
        {
            barva.r = 0.0;
            barva.g = 1.0;
            barva.b = 0.0;
            barva.a = 0.0;
        }
    }

    //nahore
    vec2 textCoordNahore = textCoord + vec2(0.0, dilek);
    if(textCoordNahore.y < 1.0)
    {
        vec4 barvaNahore = texture2D(texture, textCoordNahore);
        if(barvaNahore.r < 0.5 && barvaNahore.g < 0.5 && barvaNahore.b < 0.5 && barvaNahore.a >= 0.8)
        {
            barva.r = 1.0;
            barva.g = 0.0;
            barva.b = 0.0;
            barva.a = 0.0;
        }
    }

    }
    else
    {
        barva = texture2D(texture, textCoord);
    }
    outColor = barva;
} 
