package com.dynarithmic.twain.highlevel;

public class RGBColor
{
    int R;
    int G;
    int B;

    public RGBColor()
    {
        setColor(0,0,0);
    }

    public RGBColor(int R, int G, int B)
    {
        setColor(R, G, B);
    }

    public RGBColor setColor(int R, int G, int B)
    {
        this.R = R;
        this.G = G;
        this.B = B;
        return this;
    }

    public int getR()
    {
        return R;
    }

    public RGBColor setR(int r)
    {
        R = r;
        return this;
    }
    public int getG()
    {
        return G;
    }

    public RGBColor setG(int g)
    {
        G = g;
        return this;
    }

    public int getB()
    {
        return B;
    }

    public RGBColor setB(int b)
    {
        B = b;
        return this;
    }
}
