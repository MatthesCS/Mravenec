package app;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import meshutils.MeshGenerator;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import oglutils.OGLBuffers;
import oglutils.OGLTextRenderer;
import oglutils.OGLUtils;
import oglutils.ShaderUtils;
import oglutils.ToFloatArray;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;
import appUtils.textUtils;
import oglutils.OGLRenderTarget;
import oglutils.OGLTexture2D;
import transforms.Mat4Scale;

/**
 * GLSL sample:<br/>
 * Draw 3D geometry, use camera and projection transformations<br/>
 * Requires JOGL 2.3.0 or newer
 *
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2015-09-05
 */
public class Renderer implements GLEventListener, MouseListener,
        MouseMotionListener, KeyListener
{

    int width, height, ox, oy;
    int pocetBodu, kroku, krokChache = -2;

    boolean inicializace, stop, jedenKrok;

    textUtils textUtils;

    OGLTexture2D.Viewer textureViewer;
    OGLRenderTarget mravenci1, mravenci2;

    OGLBuffers shader, shaderMravenci1, shaderMravenci2;
    OGLTextRenderer textRenderer;

    int shader1, shaderLocMat;
    int shaderM1, shaderM1LocMat, shaderM1LocStop, shaderM1LocDilku;
    int shaderM2, shaderM2LocMat, shaderM2LocStop, shaderM2LocDilku;

    Camera cam = new Camera();
    Mat4 proj;

    @Override
    public void init(GLAutoDrawable glDrawable)
    {
        inicializace = true;
        stop = true;
        kroku = 0;
        pocetBodu = 10;
        GL2 gl = glDrawable.getGL().getGL2();

        OGLUtils.printOGLparameters(gl);
        OGLUtils.shaderCheck(gl);
        shader1 = ShaderUtils.loadProgram(gl, "/shader/shader");
        shaderM1 = ShaderUtils.loadProgram(gl, "/shader/shaderTexturovy");
        shaderM2 = ShaderUtils.loadProgram(gl, "/shader/shaderTexturovy");

        createBuffers(gl);

        shaderLocMat = gl.glGetUniformLocation(shader1, "mat");
        shaderM1LocMat = gl.glGetUniformLocation(shaderM1, "mat");
        shaderM1LocDilku = gl.glGetUniformLocation(shaderM1, "dilku");
        shaderM1LocStop = gl.glGetUniformLocation(shaderM1, "stop");

        shaderM2LocMat = gl.glGetUniformLocation(shaderM1, "mat");
        shaderM2LocDilku = gl.glGetUniformLocation(shaderM1, "dilku");
        shaderM2LocStop = gl.glGetUniformLocation(shaderM1, "stop");

        cam = cam.withPosition(new Vec3D(5, 5, 2.5))
                .withAzimuth(Math.PI * 1.25)
                .withZenith(Math.PI * -0.125);

        gl.glEnable(GL2.GL_DEPTH_TEST);

        mravenci1 = new OGLRenderTarget(gl, 200, 200);
        mravenci2 = new OGLRenderTarget(gl, 200, 200);
        textureViewer = new OGLTexture2D.Viewer(gl);

        textRenderer = new OGLTextRenderer(gl, width, height);
        textUtils = new textUtils(textRenderer, width, height, glDrawable);
    }

    void createBuffers(GL2 gl)
    {
        //shader = MeshGenerator.createGrid(gl, pocetBodu, "inParamPos");
        shader = MeshGenerator.vytvorGridMravencu(gl, pocetBodu, "inParamPos", "inColor", 10);
        shaderMravenci1 = MeshGenerator.createGrid(gl, 2, "inParamPos");
        shaderMravenci2 = MeshGenerator.createGrid(gl, 2, "inParamPos");
    }

    @Override
    public void display(GLAutoDrawable glDrawable)
    {

        GL2 gl = glDrawable.getGL().getGL2();

        

        if (!stop)
        {
            kroku++;
        }
        if (jedenKrok)
        {
            krokChache = kroku;
            stop = false;
            jedenKrok = false;
        }
        if (kroku - 1 == krokChache)
        {
            krokChache--;
            stop = true;
        }
        
        if (kroku == 0)
        {
            inicializace(glDrawable);
        } else if (kroku % 2 == 1)
        {
            licha(glDrawable);
        } else
        {
            suda(glDrawable);
        }

        textureViewer.view(mravenci1.getColorTexture(), -1, -1, 1);
        textureViewer.view(mravenci2.getColorTexture(), 0, 0, 1);

        textUtils.vypisCopyrightaKroky(kroku);
        textUtils.vypisTextOvládání();
    }

    void suda(GLAutoDrawable glDrawable)
    {
        GL2 gl = glDrawable.getGL().getGL2();

        gl.glUseProgram(shaderM2);
        mravenci1.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUniformMatrix4fv(shaderM2LocMat,
                1,
                false,
                ToFloatArray.convert(cam.getViewMatrix().mul(proj)
                        .mul(new Mat4Scale((double) width / height, 1, 1))), 0);
        gl.glUniform1f(shaderM2LocDilku, (float) pocetBodu - 1);
        float s = (float) 0.0;
        if (stop)
        {
            s = (float) 1.0;
        }
        gl.glUniform1f(shaderM2LocStop, s);

        mravenci2.getColorTexture().bind(shaderM2, "texture", 0);
        shaderMravenci2.draw(GL2.GL_TRIANGLES, shaderM2);

        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        gl.glViewport(0, 0, width, height);

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    }

    void licha(GLAutoDrawable glDrawable)
    {
        GL2 gl = glDrawable.getGL().getGL2();

        gl.glUseProgram(shaderM1);
        mravenci2.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUniformMatrix4fv(shaderM1LocMat,
                1,
                false,
                ToFloatArray.convert(cam.getViewMatrix().mul(proj)
                        .mul(new Mat4Scale((double) width / height, 1, 1))), 0);
        gl.glUniform1f(shaderM1LocDilku, (float) pocetBodu - 1);
        float s = (float) 0.0;
        if (stop)
        {
            s = (float) 1.0;
        }
        gl.glUniform1f(shaderM1LocStop, s);

        mravenci1.getColorTexture().bind(shaderM1, "texture", 0);
        shaderMravenci1.draw(GL2.GL_TRIANGLES, shaderM1);

        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        gl.glViewport(0, 0, width, height);

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    }

    void inicializace(GLAutoDrawable glDrawable)
    {
        GL2 gl = glDrawable.getGL().getGL2();

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(shader1);
        mravenci1.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUniformMatrix4fv(
                shaderLocMat,
                1,
                false,
                ToFloatArray.convert(cam.getViewMatrix().mul(proj)
                        .mul(new Mat4Scale((double) width / height, 1, 1))), 0);

        shader.draw(GL2.GL_TRIANGLES, shader1);

        gl.glUseProgram(shaderM1);

        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        gl.glViewport(0, 0, width, height);

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height)
    {
        this.width = width;
        this.height = height;
        proj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);
        textRenderer.updateSize(width, height);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
            boolean deviceChanged)
    {
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        ox = e.getX();
        oy = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        cam = cam.addAzimuth((double) Math.PI * (ox - e.getX()) / width)
                .addZenith((double) Math.PI * (e.getY() - oy) / width);
        ox = e.getX();
        oy = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_M:
                stop = !stop;
                break;
            case KeyEvent.VK_N:
                if (stop)
                {
                    jedenKrok = true;
                }
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                cam = cam.forward(1);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                cam = cam.right(1);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                cam = cam.backward(1);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                cam = cam.left(1);
                break;
            case KeyEvent.VK_CONTROL:
                cam = cam.down(1);
                break;
            case KeyEvent.VK_SHIFT:
                cam = cam.up(1);
                break;
            case KeyEvent.VK_SPACE:
                cam = cam.withFirstPerson(!cam.getFirstPerson());
                break;
            case KeyEvent.VK_R:
                cam = cam.mulRadius(0.9f);
                break;
            case KeyEvent.VK_F:
                cam = cam.mulRadius(1.1f);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e
    )
    {
    }

    @Override
    public void keyTyped(KeyEvent e
    )
    {
    }

    @Override
    public void dispose(GLAutoDrawable glDrawable
    )
    {
        GL2 gl = glDrawable.getGL().getGL2();
        gl.glDeleteProgram(shader1);
    }

}
