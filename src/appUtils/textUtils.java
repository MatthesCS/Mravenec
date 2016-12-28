/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appUtils;

import com.jogamp.opengl.GLAutoDrawable;
import oglutils.OGLTextRenderer;

/**
 *
 * @author Matthes
 */
public class textUtils
{
    OGLTextRenderer textRenderer;

    public textUtils(OGLTextRenderer textRenderer, int width, int height, GLAutoDrawable glDraw)
    {
        this.textRenderer = textRenderer;
    }
    
     public void vypisTextOvládání()
    {
        String text = "Ovládání: kamera: [LMB], pohyb: [WASD] nebo šipky, [CTRL] a [Shift], ";
        textRenderer.drawStr2D(3, textRenderer.getHeight() - 20, text);
    }
    
    public void vypisCopyright()
    {
        textRenderer.drawStr2D(textRenderer.getWidth() - 90, 3, " (c) PGRF UHK");
    }
}
