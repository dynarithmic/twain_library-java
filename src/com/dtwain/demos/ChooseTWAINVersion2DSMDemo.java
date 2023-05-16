package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.DSMType;
import com.dynarithmic.twain.DTwainConstants.SessionStartupMode;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;

public class ChooseTWAINVersion2DSMDemo 
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        DSMType dsmToTest [] = { DSMType.LEGACY, DSMType.VERSION2 };

        TwainSession twainSession = new TwainSession(SessionStartupMode.NONE);
        
        for (int i = 0; i < 2; ++i)
        {
            // Set the TWAIN Data Source Manager to the version we
            // are using.
            // This must be done before starting a TWAIN session,
            // or if changed while a session has started, the session
            // must be restarted to reflect the change in the DSM being used.
            twainSession.setDSM(dsmToTest[i]);
        
            // Start a TWAIN session
            twainSession.start();

            if (twainSession.isStarted())
            {
                System.out.println("TWAIN DSM Path in use: " + twainSession.getDSMPath());
                TwainSource twainSource = twainSession.selectSource();
                if ( twainSource.isSelected())
                    twainSource.close();
            }
            twainSession.stop();
        }
    }
    
    public static void main(String[] args) 
    {
        ChooseTWAINVersion2DSMDemo demo = new ChooseTWAINVersion2DSMDemo();
        try
        {
            demo.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
