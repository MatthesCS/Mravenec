#version 150
in vec2 textCoord;
out vec4 outColor;
uniform sampler2D texturaMravenci;
uniform sampler2D texturaPole;
uniform vec4 ins1;
uniform vec4 ins2;
uniform vec4 ins3;
uniform vec4 ins4;
uniform float stop;
uniform int schema;

//zjistí, jestli je na souřadnici mravenec, pokud ano, vrací 1
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
    postup barev:
    bílá -> červená -> zelená -> modrá -> žlutá -> fialová -> tyrkysová -> černá -> tmavě červená ->
    tmavě želená -> tmavě modrá -> tmavě žlutá -> tmavě fialová -> tmavě tyrkysová -> šedá -> 
    oranžová -> bílá
*/
vec4 novaBarva(vec4 barva)
{
    vec4 nBarva = barva;
    if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b >= 0.8)  //bílá
    {
        if(ins1.y != 0)
        {
            nBarva.r = 1.0;
            nBarva.g = 0.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.8 && barva.g <= 0.3 && barva.b <= 0.3)  //červená
    {
        if(ins1.z != 0)
        {
            nBarva.r = 0.0;
            nBarva.g = 1.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g >= 0.8 && barva.b <= 0.3)  //zelená
    {
        if(ins1.w != 0)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b >= 0.8)  //modrá
    {
        if(ins2.x != 0)
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.8 && barva.g >= 0.8 && barva.b  <= 0.3)  //žlutá
    {
        if(ins2.y != 0)
        {
            nBarva.r = 1.0;
            nBarva.g = 0.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.8 && barva.g <= 0.3 && barva.b  >= 0.8)  //fialová
    {
        if(ins2.z != 0)
        {
            nBarva.r = 0.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g >= 0.8 && barva.b  >= 0.8)  //tyrkysová
    {
        if(ins2.w != 0)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b  <= 0.3)  //černá
    {
        if(ins3.x != 0)
        {
            nBarva.r = 0.5;
            nBarva.g = 0.0;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.3 && barva.r <= 0.7 && barva.g <= 0.3 && barva.b  <= 0.3)  //tmavě červená
    {
        if(ins3.y != 0)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.5;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b  <= 0.3)  //tmavě zelená
    {
        if(ins3.z != 0)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.0;
            nBarva.b = 0.5;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g <= 0.3 && barva.b >= 0.3 && barva.b  <= 0.7)  //tmavě modrá
    {
        if(ins3.w != 0)
        {
            nBarva.r = 0.5;
            nBarva.g = 0.5;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.3 && barva.r <= 0.7 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b  <= 0.3) //tmavě žlutá
    {
        if(ins4.x != 0)
        {
            nBarva.r = 0.5;
            nBarva.g = 0.0;
            nBarva.b = 0.5;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.3 && barva.r <= 0.7 && barva.g <= 0.3 && barva.b >= 0.3 && barva.b  <= 0.7) //tmavě fialová
    {
        if(ins4.y != 0)
        {
            nBarva.r = 0.0;
            nBarva.g = 0.5;
            nBarva.b = 0.5;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r <= 0.3 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b >= 0.3 && barva.b  <= 0.7) //tmavě tyrkysová
    {
        if(ins4.z != 0)
        {
            nBarva.r = 0.5;
            nBarva.g = 0.5;
            nBarva.b = 0.5;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.3 && barva.r <= 0.7 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b >= 0.3 && barva.b  <= 0.7) //šedá
    {
        if(ins4.w != 0)
        {
            nBarva.r = 1.0;
            nBarva.g = 0.5;
            nBarva.b = 0.0;
            nBarva.a = 1.0;
        }
        else
        {
            nBarva.r = 1.0;
            nBarva.g = 1.0;
            nBarva.b = 1.0;
            nBarva.a = 1.0;
        }
    }
    else if(barva.r >= 0.8 && barva.g >= 0.3 && barva.g <= 0.7 && barva.b <= 0.3) //oranžová
    {
        nBarva.r = 1.0;
        nBarva.g = 1.0;
        nBarva.b = 1.0;
        nBarva.a = 1.0;
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
