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
package com.dynarithmic.twain.highlevel;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainConstants.AcquireFileOptions;
import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.CompressionType;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.DTwainConstants.PixelType;
import com.dynarithmic.twain.DTwainConstants.SourceStateAfterAcquire;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.BlankPageHandlingOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.GeneralOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.JobControlOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PDFOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PaperHandlingOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.UserInterfaceOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions.FileTransferFlags;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions.FileTypeInfo;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions.FilenameIncrementOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.FileTransferOptions.MultipageSaveOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PDFOptions.*;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PaperHandlingOptions.*;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.*;
import com.dynarithmic.twain.lowlevel.TwainConstants.CAP_JOBCONTROL;
import com.dynarithmic.twain.lowlevel.TW_IDENTITY;
import org.apache.commons.lang3.time.StopWatch;

public class TwainSource
{
    public class AcquireReturnInfo
    {
        ErrorCode returnCode;
        ImageHandler imageHandler;

        public AcquireReturnInfo(ErrorCode retCode, ImageHandler imageHandler)
        {
            this.returnCode = retCode;
            this.imageHandler = imageHandler;
        }

        public ErrorCode getReturnCode()
        {
            return this.returnCode;
        }

        public ImageHandler getImageHandler() { return this.imageHandler; }
    }

    private ErrorCode errorReturnCode = ErrorCode.ERROR_NONE;
    private TwainSession twainSession = null;
    private TwainSourceInfo sourceInfo = new TwainSourceInfo();
    private boolean isCreated = false;
    private boolean isOpened = false;
    private boolean isSelected = false;
    private AcquireCharacteristics acquireCharacteristics = new AcquireCharacteristics();
    private CapabilityInterface capabilityInterface = new CapabilityInterface();
    private BufferedTransferInfo bufferedTransferInfo = new BufferedTransferInfo();
    private DeviceCameraInfo deviceCameraInfo = new DeviceCameraInfo();
    private long sourceHandle = 0;
    public static int AcquireCanceled = 2;
    private TwainCallback messageCallback = new TwainCallback();
    private byte [] customDSData = new byte[0];
    private TW_IDENTITY m_SourceID = new TW_IDENTITY();

    public TwainSource()
    {}

    public TwainSource(TwainSession session, long sourceHandle) throws DTwainJavaAPIException
    {
        this.twainSession = session;
        this.sourceHandle = sourceHandle;
        reloadInfo();
    }

    public TwainSession getTwainSession()
    {
        return twainSession;
    }

    public long getSourceHandle()
    {
        return this.sourceHandle;
    }

  
    public TwainCallback getMessageCallback()
    {
        return this.messageCallback;
    }

    public TwainSource setMessageCallback(TwainCallback messageCallback)
    {
        this.messageCallback = messageCallback;
        return this;
    }

    public TwainSource setTwainSession(TwainSession twainSession)
    {
        this.twainSession = twainSession;
        return this;
    }

    public TwainSource setSourceHandle(long sourceHandle)
    {
        this.sourceHandle = sourceHandle;
        return this;
    }

    public TwainSource setCreated(boolean isCreated)
    {
        this.isCreated = isCreated;
        return this;
    }

    private void isValidSource() throws DTwainJavaAPIException
    {
        if ( twainSession == null )
            throw new DTwainJavaAPIException("No Twain Session associated with this Twain Source");
        if ( sourceHandle == 0 )
            throw new DTwainJavaAPIException("Invalid Twain Source");
    }

    // Non throwing test
    public boolean isValid()
    {
        return twainSession != null && sourceHandle != 0;
    }
    
    public void reloadInfo() throws DTwainJavaAPIException
    {
        if ( twainSession != null && sourceHandle != 0 )
            reloadInfo(twainSession.getTwainCharacteristics().isOpenSourcesOnSelectEnabled());
    }

    private void reloadInfo(boolean selectOverride) throws DTwainJavaAPIException
    {
        if ( twainSession != null && sourceHandle != 0 )
        {
            isSelected = true;
            if ( selectOverride )
            {
                this.sourceInfo = twainSession.getAPIHandle().DTWAIN_GetSourceInfo(sourceHandle);
                this.capabilityInterface.attach(this);
                this.bufferedTransferInfo.attach(this);
                this.deviceCameraInfo.attach(this);
                this.customDSData = twainSession.getAPIHandle().DTWAIN_GetCustomDSData(sourceHandle);
                isOpened = true;
            }
        }
    }

    public byte [] getCustomDSData() throws DTwainJavaAPIException
    {
        isValidSource();
        this.customDSData = twainSession.getAPIHandle().DTWAIN_GetCustomDSData(sourceHandle);
        return this.customDSData;
    }

    public TwainSource setCustomDSData(byte [] data) throws DTwainJavaAPIException
    {
        isValidSource();
        this.customDSData = Arrays.copyOf(data, data.length);
        twainSession.getAPIHandle().DTWAIN_SetCustomDSData(sourceHandle, this.customDSData);
        return this;
    }

    public TwainSourceInfo getInfo()
    {
        return this.sourceInfo;
    }

    public AcquireCharacteristics getAcquireCharacteristics()
    {
        return acquireCharacteristics;
    }

    public TwainSource setAcquireCharacteristics(AcquireCharacteristics acquireCharacteristics)
    {
        this.acquireCharacteristics = acquireCharacteristics;
        return this;
    }

    public CapabilityInterface getCapabilityInterface()
    {
        return this.capabilityInterface;
    }

    public BufferedTransferInfo getBufferedTransferInfo()
    {
        return this.bufferedTransferInfo;
    }

    public TwainSource setBufferedTransferInfo(BufferedTransferInfo info)
    {
        this.bufferedTransferInfo = info;
        return this;
    }

    //////////////////////
    void startApply() throws DTwainJavaAPIException
    {
        OptionsApplyer.apply(this, this.acquireCharacteristics.getLanguageSupportOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getDeviceParamsOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getPagesSupportOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getPowerMonitorOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getDoublefeedDetectionOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getAutoAdjustOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getBarcodeDetectionOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getPatchcodeDetectionOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getAutoCaptureOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getImageTypeOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getImageInformationOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getUserInterfaceOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getImageParameterOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getAudibleAlarmsOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getDeviceEventsOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getResolutionSupportOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getPaperHandlingOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getColorSupportOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getCapNegotiationOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getMICRSupportOptions());
        OptionsApplyer.apply(this, this.acquireCharacteristics.getImprinterSupportOptions());
    }

    private void prepareAcquisitions() throws DTwainJavaAPIException
    {
        if ( !preAcquireSetup() )
            return;
        startApply();
        if ( !postAcquireSetup())
            return;

        AcquireCharacteristics ac = acquireCharacteristics;
        DTwainJavaAPI handle = twainSession.getAPIHandle();
        TwainAcquireArea tAcquire = new TwainAcquireArea(ac.getPagesSupportOptions().getFrame());
        if (tAcquire.isFrameValid())
        {
            handle.DTWAIN_SetAcquireArea(sourceHandle,DTwainConstants.DTWAIN_AREASET,tAcquire);
        }
        // If job control is enabled, then get JNI layer to recognize this
        JobControlOptions jobOptions = acquireCharacteristics.getJobControlOptions();
        handle.DTWAIN_EnableJobFileHandling(sourceHandle,
                (jobOptions.getJobControl() != CAP_JOBCONTROL.TWJC_NONE));

        // Disable the manual duplex mode
        handle.DTWAIN_SetManualDuplexMode(sourceHandle, 0, false);

        // Get the duplex mode
        ManualDuplexMode mduplex = ac.getPaperHandlingOptions().getManualDuplexMode();
        switch(mduplex)
        {
            case NONE:
                break;
            default:
                // Turn off auto duplexing
                capabilityInterface.setDuplexEnabled(Arrays.asList(false), capabilityInterface.set());

                // Turn on manual duplex mode
                handle.DTWAIN_SetManualDuplexMode(sourceHandle, mduplex.ordinal(), true);
                break;
        }

        // Set the polarity
        handle.DTWAIN_SetAcquireImageNegative(sourceHandle, ac.getImageTypeOptions().isNegateImageEnabled());

        // Set the blank page handler
        BlankPageHandlingOptions blank_handler = ac.getBlankPageHandlingOptions();
        handle.DTWAIN_SetBlankPageDetection(sourceHandle, blank_handler.getThreshold(),
                                            blank_handler.getDiscardOption().value(),
                                            blank_handler.getDetectionOption().value(),
                                            blank_handler.isEnabled()?true:false);

        // Set the multisave option
        MultipageSaveOptions multisave_info = ac.getFileTransferOptions().getMultipageSaveOptions();
        handle.DTWAIN_SetMultipageScanMode(sourceHandle, multisave_info.getSaveMode().ordinal());

        // general options
        GeneralOptions gopts = ac.getGeneralOptions();
        handle.DTWAIN_SetMaxAcquisitions(sourceHandle, gopts.getMaxAcquisitions());

        setPDFOptions();
    }

    private void setPDFOptions() throws DTwainJavaAPIException
    {
        PDFOptions po = acquireCharacteristics.getPDFOptions();
        DTwainJavaAPI handle = twainSession.getAPIHandle();
        handle.DTWAIN_SetPDFCreator(sourceHandle, po.getCreator());
        handle.DTWAIN_SetPDFTitle(sourceHandle, po.getTitle());
//      handle.DTWAIN_SetPDFProducer(sourceHandle, po.getProducer());
        handle.DTWAIN_SetPDFAuthor(sourceHandle, po.getAuthor());
        handle.DTWAIN_SetPDFSubject(sourceHandle, po.getSubject());
        handle.DTWAIN_SetPDFKeywords(sourceHandle, po.getKeywords());
        handle.DTWAIN_SetPDFASCIICompression(sourceHandle, po.isASCII85Enabled());
        handle.DTWAIN_SetPDFOrientation(sourceHandle, po.getPDFOrientation().getOrientation().ordinal());

        PDFPaperSizeOptions pagesizeopts = po.getPaperSizeOptions();
        boolean custom_used = pagesizeopts.isCustomSizeEnabled();
        int width = 0;
        int height = 0;
        if (custom_used)
        {
            height = pagesizeopts.getCustomHeight();
            width = pagesizeopts.getCustomWidth();
        }

        handle.DTWAIN_SetPDFPageSize(sourceHandle,
                                     po.getPaperSizeOptions().getPaperSize().ordinal(),
                                     (double)width, (double)height);

        PDFPageScaleOptions pageScale = po.getPDFPageScaleOptions();
        custom_used = pageScale.isCustomScaleEnabled();
        double xscale = 0.0;
        double yscale = 0.0;
        if ( custom_used )
        {
            xscale = pageScale.getCustomScaleX();
            yscale = pageScale.getCustomScaleY();
        }
        handle.DTWAIN_SetPDFPageScale(sourceHandle, pageScale.getScaling().ordinal(), xscale, yscale);

        // Encryption
        PDFEncryption encrypt_opts = po.getPDFEncryption();
        if ( encrypt_opts.isEnabled() )
        {
            handle.DTWAIN_SetPDFEncryption(sourceHandle, true,
                    encrypt_opts.getUserPassword(),
                    encrypt_opts.getOwnerPassword(),
                    encrypt_opts.getPermissionsAsInteger(),
                    encrypt_opts.isStrongEncryptionEnabled());
        }
    }

    private boolean waitForFeeder(PaperHandlingInfo paperInfo) throws DTwainJavaAPIException
    {
        boolean isFeederSupported = paperInfo.isFeederSupported();
        if ( !isFeederSupported )
            return true;
        SetCapOperation op = capabilityInterface.set();
        GetCapOperation getOp = capabilityInterface.getCurrent();

        capabilityInterface.setFeederEnabled(Arrays.asList(true), op);
        List<Boolean> enabled = capabilityInterface.getFeederEnabled(getOp);
        if ( enabled.isEmpty() || !enabled.get(0) )
            return true;

        boolean ispaperdetectable = capabilityInterface.isPaperDetectableSupported();
        if ( !ispaperdetectable )
            return true;

        boolean isfeederloaded = capabilityInterface.isFeederLoadedSupported();
        if (!isfeederloaded )
            return true;

        PaperHandlingOptions paperOpts = acquireCharacteristics.getPaperHandlingOptions();
        int timeoutval = paperOpts.getFeederWaitTime();
        if (timeoutval == PaperHandlingOptions.noFeederWaitTime)
            return true;
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        try
        {
            while (!capabilityInterface.getFeederLoaded(getOp).get(0))
            {
                if ( timeoutval != PaperHandlingOptions.waitInfinite)
                {
                    if (stopwatch.getTime() / 1000 > timeoutval)
                        return false;
                }
                TimeUnit.MILLISECONDS.sleep(1);
            }
        }
        catch(InterruptedException e)
        {
            throw new DTwainJavaAPIException(e.getMessage());
        }
        return true;
    }

    public AcquireReturnInfo acquire() throws DTwainJavaAPIException 
    {
        isValidSource();
        boolean bFstatus = true;
        DTwainJavaAPI handle = twainSession.getAPIHandle();
        prepareAcquisitions();
        PaperHandlingInfo paperInfo = new PaperHandlingInfo();
        paperInfo.getInfo(this);
        boolean isFeederOnly = paperInfo.isFeederOnly();
        if ( !isFeederOnly && !this.acquireCharacteristics.getPaperHandlingOptions().isFeederEnabled())
            handle.DTWAIN_EnableFeeder(sourceHandle, false);
        else
        {
            PaperHandlingOptions feederOptions = this.acquireCharacteristics.getPaperHandlingOptions();
            if ( feederOptions.isAutoFeedNotifyEnabled() )
                handle.DTWAIN_EnableAutoFeedNotify(1000, true);
            FeederMode fmode = feederOptions.getFeederMode();
            boolean use_feeder_or_flatbed = (fmode == FeederMode.FEEDER_FLATBED);
            boolean use_wait = (feederOptions.getFeederWaitTime() != 0);
            if ( use_wait || use_feeder_or_flatbed )
            {
                bFstatus = waitForFeeder(paperInfo);
                if ( !bFstatus )
                    return new AcquireReturnInfo(ErrorCode.ERROR_NONE, null);
            }
            handle.DTWAIN_EnableFeeder(sourceHandle, false);
            bFstatus = true;
        }

        if ( bFstatus )
        {
            AcquireType transtype = acquireCharacteristics.getGeneralOptions().getAcquireType();
            if ( transtype == AcquireType.NATIVEFILE ||
                 transtype == AcquireType.BUFFEREDFILE ||
                 transtype == AcquireType.DEVICEFILE )
                return acquireToFile(transtype);
            return acquireToImageHandles(transtype);
        }
        return new AcquireReturnInfo(ErrorCode.ERROR_NONE, null);
    }

    private AcquireReturnInfo acquireToFile(AcquireType transtype) throws DTwainJavaAPIException
    {
        DTwainJavaAPI handle = twainSession.getAPIHandle();
        AcquireCharacteristics ac = acquireCharacteristics;
        FileTransferOptions ftOptions = ac.getFileTransferOptions();
        int dtwain_transfer_type = AcquireFileOptions.USENATIVE.value();
        if ( transtype == AcquireType.BUFFEREDFILE)
            dtwain_transfer_type = AcquireFileOptions.USEBUFFERED.value();
        FileTransferFlags ftFlags = ftOptions.getTransferFlags();
        dtwain_transfer_type += ftFlags.isUseName()?DTwainConstants.DTWAIN_USELONGNAME:DTwainConstants.DTWAIN_USEPROMPT;
        dtwain_transfer_type += ftFlags.isAutoCreateDirectory()?DTwainConstants.DTWAIN_CREATEDIRECTORY:0;
        FileType ft = ftOptions.getType();
        if ( !FileTypeInfo.isUniversalSupport(ft))
        {
            dtwain_transfer_type += AcquireFileOptions.USESOURCEMODE.value();
            handle.DTWAIN_SetCompressionType(sourceHandle, ft.value(), true);
        }

        FilenameIncrementOptions inc = ftOptions.getFilenameIncrementOptions();
        handle.DTWAIN_SetFileAutoIncrement(sourceHandle, inc.getIncrementValue(), inc.isResetCount(), inc.isEnabled());

        int fileTypeAsInt = ft.value();
        if (ftOptions.canMultiPage())
            fileTypeAsInt = FileTypeInfo.getMultipageType(ft).value();

        GeneralOptions gopts = ac.getGeneralOptions();
        PixelType curPixelType = gopts.getPixelType();

        UserInterfaceOptions ui = ac.getUserInterfaceOptions();

        int retval = handle.DTWAIN_AcquireFile(
                sourceHandle,
                ftOptions.getName(),
                fileTypeAsInt,
                dtwain_transfer_type,
                curPixelType.value(),
                gopts.getMaxPageCount(),
                ui.isShowUI(),
                gopts.getSourceStateAfterAcquire() == SourceStateAfterAcquire.CLOSED?true:false);
        if ( retval == -1 )
            return new AcquireReturnInfo(ErrorCode.from(handle.DTWAIN_GetLastError()), null);
        return new AcquireReturnInfo( ErrorCode.ERROR_NONE, null );
    }

    private AcquireReturnInfo acquireToImageHandles(AcquireType transtype) throws DTwainJavaAPIException
    {
        DTwainJavaAPI handle = twainSession.getAPIHandle();
        AcquireCharacteristics ac = acquireCharacteristics;
        GeneralOptions gOpts = ac.getGeneralOptions();
        CapabilityInterface ci = this.capabilityInterface;
        List<Integer> curPixelTypes = this.capabilityInterface.getPixelType(ci.getCurrent());
        if ( curPixelTypes.isEmpty() )
            return new AcquireReturnInfo(ErrorCode.from(handle.DTWAIN_GetLastError()), null);
        int ct = curPixelTypes.get(0);
        TwainAcquisitionArray acqArray = null;
        UserInterfaceOptions ui = ac.getUserInterfaceOptions();

        if ( transtype == AcquireType.NATIVE || transtype == AcquireType.BUFFERED)
        {
            if ( transtype == AcquireType.NATIVE )
            {
                acqArray = handle.DTWAIN_AcquireNative(sourceHandle,
                        ct,
                        gOpts.getMaxPageCount(),
                        ui.isShowUI(),
                        gOpts.getSourceStateAfterAcquire() == SourceStateAfterAcquire.CLOSED?true:false);
            }
            else
            {
                int nStripSize = bufferedTransferInfo.getStripSize();
                if ( bufferedTransferInfo.isHandleStrips() )
                {
                    OptionsApplyer.apply(this, ac.getCompressionSupportOptions());
                    bufferedTransferInfo.attach(this);
                    List<Integer> currentCompression = ci.getCompression(ci.getCurrent());
                    bufferedTransferInfo.setStripSize(nStripSize);
                    bufferedTransferInfo.initTransfer(CompressionType.values()[currentCompression.get(0)]);
                }
                else
                {
                    bufferedTransferInfo.setStripSize(nStripSize);
                    bufferedTransferInfo.initTransfer(CompressionType.NONE);
                }
                acqArray = handle.DTWAIN_AcquireBuffered(sourceHandle,
                        ct,
                        gOpts.getMaxPageCount(),
                        ui.isShowUI(),
                        gOpts.getSourceStateAfterAcquire() == SourceStateAfterAcquire.CLOSED?true:false);
            }

            int last_error = this.twainSession.getLastError();
            if ( acqArray != null || last_error != DTwainConstants.ErrorCode.ERROR_NONE.value())
            {
                return new AcquireReturnInfo(ErrorCode.ERROR_NONE, new ImageHandler(acqArray));
            }
            else
                return new AcquireReturnInfo(ErrorCode.from(last_error), null);
        }
        return new AcquireReturnInfo(ErrorCode.ERROR_ACQUIRECANCELLED, null);
    }

    // Override this for custom setup before options are applied
    public boolean preAcquireSetup()
    {
        return true;
    }

    // Override this for custom setup after options are applied
    public boolean postAcquireSetup()
    {
        return true;
    }

    public boolean close() throws DTwainJavaAPIException
    {
        if ( sourceHandle != 0 && twainSession != null && isOpened)
        {
            DTwainJavaAPI handle = twainSession.getAPIHandle();
            int retVal = handle.DTWAIN_CloseSource(sourceHandle);
            if ( retVal != DTwainConstants.ErrorCode.ERROR_NONE.value() )
                return false;
            TwainSession.removeTwainSource(sourceHandle);
            sourceHandle = 0;
            twainSession = null;
            isOpened = false;
        }
        return true;
    }

    public boolean closeAll() throws DTwainJavaAPIException
    {
        boolean retVal = close();
        isSelected = false;
        return retVal;
    }

    public boolean open() throws DTwainJavaAPIException
    {
        errorReturnCode = ErrorCode.ERROR_NONE;
        if ( sourceHandle != 0 && twainSession != null && !isOpened)
        {
            DTwainJavaAPI handle = twainSession.getAPIHandle();
            int retVal = handle.DTWAIN_OpenSource(sourceHandle);
            if ( retVal != DTwainConstants.ErrorCode.ERROR_NONE.value())
            {

                errorReturnCode = ErrorCode.from(retVal);
                return false;
            }
            this.reloadInfo(true);
        }
        return true;
    }

    public ErrorCode getLastError()
    {
        return errorReturnCode;
    }

    public TwainSource setLastError(ErrorCode error)
    {
        this.errorReturnCode = error;
        return this;
    }

    public TwainSource setLastError(int error)
    {
        this.errorReturnCode = ErrorCode.from(error);
        return this;
    }

    public boolean isClosed()
    {
        return sourceHandle == 0;
    }

    public boolean isOpened()
    {
        return isOpened;
    }

    public boolean isCreated()
    {
        return isCreated;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public BufferedStripInfo getBufferedStripInfo() throws DTwainJavaAPIException
    {
        if ( sourceHandle != 0 && twainSession != null && isOpened)
        {
            DTwainJavaAPI handle = twainSession.getAPIHandle();
            BufferedStripInfo stripInfo = getBufferedTransferInfo().getStripInfo();
            handle.DTWAIN_GetBufferedStripData(sourceHandle, stripInfo);
            return stripInfo;
        }
        return new BufferedStripInfo();
    }

    public void writePDFTextElement(PDFTextElement textElement) throws DTwainJavaAPIException
    {
        if ( sourceHandle != 0 && twainSession != null && isOpened)
        {
            DTwainJavaAPI handle = twainSession.getAPIHandle();
            handle.DTWAIN_AddPDFText(sourceHandle, textElement);
        }
    }

    // If FileTransferOptions.MultipageSaveOptions has set the multipage save mode, this
    // call will save the acquired pages to the multipage file at any time.
    // See https://www.dynarithmic.com/onlinehelp/dtwain/newversion/DTWAIN_SetMultipageScanMode.html
    public void flushAcquiredPages() throws DTwainJavaAPIException
    {
        if ( sourceHandle != 0 && twainSession != null && isOpened)
        {
            DTwainJavaAPI handle = twainSession.getAPIHandle();
            handle.DTWAIN_FlushAcquiredPages(sourceHandle);
        }
    }

    public void setTiffCompressType(FileType tiffFileType) throws DTwainJavaAPIException
    {
        if ( sourceHandle != 0 && twainSession != null && isOpened)
        {
            DTwainJavaAPI handle = twainSession.getAPIHandle();
            int retValue = handle.DTWAIN_SetTIFFCompressType(sourceHandle, tiffFileType.value());
            if ( retValue != 1 )
            {
                String err = twainSession.getErrorString(twainSession.getLastError());
                throw new DTwainJavaAPIException(err);
            }
        }
    }

    public TwainImageInfo getAcquiredImageInfo() throws DTwainJavaAPIException
    {
        if ( sourceHandle != 0 && twainSession != null && isOpened)
        {
            DTwainJavaAPI handle = twainSession.getAPIHandle();
            TwainImageInfo iInfo = handle.DTWAIN_GetImageInfo(sourceHandle);
            return iInfo;
        }
        return new TwainImageInfo();
    }
    
    public TW_IDENTITY getSourceID() throws DTwainJavaAPIException
    {
        if ( sourceHandle != 0 && twainSession != null && isOpened)
            return twainSession.getAPIHandle().DTWAIN_GetSourceID(sourceHandle);
        throw new DTwainJavaAPIException("Invalid source or TWAIN session handle");
    }
}
