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
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;
import appUtils.textUtils;
import oglutils.OGLRenderTarget;
import oglutils.OGLTexture2D;

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
    int pocetBodu, barevneSchema, stareSchema, kroku, krokChache = -2, krokuZaSnimek, pocetSchemat;
    int[] instrukce;

    boolean stop, jedenKrok, popisSchematu, reset, debug;

    textUtils textUtils;

    OGLTexture2D.Viewer textureViewer;
    OGLRenderTarget mravenci1, mravenci2, pole1, pole2;

    OGLBuffers shaderMravenciInit, shaderMravenci1, shaderMravenci2, shaderPoleInit, shaderPole1, shaderPole2;
    OGLTextRenderer textRenderer;

    int shaderMInit;
    int shaderM1, shaderM1LocStop, shaderM1LocDilku, shaderM1LocSchema,
            shaderM1LocIns1, shaderM1LocIns2, shaderM1LocIns3, shaderM1LocIns4;
    int shaderM2, shaderM2LocStop, shaderM2LocDilku, shaderM2LocSchema,
            shaderM2LocIns1, shaderM2LocIns2, shaderM2LocIns3, shaderM2LocIns4;
    int shaderPInit;
    int shaderP1, shaderP1LocStop, shaderP1LocSchema,
            shaderP1LocIns1, shaderP1LocIns2, shaderP1LocIns3, shaderP1LocIns4;
    int shaderP2, shaderP2LocStop, shaderP2LocSchema,
            shaderP2LocIns1, shaderP2LocIns2, shaderP2LocIns3, shaderP2LocIns4;

    Camera cam = new Camera();
    Mat4 proj;

    @Override
    public void init(GLAutoDrawable glDrawable)
    {
        stop = true;
        reset = false;
        debug = false;
        popisSchematu = false;
        krokuZaSnimek = 100;
        pocetSchemat = 13;
        instrukce = new int[16];
        kroku = 0;
        pocetBodu = 100;
        barevneSchema = 0;
        nastavInstrukce(barevneSchema);
        stareSchema = barevneSchema;
        GL2 gl = glDrawable.getGL().getGL2();

        OGLUtils.printOGLparameters(gl);
        OGLUtils.shaderCheck(gl);
        shaderMInit = ShaderUtils.loadProgram(gl, "/shader/shaderInitMravenci");
        shaderM1 = ShaderUtils.loadProgram(gl, "/shader/shaderMravencovy");
        shaderM2 = ShaderUtils.loadProgram(gl, "/shader/shaderMravencovy");
        shaderPInit = ShaderUtils.loadProgram(gl, "/shader/shaderInitMravenci");
        shaderP1 = ShaderUtils.loadProgram(gl, "/shader/shaderPole");
        shaderP2 = ShaderUtils.loadProgram(gl, "/shader/shaderPole");

        createBuffers(gl);

        shaderM1LocDilku = gl.glGetUniformLocation(shaderM1, "dilku");
        shaderM1LocStop = gl.glGetUniformLocation(shaderM1, "stop");
        shaderM1LocSchema = gl.glGetUniformLocation(shaderM1, "schema");
        shaderM1LocIns1 = gl.glGetUniformLocation(shaderM1, "ins1");
        shaderM1LocIns2 = gl.glGetUniformLocation(shaderM1, "ins2");
        shaderM1LocIns3 = gl.glGetUniformLocation(shaderM1, "ins3");
        shaderM1LocIns4 = gl.glGetUniformLocation(shaderM1, "ins4");

        shaderM2LocDilku = gl.glGetUniformLocation(shaderM2, "dilku");
        shaderM2LocStop = gl.glGetUniformLocation(shaderM2, "stop");
        shaderM2LocSchema = gl.glGetUniformLocation(shaderM2, "schema");
        shaderM2LocIns1 = gl.glGetUniformLocation(shaderM2, "ins1");
        shaderM2LocIns2 = gl.glGetUniformLocation(shaderM2, "ins2");
        shaderM2LocIns3 = gl.glGetUniformLocation(shaderM2, "ins3");
        shaderM2LocIns4 = gl.glGetUniformLocation(shaderM2, "ins4");

        shaderP1LocStop = gl.glGetUniformLocation(shaderP1, "stop");
        shaderP1LocSchema = gl.glGetUniformLocation(shaderP1, "schema");
        shaderP1LocIns1 = gl.glGetUniformLocation(shaderP1, "ins1");
        shaderP1LocIns2 = gl.glGetUniformLocation(shaderP1, "ins2");
        shaderP1LocIns3 = gl.glGetUniformLocation(shaderP1, "ins3");
        shaderP1LocIns4 = gl.glGetUniformLocation(shaderP1, "ins4");

        shaderP2LocStop = gl.glGetUniformLocation(shaderP2, "stop");
        shaderP2LocSchema = gl.glGetUniformLocation(shaderP2, "schema");
        shaderP2LocIns1 = gl.glGetUniformLocation(shaderP2, "ins1");
        shaderP2LocIns2 = gl.glGetUniformLocation(shaderP2, "ins2");
        shaderP2LocIns3 = gl.glGetUniformLocation(shaderP2, "ins3");
        shaderP2LocIns4 = gl.glGetUniformLocation(shaderP2, "ins4");

        cam = cam.withPosition(new Vec3D(5, 5, 2.5))
                .withAzimuth(Math.PI * 1.25)
                .withZenith(Math.PI * -0.125);

        gl.glEnable(GL2.GL_DEPTH_TEST);

        mravenci1 = new OGLRenderTarget(gl, 200, 200);
        mravenci2 = new OGLRenderTarget(gl, 200, 200);
        pole1 = new OGLRenderTarget(gl, 200, 200);
        pole2 = new OGLRenderTarget(gl, 200, 200);
        textureViewer = new OGLTexture2D.Viewer(gl);

        textRenderer = new OGLTextRenderer(gl, width, height);
        textUtils = new textUtils(textRenderer);
    }

    void createBuffers(GL2 gl)
    {
        shaderMravenciInit = MeshGenerator.vytvorGridSMravencemUprostred(gl, pocetBodu, "inParamPos", "inColor");
        //shaderMravenciInit = MeshGenerator.vytvorGridMravencu(gl, pocetBodu, "inParamPos", "inColor", 2);
        shaderMravenci1 = MeshGenerator.createGrid(gl, 2, "inParamPos");
        shaderMravenci2 = MeshGenerator.createGrid(gl, 2, "inParamPos");
        shaderPoleInit = MeshGenerator.vytvorGridMravencu(gl, 2, "inParamPos", "inColor", 0);
        shaderPole1 = MeshGenerator.createGrid(gl, 2, "inParamPos");
        shaderPole2 = MeshGenerator.createGrid(gl, 2, "inParamPos");
    }

    @Override
    public void display(GLAutoDrawable glDrawable)
    {
        if (reset)
        {
            reset = false;
            kroku = 0;
            stareSchema = barevneSchema;
            stop = true;
        }

        if (barevneSchema != stareSchema)
        {
            if (kroku <= 1)
            {
                stareSchema = barevneSchema;
                nastavInstrukce(stareSchema);
            }
        }
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
            kroku++;
        } else if (kroku % 2 == 1)
        {
            licha(glDrawable);
            if (!stop)
            {
                for (int i = 1; i < krokuZaSnimek; i++)
                {
                    suda(glDrawable);
                    licha(glDrawable);
                }
            }
        } else
        {
            suda(glDrawable);
            if (!stop)
            {
                for (int i = 1; i < krokuZaSnimek; i++)
                {
                    licha(glDrawable);
                    suda(glDrawable);
                }
            }
        }

        if (!debug)
        {
            textureViewer.view(pole1.getColorTexture(), -1, -1, 2);
        } else
        {
            textureViewer.view(mravenci1.getColorTexture(), -1, -1, 0.99);
            textureViewer.view(pole1.getColorTexture(), -1, 0.01, 0.99);
            textureViewer.view(mravenci2.getColorTexture(), 0.01, 0.01, 0.99);
            textureViewer.view(pole1.getColorTexture(), 0.01, -1, 0.99);
        }

        //System.out.println(kroku);
        textUtils.vypisCopyrightaKroky(kroku, krokuZaSnimek);
        textUtils.vypisTextOvládání(stop);
        textUtils.vypisTextSchéma(stareSchema, barevneSchema, popisSchematu);
    }

    void suda(GLAutoDrawable glDrawable)
    {
        GL2 gl = glDrawable.getGL().getGL2();

        gl.glUseProgram(shaderM2);
        mravenci1.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUniform1f(shaderM2LocDilku, (float) pocetBodu - 1);
        float s = (float) 0.0;
        if (stop)
        {
            s = (float) 1.0;
        }
        gl.glUniform1f(shaderM2LocStop, s);
        gl.glUniform1i(shaderM2LocSchema, stareSchema);
        gl.glUniform4f(shaderM2LocIns1, instrukce[0], instrukce[1], instrukce[2], instrukce[3]);
        gl.glUniform4f(shaderM2LocIns2, instrukce[4], instrukce[5], instrukce[6], instrukce[7]);
        gl.glUniform4f(shaderM2LocIns3, instrukce[8], instrukce[9], instrukce[10], instrukce[11]);
        gl.glUniform4f(shaderM2LocIns4, instrukce[12], instrukce[13], instrukce[14], instrukce[15]);

        mravenci2.getColorTexture().bind(shaderM2, "texturaMravenci", 0);
        pole2.getColorTexture().bind(shaderM2, "texturaPole", 1);
        shaderMravenci2.draw(GL2.GL_TRIANGLES, shaderM2);

        gl.glUseProgram(shaderP2);
        pole1.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUniform1f(shaderP2LocStop, s);
        gl.glUniform1i(shaderP2LocSchema, stareSchema);
        gl.glUniform4f(shaderP2LocIns1, instrukce[0], instrukce[1], instrukce[2], instrukce[3]);
        gl.glUniform4f(shaderP2LocIns2, instrukce[4], instrukce[5], instrukce[6], instrukce[7]);
        gl.glUniform4f(shaderP2LocIns3, instrukce[8], instrukce[9], instrukce[10], instrukce[11]);
        gl.glUniform4f(shaderP2LocIns4, instrukce[12], instrukce[13], instrukce[14], instrukce[15]);

        mravenci2.getColorTexture().bind(shaderP2, "texturaMravenci", 0);
        pole2.getColorTexture().bind(shaderP2, "texturaPole", 1);
        shaderPole2.draw(GL2.GL_TRIANGLES, shaderP2);

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

        gl.glUniform1f(shaderM1LocDilku, (float) pocetBodu - 1);
        float s = (float) 0.0;
        if (stop)
        {
            s = (float) 1.0;
        }
        gl.glUniform1f(shaderM1LocStop, s);
        gl.glUniform1i(shaderM1LocSchema, stareSchema);
        gl.glUniform4f(shaderM1LocIns1, instrukce[0], instrukce[1], instrukce[2], instrukce[3]);
        gl.glUniform4f(shaderM1LocIns2, instrukce[4], instrukce[5], instrukce[6], instrukce[7]);
        gl.glUniform4f(shaderM1LocIns3, instrukce[8], instrukce[9], instrukce[10], instrukce[11]);
        gl.glUniform4f(shaderM1LocIns4, instrukce[12], instrukce[13], instrukce[14], instrukce[15]);

        mravenci1.getColorTexture().bind(shaderM1, "texturaMravenci", 0);
        pole1.getColorTexture().bind(shaderM1, "texturaPole", 1);
        shaderMravenci1.draw(GL2.GL_TRIANGLES, shaderM1);

        gl.glUseProgram(shaderP1);
        pole2.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUniform1f(shaderP1LocStop, s);
        gl.glUniform1i(shaderP1LocSchema, stareSchema);
        gl.glUniform4f(shaderP1LocIns1, instrukce[0], instrukce[1], instrukce[2], instrukce[3]);
        gl.glUniform4f(shaderP1LocIns2, instrukce[4], instrukce[5], instrukce[6], instrukce[7]);
        gl.glUniform4f(shaderP1LocIns3, instrukce[8], instrukce[9], instrukce[10], instrukce[11]);
        gl.glUniform4f(shaderP1LocIns4, instrukce[12], instrukce[13], instrukce[14], instrukce[15]);

        mravenci1.getColorTexture().bind(shaderP1, "texturaMravenci", 0);
        pole1.getColorTexture().bind(shaderP1, "texturaPole", 1);
        shaderPole1.draw(GL2.GL_TRIANGLES, shaderP1);

        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        gl.glViewport(0, 0, width, height);

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    }

    void nastavInstrukce(int barSchema)
    {
        /*
        1 = doprava
        2 = doleva
        3 = dopředu
        4 = dozadu
         */
        switch (barSchema)
        {
            case 0: //RL
                instrukce[0] = 1;
                instrukce[1] = 2;
                for (int i = 2; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 1: //RRL
                instrukce[0] = 1;
                instrukce[1] = 1;
                instrukce[2] = 2;
                for (int i = 3; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 2: //RLL
                instrukce[0] = 1;
                instrukce[1] = 2;
                instrukce[2] = 2;
                for (int i = 3; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 3: //RRRLR
                instrukce[0] = 1;
                instrukce[1] = 1;
                instrukce[2] = 1;
                instrukce[3] = 2;
                instrukce[4] = 1;
                for (int i = 5; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 4: //RLRRRRRLL
                instrukce[0] = 1;
                instrukce[1] = 2;
                instrukce[2] = 1;
                instrukce[3] = 1;
                instrukce[4] = 1;
                instrukce[5] = 1;
                instrukce[6] = 1;
                instrukce[7] = 2;
                instrukce[8] = 2;
                for (int i = 9; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 5: //RLRRRRLLLRR
                instrukce[0] = 1;
                instrukce[1] = 2;
                instrukce[2] = 1;
                instrukce[3] = 1;
                instrukce[4] = 1;
                instrukce[5] = 1;
                instrukce[6] = 2;
                instrukce[7] = 2;
                instrukce[8] = 2;
                instrukce[9] = 1;
                instrukce[10] = 1;
                for (int i = 11; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 6: //RLLR
                instrukce[0] = 1;
                instrukce[1] = 2;
                instrukce[2] = 2;
                instrukce[3] = 1;
                for (int i = 4; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 7: //RRLL
                instrukce[0] = 1;
                instrukce[1] = 1;
                instrukce[2] = 2;
                instrukce[3] = 2;
                for (int i = 4; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 8: //LF
                instrukce[0] = 2;
                instrukce[1] = 3;
                for (int i = 2; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 9: //RLLLLRRR
                instrukce[0] = 1;
                instrukce[1] = 2;
                instrukce[2] = 2;
                instrukce[3] = 2;
                instrukce[4] = 2;
                instrukce[5] = 1;
                instrukce[6] = 1;
                instrukce[7] = 1;
                for (int i = 8; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 10: //LRRRRLLF
                instrukce[0] = 1;
                instrukce[1] = 2;
                instrukce[2] = 2;
                instrukce[3] = 2;
                instrukce[4] = 2;
                instrukce[5] = 1;
                instrukce[6] = 1;
                instrukce[7] = 3;
                for (int i = 8; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 11:  //LRRRRRLLR
                instrukce[0] = 2;
                instrukce[1] = 1;
                instrukce[2] = 1;
                instrukce[3] = 1;
                instrukce[4] = 1;
                instrukce[5] = 1;
                instrukce[6] = 2;
                instrukce[7] = 2;
                instrukce[8] = 1;
                for (int i = 9; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 12:  //LRRRRLLLRRR 
                instrukce[0] = 2;
                instrukce[1] = 1;
                instrukce[2] = 1;
                instrukce[3] = 1;
                instrukce[4] = 1;
                instrukce[5] = 2;
                instrukce[6] = 2;
                instrukce[7] = 2;
                instrukce[8] = 1;
                instrukce[9] = 1;
                instrukce[10] = 1;
                for (int i = 11; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
            case 13:  //RLLLLRRRLLLR
                instrukce[0] = 1;
                instrukce[1] = 2;
                instrukce[2] = 2;
                instrukce[3] = 2;
                instrukce[4] = 2;
                instrukce[5] = 1;
                instrukce[6] = 1;
                instrukce[7] = 1;
                instrukce[8] = 2;
                instrukce[9] = 2;
                instrukce[10] = 2;
                instrukce[11] = 1;
                for (int i = 12; i < instrukce.length; i++)
                {
                    instrukce[i] = 0;
                }
                break;
        }
    }

    void inicializace(GLAutoDrawable glDrawable)
    {
        GL2 gl = glDrawable.getGL().getGL2();

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(shaderMInit);
        mravenci1.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        shaderMravenciInit.draw(GL2.GL_TRIANGLES, shaderMInit);

        gl.glUseProgram(shaderPInit);
        pole1.bind();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        shaderPoleInit.draw(GL2.GL_TRIANGLES, shaderPInit);

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
            case KeyEvent.VK_0:
            case KeyEvent.VK_NUMPAD0:
                barevneSchema++;
                if (barevneSchema > pocetSchemat)
                {
                    barevneSchema = 0;
                }
                break;
            case KeyEvent.VK_1:
            case KeyEvent.VK_NUMPAD1:
                popisSchematu = !popisSchematu;
                break;
            case KeyEvent.VK_2:
            case KeyEvent.VK_NUMPAD2:
                debug = !debug;
                break;
            case KeyEvent.VK_3:
            case KeyEvent.VK_NUMPAD3:
                reset = !reset;
                break;
            case KeyEvent.VK_5:
            case KeyEvent.VK_NUMPAD5:
                if (krokuZaSnimek > 1)
                {
                    krokuZaSnimek--;
                }
                break;
            case KeyEvent.VK_6:
            case KeyEvent.VK_NUMPAD6:
                krokuZaSnimek++;
                break;
            case KeyEvent.VK_8:
            case KeyEvent.VK_NUMPAD8:
                if (krokuZaSnimek > 10)
                {
                    krokuZaSnimek -= 10;
                }
                break;
            case KeyEvent.VK_9:
            case KeyEvent.VK_NUMPAD9:
                krokuZaSnimek += 10;
                break;
            case KeyEvent.VK_ENTER:
                stop = !stop;
                break;
            case KeyEvent.VK_SPACE:
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
    public void dispose(GLAutoDrawable glDrawable)
    {
        GL2 gl = glDrawable.getGL().getGL2();
        gl.glDeleteProgram(shaderMInit);
        gl.glDeleteProgram(shaderM1);
        gl.glDeleteProgram(shaderM2);
        gl.glDeleteProgram(shaderPInit);
        gl.glDeleteProgram(shaderP1);
        gl.glDeleteProgram(shaderP2);
    }

}
