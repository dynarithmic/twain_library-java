package com.dynarithmic.twain.highlevel.acquirecharacteristics;

public class BufferedTransferOptions
{
    private boolean enabled = false;

    public BufferedTransferOptions enable(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    public boolean isEnabled()
    {
        return this.enabled;
    }
}
