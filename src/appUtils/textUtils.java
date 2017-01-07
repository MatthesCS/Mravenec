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

    public void vypisTextOvládání()
    {
        String text = "Ovládání: kamera: [LMB], pohyb: [WASD] nebo šipky, [CTRL] a [Shift], ";
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 20, text);
    }

    public void vypisTextSchéma(int schema, boolean popis)
    {
        String text = "Barevné schéma číslo [0]: " + schema;
        if (!popis)
        {
            text += "; Zobrazení popisu chování [1]";
        } else
        {
            text += "; Skrytí popisu [1]";
            vypisPopisSchema(schema);
        }
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 35, text);
    }

    private void vypisPopisSchema(int schema)
    {
        String text = "";
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
        }
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 50, text);
    }

    public void vypisCopyrightaKroky(int krok)
    {
        int cislic = (int) (Math.log10(krok) + 1);
        textRenderer.drawStr2D(textRenderer.getWidth() - (149 + cislic * 4), 3, "Kroků: " + krok + "; (c) PGRF UHK");
    }
}
