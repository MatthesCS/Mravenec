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
        List<Vec2D> vertex = new ArrayList<>();
        List<Integer> index = new ArrayList<>();

        for (int y = 0; y < pocCtvercuY; y++)
        {
            for (int x = 0; x < pocCtvercuX; x++)
            {
                float p1 = (pocetMravencu - mravencu);
                float p2 = (pocCtvercuX * pocCtvercuY) - policek;
                pravdepodobnostMravence = p1 / p2 * 100;
                float r;
                float g;
                if (mravencu < pocetMravencu && ((float) rand.nextInt(10000) / 100) < pravdepodobnostMravence)
                {
                    r = (float) 1;
                    g = (float) 0;
                    mravencu++;
                } else
                {
                    r = (float) 0;
                    g = (float) 1;
                }
                float a = (float) x / pocCtvercuX;
                float b = (float) y / pocCtvercuY;
                vertex.add(new Vec2D(a, b));
                vertex.add(new Vec2D(r, g));

                a = (float) (x + 1) / pocCtvercuX;
                b = (float) y / pocCtvercuY;
                vertex.add(new Vec2D(a, b));
                vertex.add(new Vec2D(r, g));

                a = (float) x / pocCtvercuX;
                b = (float) (y + 1) / pocCtvercuY;
                vertex.add(new Vec2D(a, b));
                vertex.add(new Vec2D(r, g));

                a = (float) (x + 1) / pocCtvercuX;
                b = (float) y / pocCtvercuY;
                vertex.add(new Vec2D(a, b));
                vertex.add(new Vec2D(r, g));

                a = (float) x / pocCtvercuX;
                b = (float) (y + 1) / pocCtvercuY;
                vertex.add(new Vec2D(a, b));
                vertex.add(new Vec2D(r, g));

                a = (float) (x + 1) / pocCtvercuX;
                b = (float) (y + 1) / pocCtvercuY;
                vertex.add(new Vec2D(a, b));
                vertex.add(new Vec2D(r, g));

                policek++;
            }
        }

        final OGLBuffers.Attrib[] attributes =
        {
            new OGLBuffers.Attrib(jmenoAtributu1, 2),
            new OGLBuffers.Attrib(jmenoAtributu2, 2),
        };

        float[] vertexData = ToFloatArray.convert(vertex);

        return new OGLBuffers(gl, vertexData, attributes, null);
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
