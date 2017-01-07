#version 150
in vec2 textCoord;
out vec4 outColor;
uniform sampler2D texturaMravenci;
uniform sampler2D texturaPole;
uniform float dilku;
uniform float stop;
uniform int schema;

/*
    otočí mravence podle barvy pole a schématu
    směr mravence
    1 = doprava
    2 = doleva
    3 = nahoru
    4 = dolů
*/
int otocMravence(int smer, vec2 coord)
{
    int novySmer = 0;
    vec4 barva = texture2D(texturaPole, coord);
    if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b >= 0.8)
    {
        //doprava
            if(smer == 1)
            {
                novySmer = 4;
            }
            else if(smer == 2)
            {
                novySmer = 3;
            }
            else if(smer == 3)
            {
                novySmer = 1;
            }
            else if(smer == 4)
            {
                novySmer = 2;
            }
    }
    else if(barva.r <= 0.3 && barva.g >= 0.8 && barva.b <= 0.3)
    {
        if(schema == 0 || schema == 2)
        {
            //doleva
            if(smer == 1)
            {
                novySmer = 3;
            }
            else if(smer == 2)
            {
                novySmer = 4;
            }
            else if(smer == 3)
            {
                novySmer = 2;
            }
            else if(smer == 4)
            {
                novySmer = 1;
            }
        }
        else if(schema == 1 || schema == 3)
        {
            //doprava
            if(smer == 1)
            {
                novySmer = 4;
            }
            else if(smer == 2)
            {
                novySmer = 3;
            }
            else if(smer == 3)
            {
                novySmer = 1;
            }
            else if(smer == 4)
            {
                novySmer = 2;
            }
        }
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b >= 0.8)
    {
        if(schema == 1 || schema == 2)
        {
            //doleva
            if(smer == 1)
            {
                novySmer = 3;
            }
            else if(smer == 2)
            {
                novySmer = 4;
            }
            else if(smer == 3)
            {
                novySmer = 2;
            }
            else if(smer == 4)
            {
                novySmer = 1;
            }
        }
        else if(schema == 3)
        {
            //doprava
            if(smer == 1)
            {
                novySmer = 4;
            }
            else if(smer == 2)
            {
                novySmer = 3;
            }
            else if(smer == 3)
            {
                novySmer = 1;
            }
            else if(smer == 4)
            {
                novySmer = 2;
            }
        }
    }
    else if(barva.r >= 0.8 && barva.g <= 0.3 && barva.b <= 0.3)
    {
        if(schema == 3)
        {
            //doleva
            if(smer == 1)
            {
                novySmer = 3;
            }
            else if(smer == 2)
            {
                novySmer = 4;
            }
            else if(smer == 3)
            {
                novySmer = 2;
            }
            else if(smer == 4)
            {
                novySmer = 1;
            }
        }
    }
    else if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b <= 0.3)
    {
        if(schema == 3)
        {
            //doprava
            if(smer == 1)
            {
                novySmer = 4;
            }
            else if(smer == 2)
            {
                novySmer = 3;
            }
            else if(smer == 3)
            {
                novySmer = 1;
            }
            else if(smer == 4)
            {
                novySmer = 2;
            }
        }
    }
    return novySmer;
}

/*
    zjistí, jestli je na dané souřadnici mravenec, a pokud ano vrátí jeho směr
    1 = doprava
    2 = doleva
    3 = nahoru
    4 = dolů
    0 = není mravenec
*/
int smerMravence(vec2 coord)
{
    int smer = 0;
    if(coord.x >= 0.0 && coord.x <= 1.0 && coord.y >= 0.0 && coord.y <= 1.0)
    {
        vec4 barva = texture2D(texturaMravenci, coord);
        if(barva.r == 1.0 && barva.g == 1.0 && barva.b == 1.0 && barva.a == 1.0)
        {
            smer = 0;
        }
        if(barva.r >= 0.8 && barva.g < 0.5 && barva.b < 0.5 && barva.a < 0.5)
        {
            smer = 1;
        }
        if(barva.r < 0.5 && barva.g >= 0.8 && barva.b < 0.5 && barva.a < 0.5)
        {
            smer = 2;
        }
        if(barva.r < 0.5 && barva.g < 0.5 && barva.b >= 0.8 && barva.a < 0.5)
        {
            smer = 3;
        }
        if(barva.r < 0.5 && barva.g < 0.5 && barva.b < 0.5 && barva.a >= 0.8)
        {
            smer = 4;
        }
    }
    else
    {
        smer = 0;
    }
    if(smer > 0)
    {
        return otocMravence(smer, coord);
    }
    return 0;
}

/*
    vrátí barvu pro mravence podle jeho směru
*/
vec4 barvaMravence(int smer)
{
    vec4 barva = vec4(1.0, 1.0, 1.0, 1.0);
    if(smer == 1)
    {
        barva.r = 1.0;
        barva.g = 0.0;
        barva.b = 0.0;
        barva.a = 0.0;
    }
    else if(smer == 2)
    {
        barva.r = 0.0;
        barva.g = 1.0;
        barva.b = 0.0;
        barva.a = 0.0;
    }
    else if(smer == 3)
    {
        barva.r = 0.0;
        barva.g = 0.0;
        barva.b = 1.0;
        barva.a = 0.0;
    }
    else if(smer == 4)
    {
        barva.r = 0.0;
        barva.g = 0.0;
        barva.b = 0.0;
        barva.a = 1.0;
    }

    return barva;
}

void main() {
    vec4 barva;
    if ( stop == 0.0 )
    {
        float dilek = 1/dilku;
        barva = texture2D(texturaMravenci, textCoord);
        barva.r = 1.0;
        barva.g = 1.0;
        barva.b = 1.0;
        barva.a = 1.0;
        
        //vlevo
        vec2 textCoordPosun = textCoord - vec2(dilek, 0.0);
        int smer = smerMravence(textCoordPosun);
        if(smer == 1)
        {
            barva = barvaMravence(smer);
        }
        //vpravo
        textCoordPosun = textCoord + vec2(dilek, 0.0);
        smer = smerMravence(textCoordPosun);
        if(smer == 2)
        {
            barva = barvaMravence(smer);
        }
        //dole
        textCoordPosun = textCoord - vec2(0.0, dilek);
        smer = smerMravence(textCoordPosun);
        if(smer == 3)
        {
            barva = barvaMravence(smer);
        }
        //nahoře
        textCoordPosun = textCoord + vec2(0.0, dilek);
        smer = smerMravence(textCoordPosun);
        if(smer == 4)
        {
            barva = barvaMravence(smer);
        }
    }
    else
    {
        barva = texture2D(texturaMravenci, textCoord);
    }
    outColor = barva;
} 
