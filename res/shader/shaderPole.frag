#version 150
in vec2 textCoord;
out vec4 outColor;
uniform sampler2D texturaMravenci;
uniform sampler2D texturaPole;
uniform float stop;
uniform int schema;

//zjistí, jestli je na souřadnici mravenec, pokud ano, vrací true
int isMravenec(vec2 coord)
{
    vec4 barva = texture2D(texturaMravenci, coord);
    if(barva.r == 1.0 && barva.g == 1.0 && barva.b == 1.0 && barva.a == 1.0)
    {
        return 0;
    }
    if(barva.r >= 0.8 && barva.g < 0.5 && barva.b < 0.5 && barva.a < 0.5)
    {
        return 1;
    }
    if(barva.r < 0.5 && barva.g >= 0.8 && barva.b < 0.5 && barva.a < 0.5)
    {
        return 1;
    }
    if(barva.r < 0.5 && barva.g < 0.5 && barva.b >= 0.8 && barva.a < 0.5)
    {
        return 1;
    }
    if(barva.r < 0.5 && barva.g < 0.5 && barva.b < 0.5 && barva.a >= 0.8)
    {
        return 1;
    }
    return 0;
}

/*
    vrací novou barvu pole a schématu
*/
vec4 novaBarva(vec4 barva)
{
    vec4 nBarva = barva;
    if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b >= 0.8)  //bílá
    {
        nBarva.r = 0.0;
        nBarva.g = 1.0;
        nBarva.b = 0.0;
        nBarva.a = 1.0;
    }
    else if(barva.r <= 0.3 && barva.g >= 0.8 && barva.b <= 0.3)  //zelená
    {
        if(schema == 0 || schema == 8)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
        else if(schema > 0)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b >= 0.8)  //modrá
    {
        if(schema > 0 && schema < 3)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
        else {
            nBarva.r = 1.0;
            nBarva.g = 0.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.8 && barva.g <= 0.3 && barva.b <= 0.3)  //červená
    {
        if((schema >= 3 && schema < 6) || schema > 8)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else if (schema == 6 || schema == 7)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b <= 0.3)  //žlutá
    {
        if(schema == 3)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
        else if(schema > 3)
        {
            nBarva.r = 1.0;
            nBarva.g = 0.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.8 && barva.g <= 0.3 && barva.b >= 0.8)  //fialová
    {
        if(schema >= 4)
        {
            nBarva.r = 0.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g >= 0.8 && barva.b >= 0.8)  //tyrkysová
    {
        if(schema >= 4)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b <= 0.3)  //černá
    {
        if((schema >= 4 && schema < 9) || schema > 10)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.5;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        if(schema >= 9)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g >= 0.3 && barva.g <= 0.6 && barva.b <= 0.3)  //tmavě zelená
    {
        if(schema == 4 || schema == 11)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
        else if(schema >= 5)
        {
            nBarva.r = 0.5;
            nBarva.g = 0.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.3 && barva.r <= 0.6 && barva.g <= 0.3 && barva.b <= 0.3)//tmavě červená
    {
        if(schema >= 5)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.0;
            nBarva.b = 0.5;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b >= 0.3 && barva.b <= 0.6)  //tmavě modrá
    {
        if(schema >= 5 && schema < 13)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
        else if(schema == 13)
        {
            nBarva.r = 0.5;
            nBarva.g = 0.5;
            nBarva.b = 0.5;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.3 && barva.r <= 0.6 && barva.g >= 0.3 && barva.g <= 0.6 
    && barva.b >= 0.3 && barva.b <= 0.6)  //šedá
    {
        if(schema == 13)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }

    return nBarva;
}

void main() {
    vec4 barva;
    if ( stop == 0.0 )
    {
        barva = texture2D(texturaPole, textCoord);
        if(isMravenec(textCoord) == 1)
        {
            barva = novaBarva(barva);
        }
    }
    else
    {
        barva = texture2D(texturaPole, textCoord);
    }
    outColor = barva;
} 
