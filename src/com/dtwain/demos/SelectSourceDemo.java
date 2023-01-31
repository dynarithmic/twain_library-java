package com.dtwain.demos;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSourceDialog;

public class SelectSourceDemo
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

    public SelectSourceDemo() throws Exception
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
        SelectSourceDemo s;
        try
        {
            s = new SelectSourceDemo();
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