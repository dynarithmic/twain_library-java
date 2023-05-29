package com.dtwain.demos;

import java.util.List;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSourceInfo;

public class BasicTwainInfoDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Get info concerning this TWAIN session
        System.out.println("DTWAIN Short Version Info: " + twainSession.getShortVersionName());
        System.out.println("DTWAIN Long Version Info: " + twainSession.getLongVersionName());
        System.out.println("DTWAIN Library Path: " + twainSession.getDTwainPath());
        System.out.println("TWAIN DSM Path in use: " + twainSession.getDSMPath());
        System.out.println("TWAIN Version and Copyright: " + twainSession.getVersionCopyright());
        
        // Get information on the installed TWAIN sources
        System.out.println();
        System.out.println("Available TWAIN Sources:");
        List<TwainSourceInfo> sInfo = twainSession.getAllSourceInfo();
        for ( TwainSourceInfo oneInfo : sInfo )
            System.out.println("   Product Name: " + oneInfo.getProductName());

        // Get a JSON describing the session and all available TWAIN sources
        String details = twainSession.getSessionDetails(2, true);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Here are the details of the TWAIN session:");
        System.out.println(details);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------");
        
        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            String sourceName = ts.getInfo().getProductName();
            String sourceDetails = twainSession.getSourceDetails(sourceName,  2, true);
            System.out.println("Here are the details of the selected TWAIN Source: \"" + sourceName);
            System.out.println(sourceDetails);
        }
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        BasicTwainInfoDemo info = new BasicTwainInfoDemo();
        try
        {
            info.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}