package com.dtwain.demos;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.DTwainConstants.TextPageDisplayOptions;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.PDFTextElement;
import com.dynarithmic.twain.highlevel.RGBColor;
import com.dynarithmic.twain.highlevel.TwainCallback;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PDFOptions;

public class PDFAddTextToPageDemo
{
    // Change this to the output directory that fits your environment
    static public String outDir = "";

    public class PDFCallback extends TwainCallback
    {
        TwainSession twainSession = null;
        TwainSource twainSource = null;
        PDFTextElement textElement = new PDFTextElement();
        int pageCount = 1;

        public PDFCallback()
        {
            // Set the defaults for the text element that we will use.
            textElement.setFontName("Helvetica").
                        setXPosition(100).
                        setYPosition(100).
                        setFontSize(14).
                        setScaling(100).
                        setRGBColor(new RGBColor(255,0,0)).setPageDisplayOptions(TextPageDisplayOptions.CURRENTPAGE);
        }

        @Override
        public int onFilePageSaving(TwainSource sourceHandle)
        {
            // The lower left portion of the page will have "Page x",
            // where "x" is the page number.
            textElement.setText("Page " + pageCount);
            // Write the PDF info
            try
            {
                twainSource.writePDFTextElement(textElement);
            }
            catch (DTwainJavaAPIException e)
            {
                System.out.println("Could not add text");
            }
            ++pageCount;
            return 1;
        }
    }

    // Acquire to a PDF file, with encryption settings turned on
    public void run() throws Exception
    {
        // Start a TWAIN session
        TwainSession twainSession = new TwainSession();

        // Select a TWAIN Source using the Select Source dialog
        TwainSource ts = twainSession.selectSource();
        if ( ts.isOpened() )
        {
            // TwainCallback
            twainSession.registerCallback(ts, new PDFCallback());

            // Set the file acquire options to a multipage PDF.
            ts.getAcquireCharacteristics().
               getFileTransferOptions().
               setType(FileType.PDFMULTI).
               setName(outDir + "testmulti.pdf");

            // Get the PDF options for this device
            PDFOptions pdfOptions = ts.getAcquireCharacteristics().getPDFOptions();
            // Set the author, subject, and title
            pdfOptions.setAuthor("My Java App").
                       setSubject("Testing Text On Page").
                       setTitle("Java Demo of DTWAIN");

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
        PDFAddTextToPageDemo demo = new PDFAddTextToPageDemo();
        try
        {
            demo.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}