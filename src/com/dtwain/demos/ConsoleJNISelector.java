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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainGlobalOptions;

public class ConsoleJNISelector 
{
    public static void setJNIVersion(String appName)
    {
        class Choice
        {
            String message;
            int jniVersion;
            public Choice(String message, int jniVersion)
            {
                this.message = message;
                this.jniVersion = jniVersion;
            }
        }
        
        List<Choice> arrList = new ArrayList<>();
        boolean is64Bit = DTwainGlobalOptions.Is64BitArchitecture();
        if ( is64Bit )
        {
            arrList.add(new Choice("64-bit default (Unicode)",DTwainConstants.JNIVersion.JNI_64U));
            arrList.add(new Choice("64-bit Unicode",DTwainConstants.JNIVersion.JNI_64U));
            arrList.add(new Choice("64-bit ANSI",DTwainConstants.JNIVersion.JNI_64));
            arrList.add(new Choice("64-bit Debug Unicode",DTwainConstants.JNIVersion.JNI_64UD));
            arrList.add(new Choice("64-bit Debug ANSI",DTwainConstants.JNIVersion.JNI_64D));
        }
        else
        {
            arrList.add(new Choice("32-bit default (Unicode)",DTwainConstants.JNIVersion.JNI_32U));
            arrList.add(new Choice("32-bit Unicode",DTwainConstants.JNIVersion.JNI_32U));
            arrList.add(new Choice("32-bit ANSI",DTwainConstants.JNIVersion.JNI_32));
            arrList.add(new Choice("32-bit Debug Unicode",DTwainConstants.JNIVersion.JNI_32UD));
            arrList.add(new Choice("32-bit Debug ANSI",DTwainConstants.JNIVersion.JNI_32D));
        }
        
        System.out.println("");
        boolean choiceOk = false;
        Scanner input = new Scanner(System.in);
        while (!choiceOk)
        {
            int nChoice = 1;
            try
            {
                System.out.println("Please choose the JNI to use for " + appName);
                for (int i = 0; i < 5; ++i)
                {
                    System.out.println("  " + nChoice + ". " + arrList.get(i).message);
                    ++nChoice;
                }
                System.out.println("  6. Exit application " + appName);
                System.out.print("Press Enter for default or enter 1 - 6: ");
                String inputLine = input.nextLine();
                if ( inputLine.equals("") )
                {
                    DTwainGlobalOptions.setJNIVersion(arrList.get(0).jniVersion);
                    break;
                }
                try 
                {
                    int jniToUse = Integer.parseInt(inputLine);
                    if ( jniToUse < 1 || jniToUse > 6)
                        System.out.println("Invalid choice");
                    else
                    if ( jniToUse == 6)
                        System.exit(0);
                    else
                    {
                        DTwainGlobalOptions.setJNIVersion(arrList.get(jniToUse - 1).jniVersion);
                        break;
                    }
                }
                catch( NumberFormatException e)
                {
                    System.out.println("Invalid choice");
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
                System.exit(0);;
            }
        }
        input.close();
        System.out.println("Starting " + appName + "...");
    }
}
