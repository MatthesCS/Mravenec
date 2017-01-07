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
    if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b >= 0.8)
    {
        nBarva.r = 0.0;
        nBarva.g = 1.0;
        nBarva.b = 0.0;
        nBarva.a = 1.0;
    }
    else if(barva.r <= 0.3 && barva.g >= 0.8 && barva.b <= 0.3)
    {
        if(schema == 0)
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
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b >= 0.8)
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
    else if(barva.r >= 0.8 && barva.g <= 0.3 && barva.b <= 0.3)
    {
        if(schema >= 3)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b <= 0.3)
    {
        if(schema >= 3)
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
