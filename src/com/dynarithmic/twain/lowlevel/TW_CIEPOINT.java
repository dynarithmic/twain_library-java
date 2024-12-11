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
package com.dynarithmic.twain.lowlevel;

public class TW_CIEPOINT extends TwainLowLevel
{
    private TW_FIX32 X = new TW_FIX32();
    private TW_FIX32 Y = new TW_FIX32();
    private TW_FIX32 Z = new TW_FIX32();

    public TW_CIEPOINT() {}

    public TW_FIX32 getX()
    {
        return X;
    }

    public TW_FIX32 getY()
    {
        return Y;
    }

    public TW_FIX32 getZ()
    {
        return Z;
    }

    public TW_CIEPOINT setX(TW_FIX32 x)
    {
        X = x;
        return this;
    }

    public TW_CIEPOINT setY(TW_FIX32 y)
    {
        Y = y;
        return this;
    }

    public TW_CIEPOINT setZ(TW_FIX32 z)
    {
        Z = z;
        return this;
    }

    public TW_CIEPOINT setX(double x)
    {
        X.setValue(x);
        return this;
    }

    public TW_CIEPOINT setY(double y)
    {
        Y.setValue(y);
        return this;
    }

    public TW_CIEPOINT setZ(double z)
    {
        Z.setValue(z);
        return this;
    }
}
