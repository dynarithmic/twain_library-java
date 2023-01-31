package com.dtwain.demos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.SupportedFileTypeInfo;
import com.dynarithmic.twain.highlevel.TwainAppInfo;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.GeneralOptions;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.DTwainConstants.PixelType;
class TestImageFileCreation
{
    TwainSession twSession = null;
    @SuppressWarnings("serial")
    private static Set<FileType> sBlackWhite = new HashSet<FileType>() {
        {
            add(FileType.TIFFG3);
            add(FileType.TIFFG4);
            add(FileType.TIFFG3MULTI);
            add(FileType.TIFFG4MULTI);
            add(FileType.TEXT);
            add(FileType.TEXTMULTI);
        }
    };

    private void TestMultiOrSingleFile(String outDir, boolean bTestSingle) throws Exception
    {

        String outDirT = outDir;

        // select a source
        TwainSource twainSource = twSession.selectSource();
        if ( twainSource.isOpened())
        {
            List<Integer> allTypes = null;
            if ( bTestSingle )
            {
                allTypes = twSession.getSinglePageFileTypes();
                outDirT += "Single\\";
            }
            else
            {
                allTypes = twSession.getMultiPageFileTypes();
                outDirT += "Multi\\";
            }

            // write all the temporary files to the output directory
            twSession.setTemporaryDirectory(outDirT);
            String filePrefix = outDirT;

            AcquireCharacteristics ac = twainSource.getAcquireCharacteristics();

            //  Set the base file options for all file types
            FileTransferOptions fc = ac.getFileTransferOptions().enableAutoCreateDirectory(true);

            // Set the general options for all file types
            GeneralOptions gOpts = ac.getGeneralOptions().setMaxPageCount(bTestSingle?1:2);

            // turn off the user interface
            ac.getUserInterfaceOptions().showUI(false);

            // loop through all of the available file types
            for (Integer i : allTypes)
            {
                // get the image type that will be saved
                SupportedFileTypeInfo fileInfo = twSession.getSupportedFileTypeInfo(i);

                // Create the base of the file name
                // we use the first extension of an image file can
                // have multiple extensions
                String extToUse = fileInfo.getExtensions().get(0);

                // now create the file name
                String fileName = filePrefix + fileInfo.getName() + "." + extToUse;

                // Set the name and type
                FileType ft = fileInfo.getType();
                fc.setName(fileName).setType(fileInfo.getType());

                // Make sure pixel type is black/white for the corresponding
                // file type (TIFF G3, TIFF g4, etc.)
                PixelType pt = PixelType.DEFAULT;
                if (sBlackWhite.contains(ft) )
                    pt = PixelType.BW;

                // Set the pixel type
                gOpts.setPixelType(pt);

                // Start the acquisition
                twainSource.acquire();

                // Output information
                System.out.println(fileInfo.getName() + " " + fileName);
            }
        }
    }

    public void runDemo(String[] args) throws Exception
    {
        if ( args.length < 2)
        {
            System.out.println("Usage: TestImageFileCreation test-to-run[1,2,3] output-directory");
            System.out.println();
            System.out.println("1 --> Single page files\n2 --> Multipage files\n3 --> Single and multipage files");
            System.out.println();
            System.out.println("Example:\n    TestImageFileCreation 1 c:\\saved_images");
            return;
        }

        int value = Integer.valueOf(args[0]);
        String outDir = args[1];
        if ( !outDir.endsWith("\\"))
            outDir += "\\";

        twSession = new TwainSession();

        TwainAppInfo appInfo = new TwainAppInfo();
        appInfo.setProductName("TestImageFileCreation");
        twSession.setAppInfo(appInfo);
        twSession.start();

        switch (value)
        {
            case 1:
                TestMultiOrSingleFile(outDir, true);
                break;
            case 2:
                TestMultiOrSingleFile(outDir, false);
                break;
            default:
                TestMultiOrSingleFile(outDir, true);
                TestMultiOrSingleFile(outDir, false);
                break;
        }
        twSession.stop();
    }

    public static void main(String[] args) throws DTwainJavaAPIException, Exception
    {
        TestImageFileCreation imageProg = new TestImageFileCreation();
        imageProg.runDemo(args);

        // must be called, since TWAIN dialog is a Swing component
        // and AWT thread was started
        System.exit(0);
    }
}
