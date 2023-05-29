package com.dtwain.demos;

import java.util.Scanner;

import com.dynarithmic.twain.DTwainGlobalOptions;
import com.dynarithmic.twain.highlevel.TwainSession;

public class SetJNIVersionDemo
{
    // Simple acquire to a file
    public void run() throws Exception
    {
        System.out.println("Please choose the JNI to use for your application:\n");
        System.out.println("  1. 32-bit ANSI");
        System.out.println("  2. 32-bit Unicode");
        System.out.println("  3. 64-bit ANSI");
        System.out.println("  4. 64-bit Unicode");
        System.out.println("  5. 32-bit ANSI Debug");
        System.out.println("  6. 32-bit Unicode Debug");
        System.out.println("  7. 64-bit ANSI Debug");
        System.out.println("  8. 64-bit Unicode Debug");
        System.out.print("(1 - 8): ");

        @SuppressWarnings("resource")
        Scanner input = new Scanner(System.in);
        int jniToUse = input.nextInt();
        if ( jniToUse < 1 || jniToUse > 8)
            System.out.println("Invalid choice");
        else
        {
            // Set the JNI version
            DTwainGlobalOptions.setJNIVersion(jniToUse - 1);

            // Start a TWAIN session
            TwainSession twainSession = new TwainSession();

            // Verify these are the DLLs being used
            System.out.println("The DTWAIN DLL in use: " + twainSession.getDTwainPath());
            System.out.println("The JNI Version: " + DTwainGlobalOptions.getJNIVersionAsString());

            // Close down the TWAIN Session
            twainSession.stop();
        }
    }

    public static void main(String [] args)
    {
        SetJNIVersionDemo s = new SetJNIVersionDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}