/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2026 Dynarithmic Software.

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

import com.dynarithmic.twain.DTwainConstants.ComponentPath;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class SimpleFileAcquireJPEGXLDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "c:\\dtwain_javatest\\";
     
    public void run() throws Exception
    {
        // Allows runtime choice of choosing which JNI DLL is loaded.
        ConsoleJNISelector.setJNIVersion(getClass().getSimpleName());
        
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();
        
        // Set the path to the libjxl external libraries.
        // The setComponentPath() function sets the path to empty, which will use the
        // Windows OS search logic to find the libjxl components.

        // If the libjxl components are in a specific directory,
        // replace the second argument with the path of the components.
        // Note that the 32-bit libjxl components should reside in a different folder
        // than the 64-bit libjxl components.  Thus your program should adjust the second
        // argument depending on whether the 32-bit JVM or 64-bit JVM is used.
        
        // See https://github.com/dynarithmic/twain_library/blob/master-staging/JPEGXL_Installation.md
        // for more information on installation of the JPEGXL components
        twainSession.setComponentPath(ComponentPath.COMPONENT_JPEGXL, ""); // Use Windows search logic
        
        // Check if the components are available
        if ( !twainSession.isFileTypeSupported(FileType.JPEGXL))
		{
        	System.out.println("JPEG-XL library components not found");
        	return;
		}

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = EnhancedSourceSelector.selectSource(twainSession);
        if ( ts.isOpened() )
        {
            // Set the file acquire options. By default, the file will be in TIFF-LZW format
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setType(FileType.JPEGXL).
               setName(outDir + "testjxl.jxl");

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
        twainSession.stop();
    }

    public static void main(String [] args)
    {
    	SimpleFileAcquireJPEGXLDemo s = new SimpleFileAcquireJPEGXLDemo();
        try
        {
            s.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}