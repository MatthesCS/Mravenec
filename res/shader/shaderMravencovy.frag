#version 150
in vec2 textCoord;
out vec4 outColor;
uniform sampler2D texturaMravenci;
uniform sampler2D texturaPole;
uniform float dilku;
uniform float stop;
uniform vec4 ins1;
uniform vec4 ins2;
uniform vec4 ins3;
uniform vec4 ins4;
uniform int schema;

// otočí směr doleva
int otocDoleva(int smer)
{
    int novySmer = 0;
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
    return novySmer;
}

//otočí směr dopředu (zůstane stejný)
int otocDopredu(int smer)
{
    return smer;
}

//otočí směr dozadu
int otocDozadu(int smer)
{
    int novySmer = 0;
    if(smer == 1)
    {
        novySmer = 2;
    }
    else if(smer == 2)
    {
        novySmer = 1;
    }
    else if(smer == 3)
    {
        novySmer = 4;
    }
    else if(smer == 4)
    {
        novySmer = 5;
    }
    return novySmer;
}

//otočí směr doprava
int otocDoprava(int smer)
{
    int novySmer = 0;
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
    return novySmer;
}

/*
    otočí mravence podle instrukcí
    1 = doprava
    2 = doleva
    3 = dopředu
    4 = dozadu
*/
int otoc(int smer, float instrukce)
{
    if(instrukce == 1.0)
    {
        return otocDoprava(smer);
    }
    else if(instrukce == 2.0)
    {
        return otocDoleva(smer);
    }
    else if(instrukce == 3.0)
    {
        return otocDopredu(smer);
    }
    else if(instrukce == 4.0)
    {
        return otocDozadu(smer);
    }
    return 0;
}

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
    if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b >= 0.8)  //bílá
    {
        novySmer = otoc(smer, ins1.x);
    }
    else if(barva.r >= 0.8 && barva.g <= 0.3 && barva.b <= 0.3)  //červená
    {
        novySmer = otoc(smer, ins1.y);
    }
    else if(barva.r <= 0.3 && barva.g >= 0.8 && barva.b <= 0.3)  //zelená
    {
        novySmer = otoc(smer, ins1.z);
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b >= 0.8)  //modrá
    {
        novySmer = otoc(smer, ins1.w);
    }
    else if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b  <= 0.3)  //žlutá
    {
        novySmer = otoc(smer, ins2.x);
    }
    else if(barva.r >= 0.8 && barva.g <= 0.3 && barva.b  >= 0.8)  //fialová
    {
        novySmer = otoc(smer, ins2.y);
    }
    else if(barva.r <= 0.3 && barva.g >= 0.8 && barva.b  >= 0.8)  //tyrkysová
    {
        novySmer = otoc(smer, ins2.z);
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b  <= 0.3)  //černá
    {
        novySmer = otoc(smer, ins2.w);
    }
    else if(barva.r >= 0.3 && barva.r <= 0.7 && barva.g <= 0.3 && barva.b  <= 0.3)  //tmavě červená
    {
        novySmer = otoc(smer, ins3.x);
    }
    else if(barva.r <= 0.3 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b  <= 0.3)  //tmavě zelená
    {
        novySmer = otoc(smer, ins3.y);
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b >= 0.3 && barva.b  <= 0.7)  //tmavě modrá
    {
        novySmer = otoc(smer, ins3.z);
    }
    else if(barva.r >= 0.3 && barva.r <= 0.7 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b  <= 0.3) //tmavě žlutá
    {
        novySmer = otoc(smer, ins3.w);
    }
    else if(barva.r >= 0.3 && barva.r <= 0.7 && barva.g <= 0.3 && barva.b >= 0.3 && barva.b  <= 0.7) //tmavě fialová
    {
        novySmer = otoc(smer, ins4.x);
    }
    else if(barva.r <= 0.3 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b >= 0.3 && barva.b  <= 0.7) //tmavě tyrkysová
    {
        novySmer = otoc(smer, ins4.y);
    }
    else if(barva.r >= 0.3 && barva.r <= 0.7 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b >= 0.3 && barva.b  <= 0.7) //šedá
    {
        novySmer = otoc(smer, ins4.z);
    }
    else if(barva.r >= 0.8 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b <= 0.3) //oranžová
    {
        novySmer = otoc(smer, ins4.w);
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
