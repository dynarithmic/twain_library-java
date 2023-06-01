/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS.

 */
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