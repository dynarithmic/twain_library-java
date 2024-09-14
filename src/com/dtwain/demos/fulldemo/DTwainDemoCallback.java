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
package com.dtwain.demos.fulldemo;
import com.dynarithmic.twain.*;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.BufferedTransferInfo;
import com.dynarithmic.twain.highlevel.TwainImageData;
import com.dynarithmic.twain.highlevel.TwainSource;
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
    public int onTransferStripDone(TwainSource sourceHandle)
    {
        if ( m_CompressionOn )
        {
            // get the buffered strip
            try
            {
                DTwainJavaAPI handle = this.m_mainFrame.getTwainInterface();
                if ( handle != null )
                {
                    handle.DTWAIN_GetBufferedStripData(sourceHandle.getSourceHandle(), m_stripInfo.getStripInfo());
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
    public int onTransferDone(TwainSource sourceHandle)
    {
        DTwainJavaAPI handle = this.m_mainFrame.getTwainInterface();
        if ( m_CompressionOn )
        {
            // get the buffered strip
            try
            {
                if ( handle != null )
                {
                    handle.DTWAIN_GetBufferedStripData(sourceHandle.getSourceHandle(), m_stripInfo.getStripInfo());
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
   public int onQueryPageDiscard(TwainSource sourceHandle)
   {
       // if showing the preview image is off, then just keep the image by
       // returning 1
       DTwainJavaAPI handle = this.m_mainFrame.getTwainInterface();

       if ( !m_mainFrame.isShowPreviewImage() )
           return 1;
       try
       {
           // get the image data that was acquired
          TwainImageData imgData = handle.DTWAIN_GetCurrentAcquiredImage(sourceHandle.getSourceHandle());

          // display the image data in the dialog
          DTwainImageDisplayDialog dlg =  new DTwainImageDisplayDialog(imgData, DTwainConstants.FileType.BMP);
          dlg.setVisible(true);
          boolean isOk = dlg.isOkPressed();
          dlg.setVisible(false);
          dlg.dispose();
          if ( isOk )
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
