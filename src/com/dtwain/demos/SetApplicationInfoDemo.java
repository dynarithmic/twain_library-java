package com.dtwain.demos;

import com.dynarithmic.twain.highlevel.TwainAppInfo;
import com.dynarithmic.twain.highlevel.TwainCharacteristics;
import com.dynarithmic.twain.highlevel.TwainSession;

public class SetApplicationInfoDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Create the TWAIN characteristics that allows a TwainSession to
        // be created, but not started.
        TwainCharacteristics tc = new TwainCharacteristics();

        // Before we start a TWAIN session, let's set the application information.
        // Setting the application info allows the TWAIN Data Source Manager to
        // use these names when logging, displaying Source UI's, etc.
        TwainAppInfo appInfo = tc.getAppInfo();
        appInfo.setManufacturer(this.getClass().getName());
        appInfo.setProductName(this.getClass().getName());

        // Create a TWAIN session that uses our characteristics
        TwainSession twainSession = new TwainSession(tc);

        // Now the underlying TWAIN system will use our application information for
        // things like logging.

        // Close down the TWAIN Session
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        SetApplicationInfoDemo s = new SetApplicationInfoDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}