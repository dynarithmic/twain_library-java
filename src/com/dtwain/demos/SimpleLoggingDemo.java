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
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.SessionStartupMode;
import com.dynarithmic.twain.highlevel.EnhancedSourceSelector;
import com.dynarithmic.twain.highlevel.TwainConsoleLogger;
import com.dynarithmic.twain.highlevel.TwainFileLogger;
import com.dynarithmic.twain.highlevel.TwainLogger;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class SimpleLoggingDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";
    // Simple acquire to a file
    public void run() throws Exception
    {
        // Create a TWAIN Session without starting it.  We want to log 
        // what happens when TWAIN starts up
        TwainSession twainSession = new TwainSession(SessionStartupMode.NONE);
        twainSession.enableLogging(true);

        // Set up logging to a file and to the console
        TwainLogger logging = TwainSession.getLogger();
        logging.setVerbosity(TwainLogger.LoggerVerbosity.MAXIMUM).
                addLogger(new TwainFileLogger("newlog.log")).  // Log to a file names newlog.log
                addLogger(new TwainConsoleLogger());          // Log to the system console

        // Now start the session, which will also start the logger
        twainSession.start();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = EnhancedSourceSelector.selectSource(twainSession);
        twainSession.stopLogging();
        if ( ts.isOpened() )
        {
            // Set the file acquire options. By default, the file will be in BMP format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setName(outDir + "test3.bmp");

            // Start the acquisition
            AcquireReturnInfo retInfo = ts.acquire();
            if ( retInfo.getReturnCode() == ErrorCode.ERROR_NONE )
                System.out.println("Acquisition Successful");
            else
                System.out.println("Acquisition Failed with error: " + retInfo.getReturnCode());
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
        twainSession.startLogging();
        twainSession.stop();
    }

    public static void main(String [] args)
    {
        SimpleLoggingDemo s = new SimpleLoggingDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}