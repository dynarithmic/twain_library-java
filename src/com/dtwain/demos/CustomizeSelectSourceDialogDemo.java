/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

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

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSourceDialog;

public class CustomizeSelectSourceDialogDemo
{
    TwainSession twainSession = null;

    private void showResults(TwainSource ts, String info) throws DTwainJavaAPIException
    {
        if ( ts.isOpened())
        {
            System.out.println("The source " + ts.getInfo().getProductName() + " was successfully opened!" + info);
            ts.close();
        }
        else
        {
            System.out.println("Unable to open the source: " + twainSession.getLastError());
            System.out.println(twainSession.getErrorString(twainSession.getLastError()));
        }
    }

    public CustomizeSelectSourceDialogDemo() throws Exception
    {
        twainSession = new TwainSession();
    }

    // Select a TWAIN Source using a no-frills Select Source Dialog
    public void selectSourceDemo1() throws Exception
    {
        TwainSource ts = twainSession.selectSource();
        showResults(ts, " (TWAIN Select Source dialog used) ");
    }

    // Select a TWAIN Source using a centered dialog, with names sorted and custom title
    public void selectSourceDemo2(String title) throws Exception
    {
        TwainSourceDialog selectorOptions = new TwainSourceDialog();
        selectorOptions.enableEnhancedDialog(true).
                        center(true).
                        sortNames(true).
                        topmostWindow(true).
                        setTitle(title);
        TwainSource ts = twainSession.selectSource(selectorOptions);
        showResults(ts, " (TWAIN Customized Select Source dialog used) ");
    }

    // Select the default TWAIN Source.  The default is usually the last source opened, or
    // if there is only one source, that source is automatically opened
    public void selectSourceDemo3() throws Exception
    {
        TwainSource ts = twainSession.selectDefaultSource();
        showResults(ts, " (Default source was opened) ");
    }

    // Select a TWAIN Source using the French dialog
    public void selectSourceDemo4() throws Exception
    {
        boolean ok = twainSession.setLanguageResource("French");
        if ( ok )
            selectSourceDemo2("French Dialog");
        twainSession.setLanguageResource("English");
    }

    // Select a TWAIN Source using a name provided as the first argument to this demo
    public void selectSourceDemo5(String sourceName) throws Exception
    {
        TwainSource ts = twainSession.selectSource(sourceName);
        showResults(ts, " (TWAIN Source selected by name) ");
    }

    public static void main(String [] args)
    {
        // Allows runtime choice of choosing which JNI DLL is loaded.
        ConsoleJNISelector.setJNIVersion("CustomizeSelectSourceDialogDemo");
        
        CustomizeSelectSourceDialogDemo s;
        try
        {
            s = new CustomizeSelectSourceDialogDemo();
            s.selectSourceDemo1();
            s.selectSourceDemo2("Custom Title");
            s.selectSourceDemo3();
            s.selectSourceDemo4();
            if ( args.length > 0)
                s.selectSourceDemo5(args[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}