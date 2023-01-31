package com.dynarithmic.twain.highlevel;

public class TwainFrame<T>
{
    private T left;
    private T top;
    private T right;
    private T bottom;

    public TwainFrame(T l, T t, T r, T b)
    {
        left = l;
        top = t;
        right = r;
        bottom = b;
    }

    public T getLeft() { return left; }
    public T getTop() { return top; }
    public T getRight() { return right; }
    public T getBottom() { return bottom; }

    public TwainFrame<T> setLeft(T left) { this.left = left; return this; }
    public TwainFrame<T> setTop(T top) { this.top = top; return this; }
    public TwainFrame<T> setRight(T right) { this.right = right; return this; }
    public TwainFrame<T> setBottom(T bottom) { this.bottom = bottom; return this; }
}
