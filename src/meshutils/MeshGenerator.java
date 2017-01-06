package meshutils;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;
import java.util.Random;

import oglutils.OGLBuffers;
import oglutils.ToFloatArray;
import oglutils.ToIntArray;
import transforms.Vec2D;

public class MeshGenerator
{

    public static OGLBuffers createGrid(final GL2 gl, final int n, final int m, final String attribName)
    {
        int pocCtvercuX = n - 1;
        int pocCtvercuY = m - 1;
        List<Vec2D> vertex = new ArrayList<>();
        List<Integer> index = new ArrayList<>();

        for (int y = 0; y < n; y++)
        {
            for (int x = 0; x < m; x++)
            {
                float a = (float) x / pocCtvercuX;
                float b = (float) y / pocCtvercuY;

                vertex.add(new Vec2D(a, b));
            }
        }

        for (int y = 0; y < pocCtvercuY; y++)
        {
            for (int x = 0; x < pocCtvercuX; x++)
            {
                index.add(y * n + x);
                index.add(y * n + 1 + x);
                index.add((y + 1) * n + x);
                index.add(y * n + 1 + x);
                index.add((y + 1) * n + x);
                index.add((y + 1) * n + 1 + x);
            }
        }

        final OGLBuffers.Attrib[] attributes =
        {
            new OGLBuffers.Attrib(attribName, 2)
        };

        float[] vertexData = ToFloatArray.convert(vertex);
        int[] indexData = ToIntArray.convert(index);

        return new OGLBuffers(gl, vertexData, attributes, indexData);
    }

    public static OGLBuffers vytvorGridMravencu(final GL2 gl, final int n, final int m,
            final String jmenoAtributu1, final String jmenoAtributu2, int pocetMravencu)
    {
        int pocCtvercuX = n - 1;
        int pocCtvercuY = m - 1;
        float pravdepodobnostMravence = 1;

        if (pocetMravencu > pocCtvercuX * pocCtvercuY)
        {
            pocetMravencu = 5;
        }
        int mravencu = 0;
        int policek = 0;
        Random rand = new Random();
        float[] vertex = new float[pocCtvercuX * pocCtvercuY * 6 * (2 + 4)];

        for (int y = 0; y < pocCtvercuY; y++)
        {
            for (int x = 0; x < pocCtvercuX; x++)
            {
                float p1 = (pocetMravencu - mravencu);
                float p2 = (pocCtvercuX * pocCtvercuY) - policek;
                pravdepodobnostMravence = p1 / p2 * 100;
                float r;
                float g;
                float b;
                float a;
                int ctverec = x * 36 + y * pocCtvercuX * 36;
                if (mravencu < pocetMravencu && ((float) rand.nextInt(10000) / 100) < pravdepodobnostMravence)
                {
                    switch (rand.nextInt(4))
                    {
                        case 0:
                            r = (float) 1.0; //doprava
                            g = (float) 0;
                            b = (float) 0;
                            a = (float) 0;
                            break;
                        case 1:
                            r = (float) 0; //doleva
                            g = (float) 1.0;
                            b = (float) 0;
                            a = (float) 0;
                            break;
                        case 2:
                            r = (float) 0; //nahoru
                            g = (float) 0;
                            b = (float) 1.0;
                            a = (float) 0;
                            break;
                        case 3:
                        default:
                            r = (float) 0; //dolu
                            g = (float) 0;
                            b = (float) 0;
                            a = (float) 1.0;
                            break;
                    }
                    mravencu++;
                } else
                {
                    r = (float) 1;
                    g = (float) 1;
                    b = (float) 1;
                    a = (float) 1;
                    
                }
                float xS = (float) x / pocCtvercuX;
                float yS = (float) y / pocCtvercuY;
                vertex[ctverec] = xS;
                vertex[ctverec + 1] = yS;
                vertex[ctverec + 2] = r;
                vertex[ctverec + 3] = g;
                vertex[ctverec + 4] = b;
                vertex[ctverec + 5] = a;

                xS = (float) (x + 1) / pocCtvercuX;
                yS = (float) y / pocCtvercuY;
                vertex[ctverec + 6] = xS;
                vertex[ctverec + 7] = yS;
                vertex[ctverec + 8] = r;
                vertex[ctverec + 9] = g;
                vertex[ctverec + 10] = b;
                vertex[ctverec + 11] = a;

                xS = (float) x / pocCtvercuX;
                yS = (float) (y + 1) / pocCtvercuY;
                vertex[ctverec + 12] = xS;
                vertex[ctverec + 13] = yS;
                vertex[ctverec + 14] = r;
                vertex[ctverec + 15] = g;
                vertex[ctverec + 16] = b;
                vertex[ctverec + 17] = a;

                xS = (float) (x + 1) / pocCtvercuX;
                yS = (float) y / pocCtvercuY;
                vertex[ctverec + 18] = xS;
                vertex[ctverec + 19] = yS;
                vertex[ctverec + 20] = r;
                vertex[ctverec + 21] = g;
                vertex[ctverec + 22] = b;
                vertex[ctverec + 23] = a;

                xS = (float) x / pocCtvercuX;
                yS = (float) (y + 1) / pocCtvercuY;
                vertex[ctverec + 24] = xS;
                vertex[ctverec + 25] = yS;
                vertex[ctverec + 26] = r;
                vertex[ctverec + 27] = g;
                vertex[ctverec + 28] = b;
                vertex[ctverec + 29] = a;

                xS = (float) (x + 1) / pocCtvercuX;
                yS = (float) (y + 1) / pocCtvercuY;
                vertex[ctverec + 30] = xS;
                vertex[ctverec + 31] = yS;
                vertex[ctverec + 32] = r;
                vertex[ctverec + 33] = g;
                vertex[ctverec + 34] = b;
                vertex[ctverec + 35] = a;

                policek++;
            }
        }

        final OGLBuffers.Attrib[] attributes =
        {
            new OGLBuffers.Attrib(jmenoAtributu1, 2),
            new OGLBuffers.Attrib(jmenoAtributu2, 4),
        };

        return new OGLBuffers(gl, vertex, attributes, null);
    }

    public static OGLBuffers vytvorGridMravencu(final GL2 gl, final int n, final String jmenoAtributu1,
            final String jmenoAtributu2, final int pocetMravencu)
    {
        return vytvorGridMravencu(gl, n, n, jmenoAtributu1, jmenoAtributu2, pocetMravencu);
    }

    public static OGLBuffers createGrid(final GL2 gl, final int n, final String attribName)
    {
        return createGrid(gl, n, n, attribName);
    }
}
