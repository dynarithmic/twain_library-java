package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.DTwainConstants.AcquireType;

public class JNITwainAcquireNativeOptions extends JNITwainAcquireOptions
{
    public JNITwainAcquireNativeOptions()
    {
        super.setAcquireType(AcquireType.NATIVE);
    }
}
