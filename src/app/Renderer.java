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
    int pocetBodu, kroku;

    boolean inicializace, stop;

    textUtils textUtils;

    OGLTexture2D.Viewer textureViewer;
    OGLRenderTarget mravenci1, mravenci2;

    OGLBuffers shader, shaderTexturovy;
    OGLTextRenderer textRenderer;

    int shader1, shaderLocMat;
    int shader2, shaderTexLocMat, shaderTexLocStop, shaderTexLocDilku, shaderTexLocKroku;

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
        shader2 = ShaderUtils.loadProgram(gl, "/shader/shaderTexturovy");

        createBuffers(gl);

        shaderLocMat = gl.glGetUniformLocation(shader1, "mat");
        shaderTexLocMat = gl.glGetUniformLocation(shader2, "mat");
        shaderTexLocDilku = gl.glGetUniformLocation(shader2, "dilku");
        shaderTexLocKroku = gl.glGetUniformLocation(shader2, "kroku");
        shaderTexLocStop = gl.glGetUniformLocation(shader2, "stop");

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
        shaderTexturovy = MeshGenerator.createGrid(gl, pocetBodu, "inParamPos");
    }

    @Override
    public void display(GLAutoDrawable glDrawable)
    {

        GL2 gl = glDrawable.getGL().getGL2();

        licha(glDrawable);

        if (!stop)
        {
            kroku++;
        }

        textureViewer.view(mravenci1.getColorTexture(), -1, -1, 1);
        textureViewer.view(mravenci2.getColorTexture(), 0, 0, 1);

        textUtils.vypisCopyrightaKroky(kroku);
        textUtils.vypisTextOvládání();
    }

    void licha(GLAutoDrawable glDrawable)
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

        gl.glUseProgram(shader2);
        mravenci2.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUniformMatrix4fv(
                shaderTexLocMat,
                1,
                false,
                ToFloatArray.convert(cam.getViewMatrix().mul(proj)
                        .mul(new Mat4Scale((double) width / height, 1, 1))), 0);
        gl.glUniform1f(shaderTexLocDilku, (float) pocetBodu - 1);
        gl.glUniform1f(shaderTexLocKroku, (float) kroku);
        float s = (float) 0.0;
        if (stop)
        {
            s = (float) 1.0;
        }
        gl.glUniform1f(shaderTexLocStop, s);

        mravenci1.getColorTexture().bind(shader2, "texture", 0);
        shaderTexturovy.draw(GL2.GL_TRIANGLES, shader2);

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
                System.out.println("app.Renderer.keyPressed()");
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
