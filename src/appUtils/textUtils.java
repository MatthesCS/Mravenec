/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appUtils;

import com.jogamp.opengl.GLAutoDrawable;
import java.awt.Color;
import oglutils.OGLTextRenderer;

/**
 *
 * @author Matthes
 */
public class textUtils
{

    OGLTextRenderer textRenderer;

    public textUtils(OGLTextRenderer textRenderer)
    {
        this.textRenderer = textRenderer;
        this.textRenderer.setColor(Color.BLUE);
    }

    public void vypisTextOvládání(boolean stop)
    {
        String text = "Ovládání: kamera: [LMB], pohyb: [WASD] nebo šipky, [CTRL] a [Shift]";
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 20, text);
        if (stop)
        {
            text = "Spuštění:";
        } else
        {
            text = "Stop:";
        }
        text += " [ENTER], krokování: [SPACE], reset: [3]";
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 35, text);
    }

    public void vypisTextSchéma(int stareSchema, int schema, boolean popis)
    {
        String text = "Barevné schéma číslo [0]: " + stareSchema;
        if (stareSchema != schema)
        {
            text += " (nove schéma " + schema + " po resetu)";
        }
        if (!popis)
        {
            text += "; Zobrazení popisu chování [1]";
        } else
        {
            text += "; Skrytí popisu [1]";
            vypisPopisSchema(schema);
        }
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 50, text);
    }

    private void vypisPopisSchema(int schema)
    {
        String text = "";
        String text2 = "";
        String text3 = "";
        switch (schema)
        {
            case 0:
                //RL město a pak dálnice
                text = "Bílá: doprava -> zelená: doleva -> bílá";
                break;
            case 1:
                //RRL dálnice
                text = "Bílá: doprava -> zelená: doprava -> modrá: doleva -> bílá";
                break;
            case 2:
                //RLL velké město
                text = "Bílá: doprava -> zelená: doleva -> modrá: doleva -> bílá";
                break;
            case 3:
                //RRRLR další velké město
                text = "Bílá: doprava -> zelená: doprava -> modrá: doprava -> červená: doleva -> žlutá: doprava -> bílá";
                break;
            case 4:
                //RLRRRRRLL čtverec
                text = "Bílá: doprava -> zelená: doleva -> modrá: doprava -> červená: doprava -> žlutá: doprava ->";
                text2 = "fialová: doprava -> tyrkysová: doprava -> černá: doleva -> tmavě zelená: doleva -> bílá";
                break;
            case 5:
                //RLRRRRLLLRR jiný čtverec
                text = "Bílá: doprava -> zelená: doleva -> modrá: doprava -> červená: doprava -> žlutá: doprava ->";
                text2 = "fialová: doprava -> tyrkysová: doleva -> černá: doleva -> tmavě zelená: doleva ->";
                text3 = "tmavě červená: doprava -> tmavě modrá: doprava -> bílá";
                break;
            case 6:
                //RLLR město s hraniční výplní a dálnicemi
                text = "Bílá: doprava -> zelená: doleva -> modrá: doleva -> červená: doprava -> bílá";
                break;
            case 7:
                    //RRLL město ve tvaru mozku
                text = "Bílá: doprava -> zelená: doprava -> modrá: doleva -> červená: doleva -> bílá";
                break;
            case 8:
                //LF binární počítání
                text = "Bílá: doleva -> zelená: dopředu -> bílá";
                break;
            case 9:
                //RLLLLRRR  mozek ve čtverci
                text = "Bílá: doprava -> zelená: doleva -> modrá: doleva -> červená: doleva -> žlutá: doleva ->";
                text2 = "fialová: doprava -> tyrkysová: doprava -> černá: doprava ->  bílá";
                break;
            case 10:
                //LRRRRLLL mozek ve čtvrerci otočený
                text = "Bílá: doleva -> zelená: doprava -> modrá: doprava -> červená: doprava -> žlutá: doprava ->";
                text2 = "fialová: doleva -> tyrkysová: doleva -> černá: doleva ->  bílá";
                break;
            case 11:
                //LRRRRRLLR čtverec s dálnicemi a městy
                text = "Bílá: doleva -> zelená: doprava -> modrá: doprava -> červená: doprava -> žlutá: doprava ->";
                text2 = "fialová: doprava -> tyrkysová: doleva -> černá: doleva -> tmavě zelená: doprava -> bílá";
                break;
            case 12:
                //LRRRRLLLRRR čtverec se spirálou
                text = "Bílá: doleva -> zelená: doprava -> modrá: doprava -> červená: doprava -> žlutá: doprava ->";
                text2 = "fialová: doleva -> tyrkysová: doleva -> černá: doleva -> tmavě zelená: doprava ->";
                text3 = "tmavě červená: doprava -> tmavě modrá: doprava -> bílá";
                break;
            case 13:
                //RLLLLRRRLLLR čtverec s logaritmoou spirálou
                text = "Bílá: doprava -> zelená: doleva -> modrá: doleva -> červená: doleva -> žlutá: doleva ->";
                text2 = "fialová: doprava -> tyrkysová: doprava -> černá: doprava -> tmavě zelená: doleva ->";
                text3 = "tmavě červená: doleva -> tmavě modrá: doleva -> šedá: doprava -> bílá";
                break;
                
                //bílá -> červená -> zelená -> modrá -> žlutá -> fialová -> tyrkysová -> černá -> tmavě červená ->
                //tmavě želená -> tmavě modrá -> tmavě žlutá -> tmavě fialová -> tmavě tyrkysová -> šedá -> oranžová
                /*
                    r   g   b  
                    1   1   1   bílá   
                    1   0   0   červená
                    0   1   0   zelená
                    0   0   1   modrá
                    1   1   0   žlutá
                    1   0   1   fialová
                    0   1   1   tyrkysová
                    0   0   0   černá 
                    0.5 0   0   tmavě červená
                    0   0.5 0   tmavě zelená
                    0   0   0.5 tmodrá
                    0.5 0.5 0   tžlutá
                    0.5 0   0.5 tfialová
                    0   0.5 0.5 ttyrkysová
                    05  0.5 0.5 šedá
                    1   0.5 0   oranžová                    
                */
        }
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 65, text);
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 80, text2);
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 95, text3);
    }

    public void vypisCopyrightaKroky(int krok, int krokuZaSnimek)
    {
        if (krok > 1)
        {
            krok = krok * krokuZaSnimek;
        }
        int cislic = (int) (Math.log10(krok) + 1);
        textRenderer.drawStr2D(textRenderer.getWidth() - (300 + cislic * 4), 3, "Kroků za snímek -[5/8]+[6/9]: "
                + krokuZaSnimek + "; Krok: " + krok + "; (c) PGRF UHK");
    }
}
