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
        text += " [M], krokování: [N], reset: [3]";
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
                text = "Bílá: doprava -> zelená: doleva -> bílá";
                break;
            case 1:
                text = "Bílá: doprava -> zelená: doprava -> modrá: doleva -> bílá";
                break;
            case 2:
                text = "Bílá: doprava -> zelená: doleva -> modrá: doleva -> bílá";
                break;
            case 3:
                text = "Bílá: doprava -> zelená: doprava -> modrá: doprava -> červená: doleva -> žlutá: doprava -> bílá";
                break;
            case 4:
                text = "Bílá: doprava -> zelená: doleva -> modrá: doprava -> červená: doprava -> žlutá: doprava ->";
                text2 = "fialová: doprava -> tyrkysová: doprava -> černá: doleva -> tmavě zelená: doleva -> bílá";
                break;
            case 5:
                text = "Bílá: doprava -> zelená: doleva -> modrá: doprava -> červená: doprava -> žlutá: doprava ->";
                text2 = "fialová: doprava -> tyrkysová: doleva -> černá: doleva -> tmavě zelená: doleva ->";
                text3 = "tmavě červená: doprava -> tmavě modrá: doprava -> bílá";
                break;
        }
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 65, text);
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 80, text2);
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 95, text3);
    }

    public void vypisCopyrightaKroky(int krok, int krokuZaSnimek)
    {
        if(krok > 1)
        {
            krok = krok * krokuZaSnimek;
        }
        int cislic = (int) (Math.log10(krok) + 1);
        textRenderer.drawStr2D(textRenderer.getWidth() - (300 + cislic * 4), 3, "Kroků za snímek -[5/8]+[6/9]: "
                + krokuZaSnimek + "; Krok: "+ krok + "; (c) PGRF UHK");
    }
}
