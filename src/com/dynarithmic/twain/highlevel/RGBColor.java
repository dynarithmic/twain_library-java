/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS.

 */
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
