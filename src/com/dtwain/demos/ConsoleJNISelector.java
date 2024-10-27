/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

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

public class ConsoleJNISelector 
{
    public static boolean setJNIVersion(String appName)
    {
        boolean choiceOk = false;
        @SuppressWarnings("resource")
        Scanner input = new Scanner(System.in);
        while (!choiceOk)
        {
            System.out.println("Please choose the JNI to use for " + appName);
            System.out.println("  0. Use current JNI (must match same bit-setting as JRE)");
            System.out.println("  1. 32-bit ANSI (running JRE must be 32-bit)");
            System.out.println("  2. 32-bit Unicode (running JRE must be 32-bit)");
            System.out.println("  3. 64-bit ANSI (running JRE must be 64-bit)");
            System.out.println("  4. 64-bit Unicode (running JRE must be 64-bit)");
            System.out.println("  5. 32-bit ANSI Debug (running JRE must be 32-bit)");
            System.out.println("  6. 32-bit Unicode Debug (running JRE must be 32-bit)");
            System.out.println("  7. 64-bit ANSI Debug (running JRE must be 64-bit)");
            System.out.println("  8. 64-bit Unicode Debug (running JRE must be 64-bit)");
            System.out.println("  9. Exit application " + appName);
            System.out.print("(0 - 9): ");
    
            int jniToUse = input.nextInt();
            if ( jniToUse == 0 )
                return true;
            if ( jniToUse == 9)
            {
                System.exit(0);
            }
            if ( jniToUse < 0 || jniToUse > 8)
                System.out.println("Invalid choice");
            else
            {
                choiceOk = true;
                // Set the JNI version
                DTwainGlobalOptions.setJNIVersion(jniToUse - 1);
            }
        }
        input.close();
        System.out.println("Starting " + appName + "...");
        return true;
    }
}
