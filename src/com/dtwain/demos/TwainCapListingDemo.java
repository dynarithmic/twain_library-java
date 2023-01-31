package com.dtwain.demos;
import java.util.List;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;

public class TwainCapListingDemo
{
    private TwainSource twainSource = null;

    private void listCaps() throws IllegalArgumentException
    {
       System.out.println("Source selected: " + twainSource.getInfo().getProductName());

       // get all the supported capabilities of the device
       CapabilityInterface theCaps = twainSource.getCapabilityInterface();

       // for each one, output the name of the capability along with the value (in parentheses)
       List<Integer> allCaps = theCaps.getCaps();
       for (int cap : allCaps)
           System.out.println(CapabilityInterface.getNameFromCap(cap) + " (" + cap + ")");
    }

    public void runDemo() throws DTwainJavaAPIException, Exception
    {
        TwainSession twSession = new TwainSession();
        // Select a TWAIN device and crDTwainJavaAPISourcenSource helper object using the returned
        // TWAIN Source (which is a long type), and the interface we initialized above.
        twainSource = twSession.selectSource();
        if ( twainSource.isOpened())
        {
            // list the caps
            listCaps();
        }
        twSession.stop();
    }

    public static void main(String[] args) throws DTwainJavaAPIException, Exception
    {
        TwainCapListingDemo simpleProg = new TwainCapListingDemo();
        simpleProg.runDemo();

        // must be called, since TWAIN dialog is a Swing component
        // and AWT thread was started
        System.exit(0);
    }
}