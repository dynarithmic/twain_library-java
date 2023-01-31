package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.DTwainConstants.PixelType;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.*;

public class JNITwainAcquireOptions extends GeneralOptions
{
    private boolean showUI = true;
    private PixelType pixelType = PixelType.DEFAULT;

    public boolean isShowUI()
    {
        return showUI;
    }

    public PixelType getPixelType()
    {
        return pixelType;
    }

    public JNITwainAcquireOptions setShowUI(boolean showUI)
    {
        this.showUI = showUI;
        return this;
    }

    public JNITwainAcquireOptions setPixelType(PixelType pixelType)
    {
        this.pixelType = pixelType;
        return this;
    }

}
