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
