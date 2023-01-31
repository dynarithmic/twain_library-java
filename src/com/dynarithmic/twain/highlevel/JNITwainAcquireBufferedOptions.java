package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.DTwainConstants.AcquireType;

public class JNITwainAcquireBufferedOptions extends JNITwainAcquireOptions
{
    public JNITwainAcquireBufferedOptions()
    {
        super.setAcquireType(AcquireType.BUFFERED);
    }
}
