/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

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

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.lowlevel.*;

/**
 *
 * <p>The DTwainAcquireArea class describes the area of interest of the page / image that
 * will be acquired from the device.
 *
 * The area of interest is described by the coordinates (top, left) and (bottom, right), denoting
 * the rectangular area of the image that will be acquired.  The units used (inches, centimeters, etc.)
 * will always be the current unit setting for the device.  The units can be set by calling
 * setUnitOfMeasure().
 *  */
public class TwainAcquireArea extends TW_FRAME
{
    private int unitOfMeasure;

    public double left()
    {
        return super.getLeft().getValue();
    }

    public TwainAcquireArea setLeft(double left)
    {
        super.setLeft(left);
        return this;
    }

    public double top()
    {
        return super.getTop().getValue();
    }

    public TwainAcquireArea setTop(double top)
    {
        super.setTop(top);
        return this;
    }

    public double right()
    {
        return super.getRight().getValue();
    }

    public TwainAcquireArea setRight(double right)
    {
        super.setRight(right);
        return this;
    }

    public double bottom()
    {
        return super.getBottom().getValue();
    }

    public TwainAcquireArea setBottom(double bottom)
    {
        super.setBottom(bottom);
        return this;
    }

    public TwainAcquireArea(double l, double t, double r, double b)
    {
        setLeft(l);
        setRight(r);
        setTop(t);
        setBottom(b);
        unitOfMeasure = DTwainConstants.MeasureUnit.INCHES.value();
    }

    public TwainAcquireArea(double l, double t, double r, double b, int unit)
    {
        setLeft(l);
        setRight(r);
        setTop(t);
        setBottom(b);
        unitOfMeasure = unit;
    }

    public TwainAcquireArea()
    {
        unitOfMeasure = DTwainConstants.MeasureUnit.INCHES.value();
    }

    public TwainAcquireArea(TwainAcquireArea a)
    {
        setLeft(a.left());
        setTop(a.top());
        setRight(a.right());
        setBottom(a.bottom());
        unitOfMeasure = a.unitOfMeasure;
    }

    public TwainAcquireArea(TW_FRAME frame)
    {
        setLeft(frame.getLeft().getValue());
        setTop(frame.getTop().getValue());
        setRight(frame.getRight().getValue());
        setBottom(frame.getBottom().getValue());
        unitOfMeasure = DTwainConstants.MeasureUnit.INCHES.value();
    }

    public TwainAcquireArea(TW_FRAME frame, int unitMeasure)
    {
        setLeft(frame.getLeft().getValue());
        setTop(frame.getTop().getValue());
        setRight(frame.getRight().getValue());
        setBottom(frame.getBottom().getValue());
        unitOfMeasure = unitMeasure;
    }

    public TwainAcquireArea setAll(double l, double t, double r, double b)
    {
        setLeft(l);
        setRight(r);
        setTop(t);
        setBottom(b);
        return this;
    }

    public TwainAcquireArea setAll(double l, double t, double r, double b, int unit)
    {
        setLeft(l);
        setRight(r);
        setTop(t);
        setBottom(b);
        unitOfMeasure = unit;
        return this;
    }

    public int getUnitOfMeasure()
    {
        return unitOfMeasure;
    }

    public TwainAcquireArea setUnitOfMeasure(int unitOfMeasure)
    {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    public boolean isAcquireAreaUsed()
    {
        return super.isFrameValid();
    }
}
