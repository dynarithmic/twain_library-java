package com.dtwain.demos;

import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;

public class TinyApplication
{
    public static void main(String[] args)
    {
        try
        {
            // Open a session
            TwainSession session = new TwainSession();

            // Session will start
            TwainSource source = session.selectSource();
            if ( source.isOpened() )
                // acquire to a BMP file to the current working directory, with name "test.bmp"
                // To set the name, see the SimpleFileAcquireBMPDemo.java demo
                source.acquire();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
