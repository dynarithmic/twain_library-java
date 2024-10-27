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
package com.dynarithmic.twain;

import java.util.List;
import java.util.TreeMap;

import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.JNIVersion;
import com.dynarithmic.twain.exceptions.DTwainInitException;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.TwainImageInfo;
import com.dynarithmic.twain.highlevel.TwainOCRInfo;
import com.dynarithmic.twain.highlevel.TwainAcquireArea;
import com.dynarithmic.twain.highlevel.TwainAcquisitionArray;
import com.dynarithmic.twain.highlevel.TwainAppInfo;
import com.dynarithmic.twain.highlevel.BufferedStripInfo;
import com.dynarithmic.twain.highlevel.BufferedTileInfo;
import com.dynarithmic.twain.highlevel.ExtendedImageInfo;
import com.dynarithmic.twain.highlevel.JNITwainAcquireOptions;
import com.dynarithmic.twain.highlevel.PDFTextElement;
import com.dynarithmic.twain.highlevel.TwainFrameDouble;
import com.dynarithmic.twain.highlevel.TwainImageData;
import com.dynarithmic.twain.highlevel.TwainSourceInfo;
import com.dynarithmic.twain.highlevel.TwainStartupOptions;
import com.dynarithmic.twain.lowlevel.TW_IDENTITY;
import com.dynarithmic.twain.lowlevel.TW_IMAGEMEMXFER;
import com.dynarithmic.twain.lowlevel.TW_UINT16;
import com.dynarithmic.twain.lowlevel.TW_UINT32;
import com.dynarithmic.twain.lowlevel.TwainTriplet;

public class DTwainJavaAPI
{
    static private class DTwainModuleNames
    {
        public final String s_DTwainDLLName;
        public final String s_DTwainJNIName;
        public DTwainModuleNames(String n1, String n2)
              { s_DTwainDLLName = n1; s_DTwainJNIName = n2; }
    }

    private long  m_LibraryHandle;
    private int m_DLLName = JNIVersion.JNI_32U;
    private static final TreeMap<Integer, DTwainModuleNames> s_DLLMap;
//  private static final Logger logger = LogManager.getLogger(DTwainJavaAPI.class.getName());

    /**
     * Instantiates a DTwainJavaAPI interface that allows communication to the
     * JNI layer.  This function must be called before calling any other
     * DTwain API Java function.
     * <p>
     * The underlying JNI layer that will be used will be based on whether the JVM is running
     * 64-bit or 32-bit.  If either 32 or 64-bit the Unicode based version of DTWAIN will be utilized.
     */
    public DTwainJavaAPI()
    {
        m_LibraryHandle = 0;
        m_DLLName = DTwainGlobalOptions.Is64BitArchitecture()?JNIVersion.JNI_64U:JNIVersion.JNI_32U;
    }

    /**
     * Instantiates a DTwainJavaAPI interface that allows communication to the
     * JNI layer.  This function must be called before calling any other
     * DTwain API Java function.
     * <p>
     * The underlying JNI layer that will be used will be based on a 32-bit, Unicode-based
     * system.  If 64-bit Unicode is desired, see DTwainJavaAPI(int)
     *
     * @param  dllname One of the DTwainConstants.JNIVersion values denoting the JNI DLL to use.
     *
     * JNI_32
     * JNI_32U
     * JNI_64
     * JNI_64U
     * JNI_32D
     * JNI_32UD
     * JNI_64D
     * JNI_64UD
     */
    public DTwainJavaAPI(int dllName) throws DTwainInitException
    {
        m_LibraryHandle = 0;
        if ( s_DLLMap.get(dllName) != null )
            m_DLLName = dllName;
        else
            throw new DTwainInitException("Invalid DLL Constant \"" + dllName + "\" used");
    }

    private void InitialLoadLibrary() throws DTwainJavaAPIException 
    {
        try
        {
        // Load the JNI DLL
        System.loadLibrary(s_DLLMap.get(m_DLLName).s_DTwainJNIName);

        // Have the JNI DLL start the internal DTWAIN DLL's
        DTWAIN_LoadLibrary(s_DLLMap.get(m_DLLName).s_DTwainDLLName, "");
        }
        catch (UnsatisfiedLinkError e) 
        {
            System.out.println(e);
            System.out.println("Conflict in 32/64 bit versions between the Java runtime and DTWAIN JNI layer (bitness does not match)\nEither set the proper runtime environment (32-bit, 64-bit) in your Java runtime or");
            System.out.println("call com.dynarithmic.twain.DTwainGlobalOptions.setJNIVersion() to set the proper JNI version to use (32-bit, 64-bit) to match the Java runtime being used");
        }
    }

    private boolean EndLoadLibrary() throws DTwainJavaAPIException
    {
       m_LibraryHandle = DTWAIN_SysInitialize();
       return m_LibraryHandle != 0;
    }

    public boolean DTWAIN_JavaSysInitialize() throws DTwainJavaAPIException 
    {
        if ( m_LibraryHandle != 0 )
            return true;
        InitialLoadLibrary();
        return EndLoadLibrary();
    }

    public boolean DTWAIN_JavaSysInitialize(TwainStartupOptions startOpts) throws DTwainJavaAPIException
    {
        if ( m_LibraryHandle != 0 )
            return true;
        InitialLoadLibrary();
        String resPath = startOpts.getResourcePath();
        if ( !resPath.isEmpty() )
            DTWAIN_SetResourcePath(resPath);

        String dsmSearchOrder = startOpts.getDSMSearchOrder();
        String userDir = startOpts.getDSMUserDirectory();
        boolean bUseSearchOrder = false;
        if (!dsmSearchOrder.equals(TwainStartupOptions.DEFAULT_SEARCH))
            bUseSearchOrder = true;
        if ( bUseSearchOrder )
            DTWAIN_SetDSMSearchOrderEx(dsmSearchOrder, userDir);
        boolean libLoaded = EndLoadLibrary();
        if ( libLoaded )
        {
            DTWAIN_SetAppInfo(startOpts.getAppInfo());
            DTWAIN_SetLanguage(startOpts.getAppInfo().getLanguage().ordinal());
            String sLangToUse = startOpts.getResourceLanguage();
            if ( sLangToUse != null )
                DTWAIN_LoadCustomStringResource(sLangToUse);
            DTWAIN_SetTwainDSM(startOpts.getDSMToUse().value());
            if ( startOpts.isAutoStartSession() )
            {
                boolean sessionStarted = DTWAIN_StartTwainSession();
                if (!sessionStarted)
                {
                    throw new DTwainJavaAPIException("Twain Session Could not start");
                }
            }
        }
        return libLoaded;
    }

    public boolean DTWAIN_JavaSysDestroy() throws DTwainJavaAPIException
    {
        if ( m_LibraryHandle != 0 )
        {
            int val =  DTWAIN_SysDestroy();
            if ( val == 1 )
            {
                DTWAIN_FreeLibrary();
                m_LibraryHandle = 0;
                return true;
            }
        }
        return false;
    }

    public boolean startTwainThread()
    {
            if ( m_LibraryHandle == 0 )
                    return false;
            try
            {
                return DTWAIN_StartThread(m_LibraryHandle);
            }
            catch (DTwainJavaAPIException e)
            {
//                logger.debug(e);
            }
            return false;
    }

    public boolean endTwainThread()
    {
        if ( m_LibraryHandle == 0 )
            return false;
        try
        {
            return DTWAIN_EndThread(m_LibraryHandle);
        }
        catch (DTwainJavaAPIException e)
        {
//            logger.debug(e);
        }
        return false;
    }

    public long getLibraryHandle() { return m_LibraryHandle; }

    public TwainAcquisitionArray DTWAIN_AcquireImages(long Source, JNITwainAcquireOptions options) throws DTwainJavaAPIException
    {
        int pixelType = options.getPixelType().value();
        int sourceOpenAfterAcquire = options.getSourceStateAfterAcquire().ordinal();
        AcquireType acqType = options.getAcquireType();
        switch (acqType)
        {
            case NATIVE:
                return DTWAIN_AcquireNative(Source, pixelType, options.getMaxAcquisitions(),
                                            options.isShowUI(), sourceOpenAfterAcquire==1?true:false);
            case BUFFERED:
                return DTWAIN_AcquireBuffered(Source, pixelType, options.getMaxAcquisitions(),
                          options.isShowUI(), sourceOpenAfterAcquire==1);
            default:
                throw new DTwainJavaAPIException("DTWAIN_AcquireImages only can be used to acquire to image files.  Use DTWAIN_AcquireFile to acquire to image files.");
        }
    }

    // dynamically load/unload DTWAIN DLL
    public native int DTWAIN_LoadLibrary(String s, String resPath) throws DTwainJavaAPIException;
    public native int DTWAIN_FreeLibrary() throws DTwainJavaAPIException;

    // No argument functions
    // 0 arguments, returning an array of long
    public native long[] DTWAIN_EnumSources() throws DTwainJavaAPIException;

    public native boolean DTWAIN_IsTwainAvailable() throws DTwainJavaAPIException;
    public native long DTWAIN_SysInitialize() throws DTwainJavaAPIException;
    public native int DTWAIN_SysDestroy() throws DTwainJavaAPIException;
    public native long DTWAIN_SelectSource() throws DTwainJavaAPIException;
    public native long DTWAIN_SelectDefaultSource() throws DTwainJavaAPIException;
    public native int DTWAIN_GetLastError() throws DTwainJavaAPIException;
    public native int DTWAIN_GetTwainMode() throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsSessionEnabled() throws DTwainJavaAPIException;
    public native boolean DTWAIN_StartTwainSession() throws DTwainJavaAPIException;
    public native int DTWAIN_EndTwainSession() throws DTwainJavaAPIException;
    public native int DTWAIN_GetCountry() throws DTwainJavaAPIException;
    public native int DTWAIN_GetLanguage() throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsMsgNotifyEnabled() throws DTwainJavaAPIException;
    public native long DTWAIN_GetTwainHwnd() throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAcquiring() throws DTwainJavaAPIException;
    public native long DTWAIN_CreateAcquisitionArray() throws DTwainJavaAPIException;
    public native int DTWAIN_ClearErrorBuffer() throws DTwainJavaAPIException;
    public native int DTWAIN_GetErrorBufferThreshold() throws DTwainJavaAPIException;
    public native int DTWAIN_GetTwainAvailability() throws DTwainJavaAPIException;
    public native DTwainVersionInfo DTWAIN_GetVersionInfo() throws DTwainJavaAPIException;

    // 1 argument functions
    public native boolean DTWAIN_StartThread(long h) throws DTwainJavaAPIException;
    public native boolean DTWAIN_EndThread(long h) throws DTwainJavaAPIException;
    public native int DTWAIN_SetTwainMode(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCountry(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_SetLanguage(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableMsgNotify(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableTripletsNotify(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_OpenSourcesOnSelect(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_SetQueryCapSupport(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_SetTwainTimeout(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_SetErrorBufferThreshold(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_AppHandlesExceptions(int v) throws DTwainJavaAPIException;
    public native int DTWAIN_SetTwainDSM(int v) throws DTwainJavaAPIException;

    // 1 argument (argument is Source)
    public native int DTWAIN_OpenSource(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_CloseSource(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_CloseSourceUI(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetDefaultSource(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsSourceAcquiring(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsSourceOpen(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetCurrentPageNum(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetMaxAcquisitions(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetMaxPagesToAcquire(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsUIControllable( long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsUIEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsIndicatorSupported( long Source ) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsIndicatorEnabled( long Source ) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsThumbnailSupported( long Source ) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsThumbnailEnabled( long Source ) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsDeviceEventSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsUIOnlySupported(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_ShowUIOnly(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsPrinterSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsFeederEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsFeederLoaded(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsFeederSupported(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_IsFeederSensitive(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_FeedPage(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_RewindPage(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_ClearPage(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoFeedEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoFeedSupported(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetFeederFuncs(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsPaperDetectable(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsDuplexSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsDuplexEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsCustomDSDataSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_ClearPDFText(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoDeskewSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoDeskewEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoBorderDetectSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoBorderDetectEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsLightPathSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsLampSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsLampEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsLightSourceSupported(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetMaxRetryAttempts(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetCurrentRetryCount(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsSkipImageInfoError(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsExtImageInfoSupported(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_InitExtImageInfo(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetExtImageInfo(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_FreeExtImageInfo(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_FlushAcquiredPages(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsFileSystemSupported( long Source ) throws DTwainJavaAPIException;
    public native int DTWAIN_GetBlankPageAutoDetection(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsBlankPageDetectionOn(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoScanEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsDeviceOnLine( long Source ) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoBrightEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsAutoRotateEnabled(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsRotationSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsPatchCapsSupported(long Source) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsPatchDetectEnabled(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetAllCapsToDefault(long Source) throws DTwainJavaAPIException;

    // 1 argument functions returning array
    public native int[] DTWAIN_EnumSupportedCaps(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumExtendedCaps(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumCustomCaps(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumSourceUnits(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumFileXferFormats(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumCompressionTypes(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumPrinterStringModes(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumTwainPrintersArray(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumOrientations(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumPaperSizes(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumPixelTypes(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumBitDepths(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumJobControls(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumLightPaths(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumLightSources(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_GetLightSources(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumExtImageInfoTypes(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumAlarms(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumNoiseFilters(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumPatchMaxRetries(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumPatchMaxPriorities(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumPatchSearchModes(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumPatchTimeOutValues(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_GetPatchPriorities(long Source) throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumPatchPriorities(long Source) throws DTwainJavaAPIException;

    public native String[] DTWAIN_EnumTopCameras(long Source) throws DTwainJavaAPIException;
    public native String[] DTWAIN_EnumBottomCameras(long Source) throws DTwainJavaAPIException;
    public native String[] DTWAIN_EnumCameras(long Source) throws DTwainJavaAPIException;
    public native String[] DTWAIN_EnumCamerasEx(long Source, int whichCamera) throws DTwainJavaAPIException;

    // 2 argument functions
    public native boolean DTWAIN_IsCapSupported(long Source, int capValue) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsFileXferSupported(long Source, int lFileType) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableIndicator(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_SetMaxAcquisitions(long Source, int maxAcquisitions) throws DTwainJavaAPIException;
    public native int DTWAIN_SetSourceUnit(long Source, int Unit) throws DTwainJavaAPIException;
    public native String DTWAIN_GetCapDataTypeAsClassName(long Source, int capValue) throws DTwainJavaAPIException;
    public native int DTWAIN_GetCapDataType(long Source, int capValue) throws DTwainJavaAPIException;
    public native int DTWAIN_GetCapArrayType(long Source, int capValue) throws DTwainJavaAPIException;


    public native boolean DTWAIN_IsCompressionSupported(long Source, int lCompression) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsPrinterEnabled(long Source, int printer) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableFeeder(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_EnablePrinter(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableThumbnail(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_ForceAcquireBitDepth(long Source, int lBitDepth) throws DTwainJavaAPIException;
    public native int DTWAIN_SetAvailablePrinters(long Source, int lAvailPrinters) throws DTwainJavaAPIException;
    public native int DTWAIN_SetDeviceNotifications(long Source, int lNotifications) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPrinterStartNumber(long Source, int startNumber) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableAutoFeed(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableDuplex(long Source, boolean enable) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsOrientationSupported(long Source, int orientation) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsPaperSizeSupported(long Source, int paperSize) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsPixelTypeSupported(long Source, int pixelType) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFASCIICompression(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPostScriptType(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFJpegQuality(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFOrientation(long Source, int setting) throws DTwainJavaAPIException;

    public native int DTWAIN_SetTIFFInvert(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetTIFFCompressType(long Source, int setting) throws DTwainJavaAPIException;

    public native boolean DTWAIN_IsJobControlSupported(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableJobFileHandling(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableAutoDeskew(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableAutoBorderDetect(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_SetLightPath(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableLamp(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_SetMaxRetryAttempts(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCurrentRetryCount(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SkipImageInfoError(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_SetMultipageScanMode(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetAlarmVolume(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableAutoScan(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_ClearBuffers(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_SetFeederAlignment(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetFeederOrder(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetMaxBuffers(long Source, int setting) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsMaxBuffersSupported(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableAutoBright(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableAutoRotate(long Source, boolean enable) throws DTwainJavaAPIException;
    public native int DTWAIN_SetNoiseFilter(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPixelFlavor(long Source, int setting) throws DTwainJavaAPIException;
    public native int DTWAIN_SetRotation(long Source, double setting) throws DTwainJavaAPIException;

    public native int DTWAIN_GetSourceUnit          (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetDeviceNotifications (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetDeviceEvent         (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetCompressionSize     (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetPrinterStartNumber  (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetDuplexType          (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetLightPath           (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetAlarmVolume         (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetBatteryMinutes      (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetBatteryPercent      (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetFeederAlignment     (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetFeederOrder         (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetMaxBuffers          (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetNoiseFilter         (long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_GetPixelFlavor         (long Source) throws DTwainJavaAPIException;
    public native double DTWAIN_GetRotation         (long Source) throws DTwainJavaAPIException;

    public native double DTWAIN_GetContrast(long Source) throws DTwainJavaAPIException;
    public native double DTWAIN_GetBrightness(long Source) throws DTwainJavaAPIException;
    public native double DTWAIN_GetResolution(long Source) throws DTwainJavaAPIException;

    public native int DTWAIN_GetCapOperations(long Source, int capability) throws DTwainJavaAPIException;

    public native double[] DTWAIN_EnumContrastValues(long Source, boolean bExpandIfRange) throws DTwainJavaAPIException;
    public native double[] DTWAIN_EnumBrightnessValues(long Source, boolean bExpandIfRange) throws DTwainJavaAPIException;
    public native double[] DTWAIN_EnumResolutionValues(long Source, boolean bExpandIfRange) throws DTwainJavaAPIException;
    public native int []   DTWAIN_EnumMaxBuffers(long Source, boolean bExpandIfRange) throws DTwainJavaAPIException;

    public native int DTWAIN_SetCapValuesInt(long Source, int capValue, int setType, int[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesDouble(long Source, int capValue, int setType, double[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesFrame(long Source, int capValue, int setType, TwainFrameDouble[] frames) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesString(long Source, int capValue, int setType, String[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValues(long Source, int capValue, int setType, List<Object> vals) throws DTwainJavaAPIException;

    public native int DTWAIN_SetCapValuesIntEx(long Source, int capValue, int setType, int containerType, int[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesDoubleEx(long Source, int capValue, int setType, int containerType, double[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesFrameEx(long Source, int capValue, int setType, int containerType, TwainFrameDouble[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesStringEx(long Source, int capValue, int setType, int containerType, String[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesIntEx2(long Source, int capValue, int setType, int containerType, int nDataType, int[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesDoubleEx2(long Source, int capValue, int setType, int containerType, int nDataType, double[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesFrameEx2(long Source, int capValue, int setType, int containerType, int nDataType, TwainFrameDouble[] vals) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCapValuesStringEx2(long Source, int capValue, int setType, int containerType, int nDataType, String[] vals) throws DTwainJavaAPIException;

    public native int[] DTWAIN_GetCapValuesInt(long Source, int capValue, int getType) throws DTwainJavaAPIException;
    public native double[] DTWAIN_GetCapValuesDouble(long Source, int capValue, int getType) throws DTwainJavaAPIException;
    public native TwainFrameDouble[] DTWAIN_GetCapValuesFrame(long Source, int capValue, int getType) throws DTwainJavaAPIException;
    public native String[] DTWAIN_GetCapValuesString(long Source, int capValue, int getType) throws DTwainJavaAPIException;
    public native List<Object> DTWAIN_GetCapValues(long Source, int capValue, int getType) throws DTwainJavaAPIException;
    public native boolean DTWAIN_GetCapValuesEx2(long Source, int capValue, int getType, int containerType, int nDataType, List<Object> vals) throws DTwainJavaAPIException;

    public native int[] DTWAIN_GetCapValuesIntEx(long Source, int capValue, int getType, int containerType) throws DTwainJavaAPIException;
    public native double[] DTWAIN_GetCapValuesDoubleEx(long Source, int capValue, int getType, int containerType) throws DTwainJavaAPIException;
    public native String[] DTWAIN_GetCapValuesStringEx(long Source, int capValue, int getType, int containerType) throws DTwainJavaAPIException;
    public native TwainFrameDouble[] DTWAIN_GetCapValuesFrameEx(long Source, int capValue, int getType, int containerType) throws DTwainJavaAPIException;
    public native int[] DTWAIN_GetCapValuesIntEx2(long Source, int capValue, int getType, int containerType, int nDataType) throws DTwainJavaAPIException;
    public native double[] DTWAIN_GetCapValuesDoubleEx2(long Source, int capValue, int getType, int containerType, int nDataType) throws DTwainJavaAPIException;
    public native String[] DTWAIN_GetCapValuesStringEx2(long Source, int capValue, int getType, int containerType, int nDataType) throws DTwainJavaAPIException;

    public native TwainFrameDouble[] DTWAIN_GetCapValuesFrameEx2(long Source, int capValue, int getType, int containerType, int nDataType) throws DTwainJavaAPIException;
    public native int DTWAIN_GetCapContainer(long Source, int capValue, int getType);

    public native int DTWAIN_SetFileXferFormat(long Source, int fileType, boolean setCurrent) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCompressionType(long Source, int compression, boolean setCurrent) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPrinter(long Source, int printer, boolean setCurrent) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPrinterStringMode(long Source, int setting, boolean setCurrent) throws DTwainJavaAPIException;
    public native int DTWAIN_SetOrientation(long Source, int setting, boolean setCurrent) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPaperSize(long Source, int setting, boolean setCurrent) throws DTwainJavaAPIException;
    public native int DTWAIN_SetBitDepth(long Source, int setting, boolean setCurrent) throws DTwainJavaAPIException;
    public native int DTWAIN_SetJobControl(long Source, int setting, boolean setCurrent) throws DTwainJavaAPIException;
    public native int DTWAIN_SetManualDuplexMode(long Source, int setting, boolean setCurrent) throws DTwainJavaAPIException;
    public native TwainAcquireArea DTWAIN_GetAcquireArea(long Source, int getType) throws DTwainJavaAPIException;
    public native TwainAcquireArea DTWAIN_SetAcquireArea(long Source, int setType, TwainAcquireArea aArea) throws DTwainJavaAPIException;

    public native TwainImageInfo DTWAIN_GetImageInfo(long Source) throws DTwainJavaAPIException;
    public native BufferedTileInfo DTWAIN_GetAcquiredTileInfo(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetTwainLog(int nFlags, String szFile) throws DTwainJavaAPIException;

    // acquisitions
    public native TwainAcquisitionArray DTWAIN_AcquireNative(long Source, int pixelType, int maxPages, boolean showUI, boolean closeSource) throws DTwainJavaAPIException;
    public native TwainAcquisitionArray DTWAIN_AcquireBuffered(long Source, int pixelType, int maxPages, boolean showUI, boolean closeSource) throws DTwainJavaAPIException;
    public native int DTWAIN_AcquireFile(long Source, String filename, int fileType, int fileFlags, int pixelType, int maxPages, boolean showUI, boolean closeSource) throws DTwainJavaAPIException;

    // custom ds data
    public native byte[] DTWAIN_GetCustomDSData(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCustomDSData(long Source, byte[] customData) throws DTwainJavaAPIException;

    public native int DTWAIN_SetAcquireImageScale(long Source, double xScale, double yScale) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFPageSize(long Source, int pageSize, double customWidth, double customHeight) throws DTwainJavaAPIException;

    // String functions
    public native TwainAppInfo DTWAIN_GetAppInfo() throws DTwainJavaAPIException;
    public native TwainSourceInfo DTWAIN_GetSourceInfo(long Source) throws DTwainJavaAPIException;
    public native String DTWAIN_GetAuthor(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetAuthor(long Source, String author) throws DTwainJavaAPIException;
    public native String DTWAIN_GetCaption(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetCaption(long Source, String caption) throws DTwainJavaAPIException;
    public native long DTWAIN_SelectSourceByName(String sourceName) throws DTwainJavaAPIException;
    public native String DTWAIN_GetNameFromCap(int capability) throws DTwainJavaAPIException;
    public native int DTWAIN_GetCapFromName(String capName) throws DTwainJavaAPIException;
    public native int DTWAIN_TwainSave(String sCmd) throws DTwainJavaAPIException;
    public native String DTWAIN_GetTwainNameFromConstant(int constantType, int constant) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFAuthor(long Source, String sAuthor) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFCreator(long Source, String sCreator) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFTitle(long Source, String sTitle) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFSubject(long Source, String sSubject) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFKeywords(long Source, String sKeywords) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFEncryption(long Source, boolean useEncryption, String userPass, String ownerPass, int permissions, boolean useStringEncrypt) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPostScriptTitle(long Source, String sTitle) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFPageScale(long Source, int scaleOpts, double xScale, double yScale);
    public native boolean DTWAIN_IsDIBBlank(long hDib, double threshHold) throws DTwainJavaAPIException;
    public native int DTWAIN_SetTempFileDirectory(String dirName) throws DTwainJavaAPIException;
    public native String DTWAIN_GetTempFileDirectory() throws DTwainJavaAPIException;
    public native int DTWAIN_LogMessage(String sMsg) throws DTwainJavaAPIException;
    public native long DTWAIN_SelectSource2(String sTitle, int xPos, int yPos, int options) throws DTwainJavaAPIException;
    public native String DTWAIN_GetErrorString(int errorNum) throws DTwainJavaAPIException;
    public native int DTWAIN_AcquireFileEx(long Source, String[] filenames, int fileType, int fileFlags, int pixelType, int maxPages, boolean showUI, boolean closeSource) throws DTwainJavaAPIException;
    public native TwainImageData DTWAIN_GetCurrentAcquiredImage(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetBlankPageDetection(long Source, double threshold, int autodetect, int detectOptions, boolean bSet) throws DTwainJavaAPIException;
    public native int DTWAIN_SetAcquireArea2(long Source, TwainAcquireArea area, int flags) throws DTwainJavaAPIException;
    public native TwainAcquireArea DTWAIN_GetAcquireArea2(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetAppInfo(TwainAppInfo aInfo) throws DTwainJavaAPIException;
    //
    public native int DTWAIN_InitOCRInterface() throws DTwainJavaAPIException;
    public native long DTWAIN_SelectOCREngine() throws DTwainJavaAPIException;
    public native long DTWAIN_SelectDefaultOCREngine() throws DTwainJavaAPIException;
    public native long DTWAIN_SelectOCREngineByName(String name) throws DTwainJavaAPIException;
    public native long[] DTWAIN_EnumOCRInterfaces() throws DTwainJavaAPIException;
    public native int[] DTWAIN_EnumOCRSupportedCaps(long Engine) throws DTwainJavaAPIException;
    public native int[] DTWAIN_GetOCRCapValuesInt(long Engine, int OCRCapValue, int getType) throws DTwainJavaAPIException;
    public native String[] DTWAIN_GetOCRCapValuesString(long Engine, int OCRCapValue, int getType) throws DTwainJavaAPIException;
    public native int DTWAIN_SetOCRCapValuesInt(long Engine, int OCRCapValue, int SetType, int[] CapValues) throws DTwainJavaAPIException;
    public native int DTWAIN_SetOCRCapValuesString(long Engine, int OCRCapValue, int SetType, String[] CapValues) throws DTwainJavaAPIException;
    public native int DTWAIN_ShutdownOCREngine(long OCREngine) throws DTwainJavaAPIException;
    public native boolean DTWAIN_IsOCREngineActivated(long OCREngine) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFOCRConversion(long Engine, int pageType, int fileType, int pixelType, int bitDepth, int options) throws DTwainJavaAPIException;
    public native int DTWAIN_SetPDFOCRMode(long Source, boolean bSet) throws DTwainJavaAPIException;
    public native int DTWAIN_ExecuteOCR(long Engine, String szFileName, int startPage, int endPage) throws DTwainJavaAPIException;
    public native TwainOCRInfo DTWAIN_GetOCRInfo(long Engine) throws DTwainJavaAPIException;
    public native byte [] DTWAIN_GetOCRText(long Engine, int pageNum) throws DTwainJavaAPIException;

    public native BufferedStripInfo DTWAIN_CreateBufferedStripInfo(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_SetBufferedTransferInfo(long Source, BufferedStripInfo info) throws DTwainJavaAPIException;
    public native int DTWAIN_SetBufferedTransferInfo(long Source, BufferedTileInfo info) throws DTwainJavaAPIException;
    public native int DTWAIN_GetBufferedStripData(long Source, BufferedStripInfo info) throws DTwainJavaAPIException;
    public native BufferedTileInfo DTWAIN_GetBufferedTileInfo(long Source) throws DTwainJavaAPIException;
    public native TW_IMAGEMEMXFER DTWAIN_GetBufferedTransferInfo(long Source) throws DTwainJavaAPIException;
    public native int DTWAIN_EndBufferedTransfer(long Source, BufferedStripInfo info) throws DTwainJavaAPIException;

    public native int DTWAIN_ResetCapValues(long Source, int nCapValue) throws DTwainJavaAPIException;
    public native String DTWAIN_GetVersionString() throws DTwainJavaAPIException;
    public native String DTWAIN_GetShortVersionString()throws DTwainJavaAPIException;
    public native int DTWAIN_SetResourcePath(String path)throws DTwainJavaAPIException;
    public native String DTWAIN_GetLibraryPath()throws DTwainJavaAPIException;
    public native int DTWAIN_SetDSMSearchOrderEx(String searchOrder, String customDirectory)throws DTwainJavaAPIException;

    public native int DTWAIN_CallDSMProc(TW_IDENTITY src, TW_IDENTITY dest, TW_UINT32 DG, TW_UINT16 DAT, TW_UINT16 MSG, Object pData) throws DTwainJavaAPIException;
    public native int DTWAIN_CallDSMProc(TW_IDENTITY src, TW_IDENTITY dest, long DG, int DAT, int MSG, Object pData) throws DTwainJavaAPIException;
    public native int DTWAIN_CallDSMProc(TwainTriplet triplet) throws DTwainJavaAPIException;

    public native TW_IDENTITY DTWAIN_GetTwainAppID() throws DTwainJavaAPIException;
    public native TW_IDENTITY DTWAIN_GetSourceID(long Source) throws DTwainJavaAPIException;
    public native String DTWAIN_GetTwainStringName(int category, int twainID) throws DTwainJavaAPIException;
    public native Object DTWAIN_CreateObjectFromTriplet(long DG, int DAT, int MSG) throws DTwainJavaAPIException;
    public native String DTWAIN_GetActiveDSMPath() throws DTwainJavaAPIException;
    public native int DTWAIN_LoadCustomStringResource(String langName) throws DTwainJavaAPIException;
    public native long DTWAIN_SelectSource2Ex(String title, int xPos, int yPos, String includeNames, String excludeNames, String nameMapping, int options) throws DTwainJavaAPIException;
    public native TwainTriplet DTWAIN_GetCurrentTwainTriplet() throws DTwainJavaAPIException;
    public native int DTWAIN_AddPDFText(long Source, PDFTextElement textElement) throws DTwainJavaAPIException;
    public native int DTWAIN_SetAcquireImageNegative(long Source, boolean isNegative) throws DTwainJavaAPIException;
    public native int DTWAIN_SetFileAutoIncrement(long Source, long incValue, boolean resetOnAcquire, boolean enable) throws DTwainJavaAPIException;
    public native long DTWAIN_AllocateMemory(int memorySize) throws DTwainJavaAPIException;
    public native int DTWAIN_FreeMemory(long memoryHandle) throws DTwainJavaAPIException;
    public native boolean DTWAIN_SetAcquireStripBuffer(long Source, long memoryHandle) throws DTwainJavaAPIException;
    public native int DTWAIN_EnableAutoFeedNotify(int latency, boolean enable);
    public native boolean DTWAIN_SetCamera(long Source, String cameraName);
    public native int [] DTWAIN_EnumSupportedSinglePageFileTypes();
    public native int [] DTWAIN_EnumSupportedMultiPageFileTypes();
    public native String DTWAIN_GetFileTypeName(int fileType);
    public native String DTWAIN_GetFileTypeExtension(int fileType);
    public native ExtendedImageInfo DTWAIN_GetExtendedImageInfo(long Source);
    public native String DTWAIN_GetVersionCopyright();
    public native String DTWAIN_GetSessionDetails(int indentSize, boolean refresh);
    public native String DTWAIN_GetSourceDetails(String sourceNames, int indentSize, boolean refresh);
    static
    {
        s_DLLMap = new TreeMap<>();
        s_DLLMap.put(JNIVersion.JNI_32, new DTwainModuleNames("dtwain32", "dtwainjni32"));
        s_DLLMap.put(JNIVersion.JNI_32U, new DTwainModuleNames("dtwain32u","dtwainjni32u"));
        s_DLLMap.put(JNIVersion.JNI_64, new DTwainModuleNames("dtwain64", "dtwainjni64"));
        s_DLLMap.put(JNIVersion.JNI_64U, new DTwainModuleNames("dtwain64u", "dtwainjni64u"));
        s_DLLMap.put(JNIVersion.JNI_32D, new DTwainModuleNames("dtwain32d", "dtwainjni32d"));
        s_DLLMap.put(JNIVersion.JNI_32UD, new DTwainModuleNames("dtwain32ud","dtwainjni32ud"));
        s_DLLMap.put(JNIVersion.JNI_64D, new DTwainModuleNames("dtwain64d", "dtwainjni64d"));
        s_DLLMap.put(JNIVersion.JNI_64UD, new DTwainModuleNames("dtwain64ud", "dtwainjni64ud"));
    }
}