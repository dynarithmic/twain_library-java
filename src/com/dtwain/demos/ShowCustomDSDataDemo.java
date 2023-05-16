package com.dtwain.demos;

import java.nio.charset.StandardCharsets;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;

public class ShowCustomDSDataDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();
        twainSession.start();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            byte [] customDSData = ts.getCustomDSData();
            System.out.println("The number of bytes of custom ds data is: " + customDSData.length);
            System.out.println("The custom data for this data source is: ");
            String s = new String(customDSData, StandardCharsets.UTF_8);
            System.out.println(s);
        }
        else
        {
            // See why no acquisition was done
            ErrorCode err = ts.getLastError();

            // The user didn't make a selection
            if (err == ErrorCode.ERROR_SOURCESELECTION_CANCELED)
                System.out.println("User closed the TWAIN dialog without selecting a data source");
            else
            // User selected, but something went wrong in opening the data source
                System.out.println("Source selection failed with error: " + err);
        }

        // Close down the TWAIN Session
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        ShowCustomDSDataDemo s = new ShowCustomDSDataDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}