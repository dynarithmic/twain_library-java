/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

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

public class TwainFrameDouble
{
    private TwainAcquireArea twainAcquireArea = new TwainAcquireArea();

    public TwainFrameDouble()
    {
    }

    public TwainFrameDouble(Double l, Double t, Double r, Double b)
    {
        this.twainAcquireArea.setAll(l, t, r, b);
    }

    public double getLeft()
    {
        return this.twainAcquireArea.left();
    }

    public TwainFrameDouble setLeft(double left)
    {
        this.twainAcquireArea.setLeft(left);
        return this;
    }

    public double getTop()
    {
        return this.twainAcquireArea.top();
    }

    public TwainFrameDouble setTop(double top)
    {
        this.twainAcquireArea.setTop(top);
        return this;
    }

    public double getRight()
    {
        return this.twainAcquireArea.right();
    }

    public TwainFrameDouble setRight(double right)
    {
        this.twainAcquireArea.setRight(right);
        return this;
    }

    public double getBottom()
    {
        return this.twainAcquireArea.bottom();
    }

    public TwainFrameDouble setBottom(double bottom)
    {
        this.twainAcquireArea.setBottom(bottom);
        return this;
    }

    @Override
    public boolean equals(Object tsd)
    {
        if (this == tsd)
            return true;
        if (tsd == null || getClass() != tsd.getClass())
            return false;
        TwainFrameDouble that = (TwainFrameDouble) tsd;
        return that.getBottom() == this.getBottom() &&
                that.getTop() == this.getTop() &&
                that.getLeft() == this.getLeft() &&
                that.getRight() == this.getRight();
    }
}
