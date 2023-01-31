package com.dtwain.demos.fulldemo;
import com.dynarithmic.twain.*;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.BufferedTransferInfo;
import com.dynarithmic.twain.highlevel.TwainImageData;
import com.dynarithmic.twain.highlevel.TwainCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DTwainDemoCallback extends TwainCallback
{
    final DTwainFullDemo m_mainFrame;
    final ByteArrayOutputStream m_compressedStream;
    BufferedTransferInfo m_stripInfo;
    boolean m_CompressionOn;
    File m_CompressedFile = null;

    public void initCompressedStream(BufferedTransferInfo stripInfo)
    {
        m_stripInfo = stripInfo;
        m_compressedStream.reset();
    }

    public void setFileHandle(File f) { m_CompressedFile = f; }

    public void appendBytesToCompressedStream(byte [] b)
    {
        for (int i = 0; i < b.length; ++i)
            m_compressedStream.write(b[i]);
    }

    public ByteArrayOutputStream getStream()
    {
        return m_compressedStream;
    }
    public DTwainDemoCallback(DTwainFullDemo mFrame)
    {
        super();
        m_mainFrame = mFrame;
        m_compressedStream = new ByteArrayOutputStream();
        m_CompressionOn = false;
    }

    public void enableCompression(boolean bSet)
    {
        m_CompressionOn = bSet;
    }

    public boolean isCompressionOn()
    {
        return m_CompressionOn;
    }

    @Override
    public int onTransferStripDone(long sourceHandle)
    {
        if ( m_CompressionOn )
        {
            // get the buffered strip
            try
            {
                DTwainJavaAPI handle = this.m_mainFrame.getTwainInterface();
                if ( handle != null )
                {
                    handle.DTWAIN_GetBufferedStripData(sourceHandle, m_stripInfo.getStripInfo());
                    appendBytesToCompressedStream(m_stripInfo.getStripInfo().getBufferedStripData());
                }
            }
            catch (DTwainJavaAPIException e)
            {
            }
        }
        return 1;
    }

    @Override
    public int onTransferDone(long sourceHandle)
    {
        DTwainJavaAPI handle = this.m_mainFrame.getTwainInterface();
        if ( m_CompressionOn )
        {
            // get the buffered strip
            try
            {
                if ( handle != null )
                {
                    handle.DTWAIN_GetBufferedStripData(sourceHandle, m_stripInfo.getStripInfo());
                    appendBytesToCompressedStream(m_stripInfo.getStripInfo().getBufferedStripData());

                    // save data to file
                    try
                    {
                        if ( m_CompressedFile != null )
                        {
                            OutputStream outputStream = new FileOutputStream(m_CompressedFile);
                            m_compressedStream.writeTo(outputStream);
                            outputStream.close();
                        }
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
            catch (DTwainJavaAPIException e)
            {
            }
        }
        return 1;
    }

    /* Sent by DTWAIN to query if we want to keep the image or not.
       Image is displayed in the DTWAINImageDisplayDialog dialog to
       aid the user into making decision.
    */
   @Override
   public int onQueryPageDiscard(long sourceHandle)
   {
       // if showing the preview image is off, then just keep the image by
       // returning 1
       DTwainJavaAPI handle = this.m_mainFrame.getTwainInterface();

       if ( !m_mainFrame.isShowPreviewImage() )
           return 1;
       try
       {
           // get the image data that was acquired
          TwainImageData imgData = handle.DTWAIN_GetCurrentAcquiredImage(sourceHandle);

          // display the image data in the dialog
          DTwainImageDisplayDialog dlg =  new DTwainImageDisplayDialog(imgData, DTwainConstants.FileType.BMP);
          dlg.setVisible(true);

          // if we pressed "Keep", then we must return 1 back to DTWAIN
          if ( dlg.isOkPressed() )
              return 1;

          // throw the image away, so return 0 back to DTWAIN
          return 0;
       }
       catch (DTwainJavaAPIException e)
       {
           System.out.println(e.getMessage());
       }
       return 1;
   }
}
