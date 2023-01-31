package com.dynarithmic.twain.lowlevel;

public class TW_FRAME extends TwainLowLevel
{
    private TW_FIX32 Left = new TW_FIX32();
    private TW_FIX32 Top = new TW_FIX32();
    private TW_FIX32 Right = new TW_FIX32();
    private TW_FIX32 Bottom = new TW_FIX32();

    public TW_FRAME()
    {
        super();
        this.setBottom(Double.MIN_VALUE).
             setLeft(Double.MIN_VALUE).
             setRight(Double.MIN_VALUE).
             setTop(Double.MIN_VALUE);
    }

    public TW_FIX32 getLeft()
    {
        return Left;
    }

    public TW_FIX32 getTop()
    {
        return Top;
    }

    public TW_FIX32 getRight()
    {
        return Right;
    }

    public TW_FIX32 getBottom()
    {
        return Bottom;
    }

    public TW_FRAME setLeft(TW_FIX32 left)
    {
        Left = left;
        return this;
    }

    public TW_FRAME setTop(TW_FIX32 top)
    {
        Top = top;
        return this;
    }

    public TW_FRAME setRight(TW_FIX32 right)
    {
        Right = right;
        return this;
    }

    public TW_FRAME setBottom(TW_FIX32 bottom)
    {
        Bottom = bottom;
        return this;
    }
    public TW_FRAME setLeft(double left)
    {
        Left.setValue(left);
        return this;
    }

    public TW_FRAME setTop(double top)
    {
        Top.setValue(top);
        return this;
    }

    public TW_FRAME setRight(double right)
    {
        Right.setValue(right);
        return this;
    }

    public TW_FRAME setBottom(double bottom)
    {
        Bottom.setValue(bottom);
        return this;
    }

    public boolean isFrameValid()
    {
        return Left.getValue() != Double.MIN_VALUE &&
               Right.getValue() != Double.MIN_VALUE &&
               Top.getValue() != Double.MIN_VALUE &&
               Bottom.getValue() != Double.MIN_VALUE;
    }
}
