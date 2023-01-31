package com.dtwain.demos;

import java.util.List;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSourceInfo;

public class TwainSessionInfoDemo
{
    // Simple acquire to a file
    public void getSessionInfo() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Get info concerning this TWAIN session
        System.out.println("DTWAIN Short Version Info: " + twainSession.getDTwainVersionInfo().getShortVersionName());
        System.out.println("DTWAIN Long Version Info: " + twainSession.getDTwainVersionInfo().getLongVersionName());
        System.out.println("DTWAIN Library Path: " + twainSession.getDTwainVersionInfo().getExecutionPath());
        System.out.println("TWAIN DSM Path in use: " + twainSession.getDSMPath());

        // Get information on the installed TWAIN sources
        System.out.println();
        System.out.println("Available TWAIN Sources:");
        List<TwainSourceInfo> sInfo = twainSession.getAllSourceInfo();
        for ( TwainSourceInfo oneInfo : sInfo )
            System.out.println("   Product Name: " + oneInfo.getProductName());

        List<Integer> allTypes = twainSession.getSinglePageFileTypes();
        allTypes.addAll(twainSession.getMultiPageFileTypes());
        for (Integer fType : allTypes)
        {
            System.out.println(twainSession.getPageFileTypeName(fType) + "  Extension(s): " +
                               twainSession.getPageFileTypeExtensions(fType));
        }
        // Close down the TWAIN Session
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        TwainSessionInfoDemo info = new TwainSessionInfoDemo();
        try
        {
            info.getSessionInfo();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}