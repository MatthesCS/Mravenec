/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appUtils;

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
        String text = "Ovládání: kamera: [LMB], pohyb: [WASD] nebo šipky, [CTRL] a [Shift], přepnutí vzhledu [7]";
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

    public void vypisTextSchéma(int stareSchema, int schema, boolean popis, int[] instrukce)
    {
        String text = "Barevné schéma číslo [0]: " + stareSchema;
        if (stareSchema != schema)
        {
            text += " (nove schéma " + schema + " po resetu)";
        }
        if (!popis)
        {
            text += " (14 = vlastní [2]); Zobrazení popisu chování [1]";
        } else
        {
            text += "; Skrytí popisu [1]";
            vypisPopisSchema(instrukce);
        }
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 50, text);
    }

    public void vypisZadavani(int zadavaniCislo, int[] ins)
    {
        String text = "Zadávání schématu [WASD nebo šipky]: ";
        for (int i = 0; i < zadavaniCislo; i++)
        {
            switch (ins[zadavaniCislo])
            {
                case 1:
                    text += "R";
                    break;
                case 2:
                    text += "L";
                    break;
                case 3:
                    text += "F";
                    break;
                case 4:
                    text += "B";
                    break;
            }
        }
        text += "; Ukončení zadávání [2]";
         
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 125, text);
    }

    private void vypisPopisSchema(int[] instrukce)
    {
        String text = "Instrukce: ";
        String text2 = "Posloupnost barev: ";
        for (int i = 0; i < instrukce.length; i++)
        {
            /*
            1 = doprava
            2 = doleva
            3 = dopředu
            4 = dozadu
             */
            switch (instrukce[i])
            {
                case 1:
                    text += "R";
                    break;
                case 2:
                    text += "L";
                    break;
                case 3:
                    text += "F";
                    break;
                case 4:
                    text += "B";
                    break;
            }
        }

        text2 += "bílá -> červená -> zelená -> modrá -> žlutá -> fialová -> tyrkysová ->";
        String text3 = "černá -> tmavě červená -> tm. zelená -> tm. modrá -> tm. žlutá ->";
        String text4 = "tm. fialová -> tm. tyrkysová -> šedá -> oranžová -> bílá";

        textRenderer.drawStr2D(3, textRenderer.getHeight() - 65, text);
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 80, text2);
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 95, text3);
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 110, text4);
    }

    public void vypisCopyrightaKroky(int krok, int krokuZaSnimek)
    {
        if (krok > 1)
        {
            krok = krok * krokuZaSnimek;
        }
        textRenderer.drawStr2D(textRenderer.getWidth() / 3, 3, "Kroků za snímek -[5/8]+[6/9]: "
                + krokuZaSnimek + "; Krok: " + krok + "; (c) PGRF UHK");
    }
}
