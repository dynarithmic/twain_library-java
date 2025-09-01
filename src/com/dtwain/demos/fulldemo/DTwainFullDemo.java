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
package com.dtwain.demos.fulldemo;
import com.dtwain.demos.ConsoleJNISelector;
import com.dtwain.demos.EnhancedSourceSelector;
import com.dynarithmic.twain.*;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.BlankPageDetectionOption;
import com.dynarithmic.twain.DTwainConstants.BlankPageDiscardOption;
import com.dynarithmic.twain.DTwainConstants.DSMType;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.SourceStateAfterAcquire;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.BufferedTransferInfo;
import com.dynarithmic.twain.highlevel.ImageHandler;
import com.dynarithmic.twain.highlevel.TwainAppInfo;
import com.dynarithmic.twain.highlevel.TwainConsoleLogger;
import com.dynarithmic.twain.highlevel.TwainLogger;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.BlankPageHandlingOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.GeneralOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions.FileTransferFlags;
import com.dynarithmic.twain.highlevel.TwainSourceDialog;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;

import java.awt.Toolkit;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
public class DTwainFullDemo extends javax.swing.JFrame {

    /**
     * Creates new form DTwainDemoFrame
     */
    TwainSource m_SourceWrapper = new TwainSource();
    final String sOrigTitle;
    final javax.swing.JMenu m_AcquireFileMenu;
    TreeMap<Integer, String> m_fileTypeMap;
    final TreeMap<Integer, javax.swing.JMenuItem> m_FileTypeToMenu = new TreeMap<>();
    final TreeMap<Integer, javax.swing.JMenuItem> m_CompressTypeToMenu = new TreeMap<>();
    TwainSession mainTwainSession = null;
    DTwainDemoCallback iCallback = null;

    public DTwainFullDemo() throws Exception
    {
        initComponents();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
        sOrigTitle = getTitle();
        ShowImagePreview.setState(true);
        DisplaySourceUI.setState(true);
        DiscardBlankPages.setState(true);
        LogCalls.setState(false);
        m_AcquireFileMenu = new javax.swing.JMenu();

        m_FileTypeToMenu.put(DTwainConstants.DSFileType.JFIF.value(), JPEGFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.BMP.value(), BMPFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.TIFF.value(), TIFFFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.TIFFMULTI.value(), TIFFMultipageFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.PNG.value(), PNGFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.PDF.value(), PDFFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.PDFA.value(), PDFAFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.PDFA2.value(), PDFA2FileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.PDFRASTER.value(), PDFRasterFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.FPX.value(), FPXFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.EXIF.value(), EXIFFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.JP2.value(), JP2FileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.SPIFF.value(), SPIFFFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.XBM.value(), XBMFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.PICT.value(), PICTFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.JPX.value(), JPXFileMode);
        m_FileTypeToMenu.put(DTwainConstants.DSFileType.DEJAVU.value(), DEJAVUFileMode);

        m_CompressTypeToMenu.put(DTwainConstants.CompressionType.NONE.value(), AcquireCompressNone);
        m_CompressTypeToMenu.put(DTwainConstants.CompressionType.JPEG.value(), AcquireJPEGCompressed);
        enableSourceItems(false);

        setJNIVersion();
        mainTwainSession = new TwainSession();
        mainTwainSession.registerCallback(m_SourceWrapper, new DTwainDemoCallback(this));

        // Set the TWAIN DSM to use when starting up the TWAIN system
        mainTwainSession.setDSM(DSMType.LEGACY);

        // Create a logger 
        TwainLogger logging = this.mainTwainSession.getLogger();
        logging.setVerbosity(TwainLogger.LoggerVerbosity.MAXIMUM).
        addLogger(new TwainConsoleLogger());          // Log to the system console


        // Set the application info
        TwainAppInfo appInfo = mainTwainSession.getAppInfo();
        appInfo.setManufacturer("My Java Manufacturer");
        appInfo.setProductName("My Java Product Name");
        appInfo.setVersionInfo("My Java Version Info");
        appInfo.setProductFamily("My Java Product Family");
    }

    public void setJNIVersion()
    {
        ConsoleJNISelector.setJNIVersion("Full Demo");
    }
    
    public boolean startTwainSession() throws DTwainJavaAPIException, Exception
    {
        mainTwainSession.start();
        return mainTwainSession.isStarted();
    }

    public boolean isShowPreviewImage()
    {
        return ShowImagePreview.getState();
    }

    public DTwainJavaAPI getTwainInterface()
    {
        return mainTwainSession.getAPIHandle();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws Exception {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        SelectSource = new javax.swing.JMenuItem();
        SelectSourceByName = new javax.swing.JMenuItem();
        SelectDefaultSource = new javax.swing.JMenuItem();
        SelectSourceCustom = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        SourceProperties = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        CloseSource = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        ExitDemo = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        AcquireNative = new javax.swing.JMenuItem();
        AcquireBuffered = new javax.swing.JMenuItem();
        AcquireCompressed = new javax.swing.JMenu();
        AcquireCompressNone = new javax.swing.JMenuItem();
        AcquireGroup31Compressed = new javax.swing.JMenuItem();
        AcquireGroup31dEOLCompressed = new javax.swing.JMenuItem();
        AcquireGroup32dCompressed = new javax.swing.JMenuItem();
        AcquireGroup4Compressed = new javax.swing.JMenuItem();
        AcquireJPEGCompressed = new javax.swing.JMenuItem();
        AcquireFile = new javax.swing.JMenu();
        AcquireFileBMP = new javax.swing.JMenuItem();
        AcquireFileJPEG = new javax.swing.JMenuItem();
        AcquireFileJPEGXR = new javax.swing.JMenuItem();
        AcquireFileJP2 = new javax.swing.JMenuItem();
        AcquireFileGIF = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        javax.swing.JMenuItem AcquireFileTIFF1 = new javax.swing.JMenuItem();
        AcquireFileTIFFG3 = new javax.swing.JMenuItem();
        AcquireFileTIFFG4 = new javax.swing.JMenuItem();
        AcquireFileTIFFLZW = new javax.swing.JMenuItem();
        AcquireFileTIFFPackBits = new javax.swing.JMenuItem();
        AcquireFileTIFFFlate = new javax.swing.JMenuItem();
        AcquireFileTIFFJPEG = new javax.swing.JMenuItem();
        AcquireFilePNG = new javax.swing.JMenuItem();
        AcquireFilePDF = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        AcquireFilePSLevel1 = new javax.swing.JMenuItem();
        AcquireFilePSLevel2 = new javax.swing.JMenuItem();
        AcquireFilePCX = new javax.swing.JMenuItem();
        AcquireFileTGA = new javax.swing.JMenuItem();
        AcquireFilePSD = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        AcquireFileWMF = new javax.swing.JMenuItem();
        AcquireFileEMF = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        AcquireFileICO = new javax.swing.JMenuItem();
        AcquireFileICOLARGE = new javax.swing.JMenuItem();
        AcquireFileWBMP = new javax.swing.JMenuItem();
        AcquireFileWithDriver = new javax.swing.JMenu();
        BMPFileMode = new javax.swing.JMenuItem();
        JPEGFileMode = new javax.swing.JMenuItem();
        JP2FileMode = new javax.swing.JMenuItem();
        TIFFFileMode = new javax.swing.JMenuItem();
        TIFFMultipageFileMode = new javax.swing.JMenuItem();
        PNGFileMode = new javax.swing.JMenuItem();
        PDFFileMode = new javax.swing.JMenuItem();
        PDFAFileMode = new javax.swing.JMenuItem();
        PDFA2FileMode = new javax.swing.JMenuItem();
        FPXFileMode = new javax.swing.JMenuItem();
        EXIFFileMode = new javax.swing.JMenuItem();
        SPIFFFileMode = new javax.swing.JMenuItem();
        XBMFileMode = new javax.swing.JMenuItem();
        PICTFileMode = new javax.swing.JMenuItem();
        JPXFileMode = new javax.swing.JMenuItem();
        DEJAVUFileMode = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        ShowImagePreview = new javax.swing.JCheckBoxMenuItem();
        DisplaySourceUI = new javax.swing.JCheckBoxMenuItem();
        DiscardBlankPages = new javax.swing.JCheckBoxMenuItem();
        jMenu9 = new javax.swing.JMenu();
        LogCalls = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("DTWAIN Java Demo Program");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                try
                {
                    ShutdownTwain(evt);
                }
                catch (DTwainJavaAPIException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try
                {
                    ShutdownTwain(evt);
                    dispose();
                }
                catch (DTwainJavaAPIException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        
        jMenu1.setText("Source Selection Test");

        SelectSource.setText("Select Source...");
        SelectSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectSourceActionPerformed(evt);
            }
        });
        jMenu1.add(SelectSource);

        SelectSourceByName.setLabel("Select Source By Name...");
        SelectSourceByName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    SelectSourceByNameActionPerformed(evt);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        jMenu1.add(SelectSourceByName);
        SelectSourceByName.getAccessibleContext().setAccessibleName("selSourceByName");

        SelectDefaultSource.setText("Select Default Source");
        SelectDefaultSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    SelectDefaultSourceActionPerformed(evt);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        jMenu1.add(SelectDefaultSource);

        SelectSourceCustom.setText("Select Source Custom...");
        SelectSourceCustom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    SelectSourceCustomActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        jMenu1.add(SelectSourceCustom);
        jMenu1.add(jSeparator1);

        SourceProperties.setText("Source Properties...");
        SourceProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SourcePropertiesActionPerformed(evt);
            }
        });
        jMenu1.add(SourceProperties);
        jMenu1.add(jSeparator2);

        CloseSource.setText("Close Source");
        CloseSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseSourceActionPerformed(evt);
            }
        });
        jMenu1.add(CloseSource);
        jMenu1.add(jSeparator3);

        ExitDemo.setText("Exit");
        ExitDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    ExitDemoActionPerformed(evt);
                }
                catch (DTwainJavaAPIException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        jMenu1.add(ExitDemo);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Acquire Test");

        AcquireNative.setText("Acquire Native...");
        AcquireNative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireNativeActionPerformed(evt);
            }
        });
        jMenu2.add(AcquireNative);

        AcquireBuffered.setText("Acquire Buffered...");
        AcquireBuffered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireBufferedActionPerformed(evt);
            }
        });
        jMenu2.add(AcquireBuffered);

        AcquireCompressed.setText("Acquire Compressed ...");

        AcquireCompressNone.setText("No Compression");
        AcquireCompressNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    AcquireCompressNoneActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireCompressed.add(AcquireCompressNone);

        AcquireGroup31Compressed.setText("Group 3 (1 dimensional)");
        AcquireCompressed.add(AcquireGroup31Compressed);

        AcquireGroup31dEOLCompressed.setText("Group 3 (1 dimensional EOL)");
        AcquireCompressed.add(AcquireGroup31dEOLCompressed);

        AcquireGroup32dCompressed.setText("Group 3 (2 dimensional)");
        AcquireCompressed.add(AcquireGroup32dCompressed);

        AcquireGroup4Compressed.setText("Group 4 ");
        AcquireGroup4Compressed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    AcquireGroup4CompressedActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireCompressed.add(AcquireGroup4Compressed);

        AcquireJPEGCompressed.setText("JPEG");
        AcquireJPEGCompressed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    AcquireJPEGCompressedActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireCompressed.add(AcquireJPEGCompressed);

        jMenu2.add(AcquireCompressed);

        AcquireFile.setText("Acquire To File...");

        AcquireFileBMP.setText("Windows BMP");
        AcquireFileBMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileBMPActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFileBMP);

        AcquireFileJPEG.setText("JPEG");
        AcquireFileJPEG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileJPEGActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFileJPEG);

        AcquireFileJP2.setText("JPEG-2000");
        AcquireFileJP2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    AcquireFileJP2ActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFile.add(AcquireFileJP2);

        AcquireFileJPEGXR.setText("JPEG-XR");
        AcquireFileJPEGXR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileJPEGXRActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFileJPEGXR);
        
        AcquireFileGIF.setText("GIF");
        AcquireFileGIF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileGIFActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFileGIF);

        jMenu4.setText("TIFF");

        AcquireFileTIFF1.setText("Uncompressed");
        AcquireFileTIFF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileTIFF1ActionPerformed(evt);
            }
        });
        jMenu4.add(AcquireFileTIFF1);

        AcquireFileTIFFG3.setText("Group 3 FAX");
        AcquireFileTIFFG3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    AcquireFileTIFFG3ActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        jMenu4.add(AcquireFileTIFFG3);

        AcquireFileTIFFG4.setText("Group 4 FAX");
        AcquireFileTIFFG4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    AcquireFileTIFFG4ActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        jMenu4.add(AcquireFileTIFFG4);

        AcquireFileTIFFLZW.setText("LZW Compressed");
        AcquireFileTIFFLZW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileTIFFLZWActionPerformed(evt);
            }
        });
        jMenu4.add(AcquireFileTIFFLZW);

        AcquireFileTIFFPackBits.setText("PackBits Compressed");
        AcquireFileTIFFPackBits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileTIFFPackBitsActionPerformed(evt);
            }
        });
        jMenu4.add(AcquireFileTIFFPackBits);

        AcquireFileTIFFFlate.setText("Flate Compressed");
        AcquireFileTIFFFlate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileTIFFFlateActionPerformed(evt);
            }
        });
        jMenu4.add(AcquireFileTIFFFlate);

        AcquireFileTIFFJPEG.setText("JPEG Compressed");
        AcquireFileTIFFJPEG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileTIFFJPEGActionPerformed(evt);
            }
        });
        jMenu4.add(AcquireFileTIFFJPEG);

        AcquireFile.add(jMenu4);

        AcquireFilePNG.setText("PNG");
        AcquireFilePNG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFilePNGActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFilePNG);

        AcquireFilePDF.setText("Adobe PDF");
        AcquireFilePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFilePDFActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFilePDF);

        jMenu8.setText("PostScript");

        AcquireFilePSLevel1.setText("Level 1");
        AcquireFilePSLevel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFilePSLevel1ActionPerformed(evt);
            }
        });
        jMenu8.add(AcquireFilePSLevel1);

        AcquireFilePSLevel2.setText("Level 2");
        AcquireFilePSLevel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFilePSLevel2ActionPerformed(evt);
            }
        });
        jMenu8.add(AcquireFilePSLevel2);

        AcquireFile.add(jMenu8);

        AcquireFilePCX.setText("PCX / DCX");
        AcquireFilePCX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFilePCXActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFilePCX);

        AcquireFileTGA.setText("Targa");
        AcquireFileTGA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileTGAActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFileTGA);

        AcquireFilePSD.setText("Adobe PSD (Photoshop)");
        AcquireFilePSD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFilePSDActionPerformed(evt);
            }
        });
        AcquireFile.add(AcquireFilePSD);

        jMenu5.setText("Windows MetaFile");

        AcquireFileWMF.setText("WMF");
        AcquireFileWMF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileWMFActionPerformed(evt);
            }
        });
        jMenu5.add(AcquireFileWMF);

        AcquireFileEMF.setText("Enhanced Meta File");
        AcquireFileEMF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileEMFActionPerformed(evt);
            }
        });
        jMenu5.add(AcquireFileEMF);

        AcquireFile.add(jMenu5);

        jMenu7.setText("Windows Icon");

        AcquireFileICO.setText("Small Icon");
        AcquireFileICO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileICOActionPerformed(evt);
            }
        });
        jMenu7.add(AcquireFileICO);

        AcquireFileICOLARGE.setText("Large Icon ");
        AcquireFileICOLARGE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcquireFileICOLARGEActionPerformed(evt);
            }
        });
        jMenu7.add(AcquireFileICOLARGE);

        AcquireFile.add(jMenu7);

        AcquireFileWBMP.setText("Wireless Bitmap (WBMP)");
        AcquireFileWBMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    AcquireFileWBMPActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFile.add(AcquireFileWBMP);

        jMenu2.add(AcquireFile);

        AcquireFileWithDriver.setText("Acquire To File (Driver Mode)...");

        BMPFileMode.setText("Windows BMP");
        BMPFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    BMPFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(BMPFileMode);

        JPEGFileMode.setText("JPEG (JFIF)");
        JPEGFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    JPEGFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(JPEGFileMode);

/*        JP2FileMode.setText("JPEG-2000");
        JP2FileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JP2FileModeActionPerformed(evt);
            }
        });
        AcquireFileWithDriver.add(JP2FileMode);
*/
        TIFFFileMode.setText("TIFF");
        TIFFFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    TIFFFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(TIFFFileMode);

        TIFFMultipageFileMode.setText("TIFF (Multipage)");
        TIFFMultipageFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    TIFFMultipageFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(TIFFMultipageFileMode);

        PNGFileMode.setText("PNG");
        PNGFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    PNGFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(PNGFileMode);

        PDFFileMode.setText("PDF");
        PDFFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    PDFFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(PDFFileMode);

        PDFAFileMode.setText("PDF/A");
        PDFAFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    PDFAFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(PDFAFileMode);

        PDFA2FileMode.setText("PDF/A2");
        PDFA2FileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    PDFA2FileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(PDFA2FileMode);

        FPXFileMode.setText("FlashPix (FPX)");
        FPXFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    FPXFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(FPXFileMode);

        EXIFFileMode.setText("EXIF");
        EXIFFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    EXIFFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(EXIFFileMode);

        SPIFFFileMode.setText("SPIFF");
        SPIFFFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    SPIFFFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(SPIFFFileMode);

        XBMFileMode.setText("XBM");
        XBMFileMode.setToolTipText("");
        XBMFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    XBMFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(XBMFileMode);

        PICTFileMode.setText("Macintosh PICT");
        PICTFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    PICTFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(PICTFileMode);

        JPXFileMode.setText("JPX (JPEG ISO/IEC 15444-2)");
        JPXFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    JPXFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(JPXFileMode);

        DEJAVUFileMode.setText("DEJAVU");
        DEJAVUFileMode.setToolTipText("");
        DEJAVUFileMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try
                {
                    DEJAVUFileModeActionPerformed(evt);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AcquireFileWithDriver.add(DEJAVUFileMode);

        jMenu2.add(AcquireFileWithDriver);
        jMenu2.add(jSeparator4);

        ShowImagePreview.setSelected(true);
        ShowImagePreview.setText("Show Preview of Image...");
        ShowImagePreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowImagePreviewActionPerformed(evt);
            }
        });
        jMenu2.add(ShowImagePreview);

        DisplaySourceUI.setSelected(true);
        DisplaySourceUI.setText("Display Source User Interface...");
        jMenu2.add(DisplaySourceUI);

        DiscardBlankPages.setSelected(true);
        DiscardBlankPages.setText("Discard Blank Pages...");
        jMenu2.add(DiscardBlankPages);

        jMenuBar1.add(jMenu2);

        jMenu9.setText("TWAIN Logging...");

        LogCalls.setSelected(true);
        LogCalls.setText("Log DTWAIN Calls...");
        LogCalls.setToolTipText("");
        LogCalls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogCallsActionPerformed(evt);
            }
        });
        jMenu9.add(LogCalls);

        jMenuBar1.add(jMenu9);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 419, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void disableAllFormats()
    {
        Collection<JMenuItem> c = m_FileTypeToMenu.values();
        //obtain an Iterator for Collection
        Iterator<JMenuItem> itr = c.iterator();

        //iterate through TreeMap values iterator
        while(itr.hasNext())
        {
            javax.swing.JMenuItem menu = (javax.swing.JMenuItem)itr.next();
            if ( menu != null)
                menu.setEnabled(false);
        }
    }

    private void enableSourceItems(boolean bEnable)
    {
        CloseSource.setEnabled(bEnable);
        SourceProperties.setEnabled(bEnable);
        AcquireFile.setEnabled(bEnable);
        AcquireBuffered.setEnabled(bEnable);
        AcquireNative.setEnabled(bEnable);
        AcquireFileWithDriver.setEnabled(bEnable);
        AcquireCompressed.setEnabled(bEnable);

        if ( bEnable )
        {
            try {
                disableAllFormats();
                CapabilityInterface ci = m_SourceWrapper.getCapabilityInterface();
                List<Integer> filetypes = ci.getImageFileFormat(ci.get());
                for (int i = 0; i < filetypes.size(); ++i)
                {
                    if (m_FileTypeToMenu.containsKey(filetypes.get(i)))
                    {
                        javax.swing.JMenuItem menu = m_FileTypeToMenu.get(filetypes.get(i));
                        menu.setEnabled(true);
                    }
                }

                // now do the compression types for TIFF
                List<Integer> compressTypes = ci.getCompression(ci.get());
                for (int i = 0; i < compressTypes.size(); ++i)
                {
                    if (m_CompressTypeToMenu.containsKey(compressTypes.get(i)))
                    {
                        javax.swing.JMenuItem menu = m_CompressTypeToMenu.get(compressTypes.get(i));
                        menu.setEnabled(true);
                    }
                }
            }
            catch(DTwainJavaAPIException e)
            {
            }
        }
        else
        {
            Collection<JMenuItem> c = m_FileTypeToMenu.values();
            Iterator<JMenuItem> itr = c.iterator();
            while(itr.hasNext())
            {
                javax.swing.JMenuItem menu = (javax.swing.JMenuItem)itr.next();
                if ( menu != null)
                    menu.setEnabled(bEnable);
            }
        }
    }

    private void setDialogTitle()
    {
        long TwainSource = m_SourceWrapper.getSourceHandle();
        if ( TwainSource != 0 )
        {
            String sText = sOrigTitle;
            sText += " - " + m_SourceWrapper.getInfo().getProductName();
            setTitle(sText);
        }
        else
            setTitle(sOrigTitle);
        enableSourceItems((TwainSource!=0)?true:false);
    }

    private void SelectSourceActionPerformed(java.awt.event.ActionEvent evt)
    {
        if ( CloseCurrentSource() )
        {
            try
            {
                m_SourceWrapper = mainTwainSession.selectSource(new TwainSourceDialog().enableEnhancedDialog(true).center(true).sortNames(true).topmostWindow(true));
                setupNewSource(m_SourceWrapper.getSourceHandle());
            }
            catch (Exception e)
            {
                System.out.print(e.getMessage());
            }
        }
    }

    private void SourcePropertiesActionPerformed(java.awt.event.ActionEvent evt)
    {
        long TwainSource = m_SourceWrapper.getSourceHandle();
        if ( TwainSource != 0 )
        {
            DTwainSourcePropertiesDialog srcProperties = new DTwainSourcePropertiesDialog(m_SourceWrapper, this, true);
            srcProperties.setVisible(true);
        }
    }

    private void SelectDefaultSourceActionPerformed(java.awt.event.ActionEvent evt) throws Exception
    {
        if ( CloseCurrentSource() )
        {
            try
            {
               m_SourceWrapper = mainTwainSession.selectDefaultSource();
               setupNewSource(m_SourceWrapper.getSourceHandle());
            }
            catch (DTwainJavaAPIException e)
            {
                System.out.print(e.getMessage());
            }
        }
    }

    private void CloseSourceActionPerformed(java.awt.event.ActionEvent evt)
    {
        try
        {
            m_SourceWrapper.close();
            setDialogTitle();
        }
        catch (DTwainJavaAPIException e)
        {
            System.out.print(e.getMessage());
        }
    }

    private void setBlankPageDetection()
    {
        AcquireCharacteristics ac = m_SourceWrapper.getAcquireCharacteristics();
        BlankPageHandlingOptions bp = ac.getBlankPageHandlingOptions();
        if (DiscardBlankPages.getState())
        {
            bp.enable(true).
               setThreshold(98.5).setDetectionOption(BlankPageDetectionOption.DETECT_ORIGINAL).
               setDiscardOption(BlankPageDiscardOption.AUTODISCARD_ANY);
        }
        else
        {
            bp.enable(false);
        }
    }

    private void AcquireHelper(int nWhich)
    {
        setBlankPageDetection();
        AcquireCharacteristics ac = this.m_SourceWrapper.getAcquireCharacteristics();
        GeneralOptions gopts = ac.getGeneralOptions();
        gopts.setAcquireType(nWhich==0?AcquireType.NATIVE:AcquireType.BUFFERED).setSourceStateAfterAcquire(SourceStateAfterAcquire.OPENED);
        ac.getUserInterfaceOptions().showUI(DisplaySourceUI.getState());
        AcquireReturnInfo acInfo = null;
        try
        {
            acInfo = this.m_SourceWrapper.acquire();
        }
        catch (DTwainJavaAPIException e)
        {
            e.printStackTrace();
        }
        ImageHandler iHandler = acInfo.getImageHandler();
        long count = iHandler.getNumAcquisitions();
        System.out.println("Acquired " + count + " images.");
        if ( count == 0  )
            JOptionPane.showMessageDialog(null, "No images were acquired, or all images were discarded by user");
        else
        {
            DTwainImageDisplayDialog imgDlg = new DTwainImageDisplayDialog(iHandler.getAcquisitionArray(), DTwainConstants.FileType.BMP);
            imgDlg.setVisible(true);
            imgDlg.setVisible(false);
            imgDlg.dispose();
        }
    }

    private void AcquireFileHelper(FileType filetype)
    {
        try
        {
            AcquireFileHelper(filetype, DTwainConstants.DSFileType.BMP, DTwainConstants.PixelType.DEFAULT.value(), false);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void AcquireFileHelper(FileType filetype, DTwainConstants.PixelType pixType)
    {
        try
        {
            AcquireFileHelper(filetype, DTwainConstants.DSFileType.BMP, pixType.value(), false);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void AcquireFileHelper(DTwainConstants.DSFileType filetype)
    {
        try
        {
            AcquireFileHelper(DTwainConstants.FileType.BMP, filetype, DTwainConstants.PixelType.DEFAULT.value(), true);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void AcquireCompressedHelper(DTwainConstants.CompressionType nWhich, int pixelType) throws InterruptedException
    {
        setBlankPageDetection();
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            File file = fc.getSelectedFile();
            try
            {
                AcquireCharacteristics ac = this.m_SourceWrapper.getAcquireCharacteristics();
                ac.getGeneralOptions().setAcquireType(AcquireType.BUFFERED);
                BufferedTransferInfo bufInfo = this.m_SourceWrapper.getBufferedTransferInfo();
                bufInfo.setCompressionType(nWhich);
                ac.getPaperHandlingOptions().enableFeeder(true);

                iCallback.enableCompression(true);
                this.m_SourceWrapper.setMessageCallback(iCallback);
//                iCallback.initCompressedStream(stripInfo);
                iCallback.setFileHandle(file);
//                m_api.DTWAIN_SetCompressionType(TwainSource, nWhich, true);
                this.m_SourceWrapper.acquire();
/*                m_api.DTWAIN_AcquireBuffered(m_SourceWrapper.getHandle(), pixelType, DTwainConstants.DTWAIN_ACQUIREALL, DisplaySourceUI.getState(), false);
                m_api.DTWAIN_EndBufferedTransfer(m_SourceWrapper.getHandle(), stripInfo);
  */
                // saved the compressed string to a binary file
            }
            catch (DTwainJavaAPIException e)
            {
            }
            iCallback.enableCompression(false);
        }
    }

    private void AcquireFileHelper(DTwainConstants.FileType fType,
                                   DTwainConstants.DSFileType fUseSourceModeType,
                                   int pixelType, boolean useSourceMode) throws InterruptedException
    {
        setBlankPageDetection();
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        if ( returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            try
            {
                AcquireCharacteristics ac = this.m_SourceWrapper.getAcquireCharacteristics();
                ac.getPaperHandlingOptions().enableFeeder(true);
                FileTransferOptions fOpts = ac.getFileTransferOptions();
                fOpts.setType(fType);
                FileTransferFlags fFlags = fOpts.getTransferFlags();
                fOpts.setName(file.getAbsolutePath());
                fFlags.useName(true).useDeviceTransfer(useSourceMode);
                ac.getGeneralOptions().setAcquireType(AcquireType.NATIVEFILE).setMaxPageCount(DTwainConstants.DTWAIN_ACQUIREALL);
                AcquireReturnInfo info = this.m_SourceWrapper.acquire();
                if ( info.getReturnCode() == ErrorCode.ERROR_NONE)
                    JOptionPane.showMessageDialog(null, "Image " + file + " has been created.");
                else
                    JOptionPane.showMessageDialog(null, "Image " + file + " was not created.");
            }
            catch (DTwainJavaAPIException e)
            {
                System.out.print(e.getMessage());
            }
        }
    }

    private void AcquireNativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireNativeActionPerformed
                AcquireHelper(0);
    }//GEN-LAST:event_AcquireNativeActionPerformed

    private void AcquireBufferedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireBufferedActionPerformed
                AcquireHelper(1);
    }//GEN-LAST:event_AcquireBufferedActionPerformed

    private void AcquireFileBMPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileBMPActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.BMP);
    }//GEN-LAST:event_AcquireFileBMPActionPerformed

    private void AcquireFileJPEGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileJPEGActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.JPEG);
    }//GEN-LAST:event_AcquireFileJPEGActionPerformed

    private void AcquireFileJPEGXRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileJPEGActionPerformed
        AcquireFileHelper(DTwainConstants.FileType.JPEGXR);
    }//GEN-LAST:event_AcquireFileJPEGActionPerformed
    
    private void AcquireFileGIFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileGIFActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.GIF);
    }//GEN-LAST:event_AcquireFileGIFActionPerformed

    private void AcquireFileTIFF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileTIFF1ActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.TIFFNONEMULTI);
    }//GEN-LAST:event_AcquireFileTIFF1ActionPerformed

    private void AcquireFileTIFFG3ActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {//GEN-FIRST:event_AcquireFileTIFFG3ActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.TIFFG3MULTI, DTwainConstants.PixelType.BW);
    }//GEN-LAST:event_AcquireFileTIFFG3ActionPerformed

    private void AcquireFileTIFFG4ActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {//GEN-FIRST:event_AcquireFileTIFFG4ActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.TIFFG4MULTI, DTwainConstants.PixelType.BW);
    }//GEN-LAST:event_AcquireFileTIFFG4ActionPerformed

    private void AcquireFileTIFFLZWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileTIFFLZWActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.TIFFLZWMULTI);
    }//GEN-LAST:event_AcquireFileTIFFLZWActionPerformed

    private void AcquireFileTIFFPackBitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileTIFFPackBitsActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.TIFFPACKBITSMULTI);
    }//GEN-LAST:event_AcquireFileTIFFPackBitsActionPerformed

    private void AcquireFileTIFFFlateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileTIFFFlateActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.TIFFDEFLATEMULTI);
    }//GEN-LAST:event_AcquireFileTIFFFlateActionPerformed

    private void AcquireFileTIFFJPEGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileTIFFJPEGActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.TIFFJPEGMULTI);
    }//GEN-LAST:event_AcquireFileTIFFJPEGActionPerformed

    private void AcquireFilePNGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFilePNGActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.PNG);
    }//GEN-LAST:event_AcquireFilePNGActionPerformed

    private void AcquireFilePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFilePDFActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.PDFMULTI);
    }//GEN-LAST:event_AcquireFilePDFActionPerformed

    private void AcquireFilePCXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFilePCXActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.DCX);
    }//GEN-LAST:event_AcquireFilePCXActionPerformed

    private void AcquireFileTGAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileTGAActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.TGA);
    }//GEN-LAST:event_AcquireFileTGAActionPerformed

    private void AcquireFilePSDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFilePSDActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.PSD);
    }//GEN-LAST:event_AcquireFilePSDActionPerformed

    private void AcquireFileWMFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileWMFActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.WMF);
    }//GEN-LAST:event_AcquireFileWMFActionPerformed

    private void AcquireFileEMFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileEMFActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.EMF);
    }//GEN-LAST:event_AcquireFileEMFActionPerformed

    private void AcquireFileICOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileICOActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.ICO_RESIZED);
    }//GEN-LAST:event_AcquireFileICOActionPerformed

    private void AcquireFileICOLARGEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFileICOLARGEActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.ICO_VISTA);
    }//GEN-LAST:event_AcquireFileICOLARGEActionPerformed

    private void AcquireFileWBMPActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {//GEN-FIRST:event_AcquireFileWBMPActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.WBMP_RESIZED, DTwainConstants.PixelType.BW);
    }//GEN-LAST:event_AcquireFileWBMPActionPerformed

    private void AcquireFilePSLevel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFilePSLevel1ActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.POSTSCRIPT1);
    }//GEN-LAST:event_AcquireFilePSLevel1ActionPerformed

    private void AcquireFilePSLevel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcquireFilePSLevel2ActionPerformed
                AcquireFileHelper(DTwainConstants.FileType.POSTSCRIPT2);
    }//GEN-LAST:event_AcquireFilePSLevel2ActionPerformed

    private void ShowImagePreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowImagePreviewActionPerformed
    }//GEN-LAST:event_ShowImagePreviewActionPerformed

    private void LogCallsActionPerformed(java.awt.event.ActionEvent evt)
    {
        try
        {
            TwainLogger logging = this.mainTwainSession.getLogger();
            logging.setVerbosity(TwainLogger.LoggerVerbosity.MINIMAL);

            if (LogCalls.getState())
                mainTwainSession.startLogging();
            else
                mainTwainSession.stopLogging();
        }
        catch (DTwainJavaAPIException e)
        {
            System.out.print(e.getMessage());
        }
    }

    private void ExitDemoActionPerformed(java.awt.event.ActionEvent evt) throws DTwainJavaAPIException, InterruptedException {//GEN-FIRST:event_ExitDemoActionPerformed
        ShutdownTwain();
        dispose();
    }//GEN-LAST:event_ExitDemoActionPerformed

    private void ShutdownTwain() throws DTwainJavaAPIException, InterruptedException
    {
        mainTwainSession.stop();
    }

    private void ShutdownTwain(java.awt.event.WindowEvent evt) throws DTwainJavaAPIException, InterruptedException {//GEN-FIRST:event_ShutdownTwain
        ShutdownTwain();
    }//GEN-LAST:event_ShutdownTwain

    private void BMPFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {//GEN-FIRST:event_BMPFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.BMP);
    }//GEN-LAST:event_BMPFileModeActionPerformed

    private void JPEGFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {//GEN-FIRST:event_JPEGFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.JFIF);
    }//GEN-LAST:event_JPEGFileModeActionPerformed

    private void TIFFFileModeActionPerformed(java.awt.event.ActionEvent evt)  throws InterruptedException{//GEN-FIRST:event_TIFFFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.TIFF);
    }//GEN-LAST:event_TIFFFileModeActionPerformed

    private void TIFFMultipageFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {//GEN-FIRST:event_TIFFMultipageFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.TIFFMULTI);
    }//GEN-LAST:event_TIFFMultipageFileModeActionPerformed

    private void PNGFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {//GEN-FIRST:event_PNGFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.PNG);
    }//GEN-LAST:event_PNGFileModeActionPerformed

    private void PDFFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException {//GEN-FIRST:event_PDFFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.PDF);
    }//GEN-LAST:event_PDFFileModeActionPerformed

    private void PDFAFileModeActionPerformed(java.awt.event.ActionEvent evt)  throws InterruptedException{//GEN-FIRST:event_PDFAFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.PDFA);
    }//GEN-LAST:event_PDFAFileModeActionPerformed

    private void PDFA2FileModeActionPerformed(java.awt.event.ActionEvent evt)  throws InterruptedException{//GEN-FIRST:event_PDFA2FileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.PDFA2);
    }//GEN-LAST:event_PDFA2FileModeActionPerformed

    private void PDFRASTERFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_PDFRASTERFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.PDFRASTER);
    }//GEN-LAST:event_PDFRASTERFileModeActionPerformed

    private void FPXFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_FPXFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.FPX);
    }//GEN-LAST:event_FPXFileModeActionPerformed

    private void EXIFFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_EXIFFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.EXIF);
    }//GEN-LAST:event_EXIFFileModeActionPerformed

    private void AcquireFileJP2ActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_AcquireFileJP2ActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.JP2);
    }//GEN-LAST:event_AcquireFileJP2ActionPerformed

    private void SPIFFFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_SPIFFFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.SPIFF);
    }//GEN-LAST:event_SPIFFFileModeActionPerformed

    private void XBMFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_XBMFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.XBM);
    }//GEN-LAST:event_XBMFileModeActionPerformed

    private void PICTFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_PICTFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.PICT);
    }//GEN-LAST:event_PICTFileModeActionPerformed

    private void JPXFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_JPXFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.JPX);
    }//GEN-LAST:event_JPXFileModeActionPerformed

    private void DEJAVUFileModeActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_DEJAVUFileModeActionPerformed
        AcquireFileHelper(DTwainConstants.DSFileType.DEJAVU);
    }//GEN-LAST:event_DEJAVUFileModeActionPerformed

    private void setupNewSource(long theSource)
    {
        if ( theSource != 0 )
        {
            setDialogTitle();
        }
    }

    private void SelectSourceByNameActionPerformed(java.awt.event.ActionEvent evt) throws Exception {//GEN-FIRST:event_SelectSourceByNameActionPerformed

        if ( CloseCurrentSource() )
        {
            DTwainSelectSourceByNameDialog dlg = new DTwainSelectSourceByNameDialog(this, true);
            dlg.setVisible(true);
            if ( dlg.isOkPressed() )
            {
                String sName = dlg.getSourceName();
                try
                {
                    this.m_SourceWrapper = mainTwainSession.selectSource( sName );
                    setupNewSource(this.m_SourceWrapper.getSourceHandle());
                }
                catch (DTwainJavaAPIException e)
                { }
            }
        }
    }//GEN-LAST:event_SelectSourceByNameActionPerformed

    private void SelectSourceCustomActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_SelectSourceCustomActionPerformed
        if ( CloseCurrentSource() )
        {
            DTwainSelectSourceCustomDialog dlg = new DTwainSelectSourceCustomDialog(this, true, mainTwainSession);
            dlg.setVisible(true);
            long localTwainSource = dlg.getSelectedSource();
            if ( localTwainSource != 0 )
                setupNewSource(localTwainSource);
        }
    }//GEN-LAST:event_SelectSourceCustomActionPerformed


    private boolean CloseCurrentSource()
    {
        if ( m_SourceWrapper != null )
        {
            if ( m_SourceWrapper.getSourceHandle() != 0 )
            {
                int nValue = JOptionPane.showOptionDialog(null, "Close current open Twain Source?", "Close Current Source",
                                                          JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if ( nValue == JOptionPane.YES_OPTION)
                {
                    try
                    {
                        m_SourceWrapper.close();
                        return true;
                    }
                    catch (DTwainJavaAPIException e )
                    {}
                }
                else
                    return false;
            }
        }
        return true;
    }

    private void AcquireCompressNoneActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_AcquireCompressNoneActionPerformed
        AcquireCompressedHelper(DTwainConstants.CompressionType.NONE, DTwainConstants.PixelType.DEFAULT.value());
    }//GEN-LAST:event_AcquireCompressNoneActionPerformed

    private void AcquireJPEGCompressedActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_AcquireJPEGCompressedActionPerformed
        AcquireCompressedHelper(DTwainConstants.CompressionType.JPEG, DTwainConstants.PixelType.DEFAULT.value());
    }//GEN-LAST:event_AcquireJPEGCompressedActionPerformed

    private void AcquireGroup4CompressedActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException  {//GEN-FIRST:event_AcquireGroup4CompressedActionPerformed
        AcquireCompressedHelper(DTwainConstants.CompressionType.GROUP4, DTwainConstants.PixelType.BW.value());
    }//GEN-LAST:event_AcquireGroup4CompressedActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DTwainFullDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DTwainFullDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DTwainFullDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DTwainFullDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run()
            {
                DTwainFullDemo fullDemo = null;
                try
                {
                    fullDemo = new DTwainFullDemo();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                fullDemo.setVisible(true);
                try
                {
                    fullDemo.startTwainSession();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AcquireBuffered;
    private javax.swing.JMenuItem AcquireCompressNone;
    private javax.swing.JMenu AcquireCompressed;
    private javax.swing.JMenu AcquireFile;
    private javax.swing.JMenuItem AcquireFileBMP;
    private javax.swing.JMenuItem AcquireFileEMF;
    private javax.swing.JMenuItem AcquireFileGIF;
    private javax.swing.JMenuItem AcquireFileICO;
    private javax.swing.JMenuItem AcquireFileICOLARGE;
    private javax.swing.JMenuItem AcquireFileJP2;
    private javax.swing.JMenuItem AcquireFileJPEG;
    private javax.swing.JMenuItem AcquireFileJPEGXR;
    private javax.swing.JMenuItem AcquireFilePCX;
    private javax.swing.JMenuItem AcquireFilePDF;
    private javax.swing.JMenuItem AcquireFilePNG;
    private javax.swing.JMenuItem AcquireFilePSD;
    private javax.swing.JMenuItem AcquireFilePSLevel1;
    private javax.swing.JMenuItem AcquireFilePSLevel2;
    private javax.swing.JMenuItem AcquireFileTGA;
    private javax.swing.JMenuItem AcquireFileTIFFFlate;
    private javax.swing.JMenuItem AcquireFileTIFFG3;
    private javax.swing.JMenuItem AcquireFileTIFFG4;
    private javax.swing.JMenuItem AcquireFileTIFFJPEG;
    private javax.swing.JMenuItem AcquireFileTIFFLZW;
    private javax.swing.JMenuItem AcquireFileTIFFPackBits;
    private javax.swing.JMenuItem AcquireFileWBMP;
    private javax.swing.JMenuItem AcquireFileWMF;
    private javax.swing.JMenu AcquireFileWithDriver;
    private javax.swing.JMenuItem AcquireGroup31Compressed;
    private javax.swing.JMenuItem AcquireGroup31dEOLCompressed;
    private javax.swing.JMenuItem AcquireGroup32dCompressed;
    private javax.swing.JMenuItem AcquireGroup4Compressed;
    private javax.swing.JMenuItem AcquireJPEGCompressed;
    private javax.swing.JMenuItem AcquireNative;
    private javax.swing.JMenuItem BMPFileMode;
    private javax.swing.JMenuItem CloseSource;
    private javax.swing.JMenuItem DEJAVUFileMode;
    private javax.swing.JCheckBoxMenuItem DiscardBlankPages;
    private javax.swing.JCheckBoxMenuItem DisplaySourceUI;
    private javax.swing.JMenuItem EXIFFileMode;
    private javax.swing.JMenuItem ExitDemo;
    private javax.swing.JMenuItem FPXFileMode;
    private javax.swing.JMenuItem JP2FileMode;
    private javax.swing.JMenuItem JPEGFileMode;
    private javax.swing.JMenuItem JPXFileMode;
    private javax.swing.JCheckBoxMenuItem LogCalls;
    private javax.swing.JMenuItem PDFA2FileMode;
    private javax.swing.JMenuItem PDFAFileMode;
    private javax.swing.JMenuItem PDFFileMode;
    private javax.swing.JMenuItem PDFRasterFileMode;
    private javax.swing.JMenuItem PICTFileMode;
    private javax.swing.JMenuItem PNGFileMode;
    private javax.swing.JMenuItem SPIFFFileMode;
    private javax.swing.JMenuItem SelectDefaultSource;
    private javax.swing.JMenuItem SelectSource;
    private javax.swing.JMenuItem SelectSourceByName;
    private javax.swing.JMenuItem SelectSourceCustom;
    private javax.swing.JCheckBoxMenuItem ShowImagePreview;
    private javax.swing.JMenuItem SourceProperties;
    private javax.swing.JMenuItem TIFFFileMode;
    private javax.swing.JMenuItem TIFFMultipageFileMode;
    private javax.swing.JMenuItem XBMFileMode;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    // End of variables declaration//GEN-END:variables
}
