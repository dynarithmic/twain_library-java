package com.dynarithmic.twain.highlevel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import com.dynarithmic.twain.DTwainConstants.NotificationCode;

public class TwainCallback
{
    private boolean activated;
    private boolean logUnhandledEvents;
    private static final ArrayList<TwainCallback> allCallbacks = new ArrayList<>();
    private OutputStream outputStream = System.out;
    private static final TreeMap<NotificationCode, String> s_mapData = new TreeMap<>();
    static
    {
        s_mapData.put(NotificationCode.ACQUIREDONE, "onAcquireDone()");
        s_mapData.put(NotificationCode.ACQUIRESTARTED, "onAcquireStarted()");
        s_mapData.put(NotificationCode.UIOPENED, "onUIOpened()");
        s_mapData.put(NotificationCode.UICLOSING, "onUIClosing()");
        s_mapData.put(NotificationCode.UICLOSED, "onUIClosed()");
        s_mapData.put(NotificationCode.TRANSFERREADY, "onTransferReady()");
        s_mapData.put(NotificationCode.TRANSFERDONE, "onTransferDone()");
        s_mapData.put(NotificationCode.TRANSFERCANCELLED, "onTransferCancelled()");
        s_mapData.put(NotificationCode.ACQUIREFAILED, "onAcquireFailed()");
        s_mapData.put(NotificationCode.ACQUIRECANCELLED, "onAcquireCancelled()");
        s_mapData.put(NotificationCode.PROCESSEDDIB, "onProcessedDib()");
        s_mapData.put(NotificationCode.FILESAVECANCELLED, "onFileSaveCancelled()");
        s_mapData.put(NotificationCode.FILESAVEOK, "onFileSaveOk()");
        s_mapData.put(NotificationCode.FILESAVEERROR, "onFileSaveError()");
        s_mapData.put(NotificationCode.FILEPAGESAVING, "onFilePageSaving()");
        s_mapData.put(NotificationCode.FILEPAGESAVEOK, "onFilePageSaveOk()");
        s_mapData.put(NotificationCode.FILEPAGESAVEERROR, "onFilePageSaveError()");
        s_mapData.put(NotificationCode.ACQUIRETERMINATED, "onAcquireTerminated()");
        s_mapData.put(NotificationCode.PAGECONTINUE, "onPageContinue()");
        s_mapData.put(NotificationCode.PAGECANCELLED, "onPageCancelled()");
        s_mapData.put(NotificationCode.PAGEDISCARDED, "onPageDiscarded()");
        s_mapData.put(NotificationCode.PAGEFAILED, "onPageFailed()");
        s_mapData.put(NotificationCode.QUERYPAGEDISCARD, "onQueryPageDiscard()");
        s_mapData.put(NotificationCode.TRANSFERSTRIPREADY, "onTransferStripReady()");
        s_mapData.put(NotificationCode.TRANSFERSTRIPDONE, "onTransferStripDone()");
        s_mapData.put(NotificationCode.TRANSFERSTRIPFAILED, "onTransferStripFailed()");
        s_mapData.put(NotificationCode.IMAGEINFOERROR, "onImageInfoError()");
        s_mapData.put(NotificationCode.CLIPTRANSFERDONE, "onClipTransferDone()");
        s_mapData.put(NotificationCode.PROCESSEDDIBFINAL, "onProcessedDibFinal()");
        s_mapData.put(NotificationCode.EOJDETECTED, "onEOJDetected()");
        s_mapData.put(NotificationCode.TWAINPAGECANCELLED, "onTwainPageCancelled()");
        s_mapData.put(NotificationCode.TWAINPAGEFAILED, "onTwainPageFailed()");
        s_mapData.put(NotificationCode.EOJDETECTED_XFERDONE, "onEOJDetectedEndTransfer()");
        s_mapData.put(NotificationCode.DEVICEEVENT, "onDeviceEvent()");
        s_mapData.put(NotificationCode.CROPFAILED, "onCropImageFailedEvent()");
        s_mapData.put(NotificationCode.BLANKPAGEDETECTED1, "onBlankPageDetectedOriginalImage()");
        s_mapData.put(NotificationCode.BLANKPAGEDETECTED2, "onBlankPageDetectedAdjustedlImage()");
        s_mapData.put(NotificationCode.BLANKPAGEDETECTED3, "onBlankPageDetectedEvent3()");
        s_mapData.put(NotificationCode.BLANKPAGEDISCARDED1, "onBlankPageDiscardedEvent1()");
        s_mapData.put(NotificationCode.BLANKPAGEDISCARDED2, "onBlankPageDiscardedEvent2()");
    }

    public TwainCallback()
    {
        activated = false;
        logUnhandledEvents = false;
    }

    public void logUnhandledEvents(boolean bLog)
    {
        logUnhandledEvents = bLog;
    }

    public boolean isLogUnhandledEventsOn()
    {
        return logUnhandledEvents;
    }

    public void activate()
    {
        activated = true;
        allCallbacks.add(this);
    }

    public void deactivate()
    {
        activated = false;
    }

    public boolean isActivated()
    {
        return activated;
    }

    public void addCallback()
    {
        if ( !allCallbacks.contains(this))
            allCallbacks.add(this);
    }

    public void removeCallback()
    {
        if ( allCallbacks.contains(this))
            allCallbacks.remove(this);
    }

    public TwainCallback setOutputStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
        return this;
    }

    public OutputStream getOutputStream()
    {
        return this.outputStream;
    }

    public static int onTwainEvent(int event, long sourceHandle)
    {
        // userdata is the handle to the AcquireEngine
        // use map to find the AcquireEngine
        // Once found, write loop to call all callbacks added to the engine
        int nCallback = allCallbacks.size();
        int returner = 1;
        NotificationCode nc = null;
        for (int i = 0; i < nCallback; ++i)
        {
            TwainCallback theCallback = allCallbacks.get(i);
            if (theCallback.activated)
            {
                try
                {
                    nc = NotificationCode.from(event);
                }
                catch (Exception e)
                {
                    theCallback.catchUnknown(event, sourceHandle);
                    continue;
                }
                if ( nc == null )
                {
                    theCallback.catchUnknown(event, sourceHandle);
                    continue;
                }
                switch (nc)
                {
                    case ACQUIREDONE:
                        returner = theCallback.onAcquireDone(sourceHandle);
                        break;

                    case ACQUIRESTARTED:
                        returner = theCallback.onAcquireStarted(sourceHandle);
                        break;

                    case UIOPENED:
                        returner = theCallback.onUIOpened(sourceHandle);
                        break;

                    case UICLOSING:
                        returner = theCallback.onUIClosing(sourceHandle);
                        break;

                    case UICLOSED:
                        returner = theCallback.onUIClosed(sourceHandle);
                        break;

                    case TRANSFERREADY:
                        returner = theCallback.onTransferReady(sourceHandle);
                        break;

                    case TRANSFERDONE:
                        returner = theCallback.onTransferDone(sourceHandle);
                        break;

                    case TRANSFERCANCELLED:
                        returner = theCallback.onTransferCancelled(sourceHandle);
                        break;

                    case ACQUIREFAILED:
                        returner = theCallback.onAcquireFailed(sourceHandle);
                        break;

                    case ACQUIRECANCELLED:
                        returner = theCallback.onAcquireCancelled(sourceHandle);
                        break;

                    case PROCESSEDDIB:
                        returner = theCallback.onProcessedDib(sourceHandle);
                        break;

                    case FILESAVECANCELLED:
                        returner = theCallback.onFileSaveCancelled(sourceHandle);
                        break;

                    case FILESAVEOK:
                        returner = theCallback.onFileSaveOk(sourceHandle);
                        break;

                    case FILESAVEERROR:
                        returner = theCallback.onFileSaveError(sourceHandle);
                        break;

                    case FILEPAGESAVEOK:
                        returner = theCallback.onFilePageSaveOk(sourceHandle);
                        break;

                    case FILEPAGESAVING:
                        returner = theCallback.onFilePageSaving(sourceHandle);
                        break;

                    case FILEPAGESAVEERROR:
                        returner = theCallback.onFilePageSaveError(sourceHandle);
                        break;

                    case ACQUIRETERMINATED:
                        returner = theCallback.onAcquireTerminated(sourceHandle);
                        break;

                    case PAGECONTINUE:
                        returner = theCallback.onPageContinue(sourceHandle);
                        break;

                    case QUERYPAGEDISCARD:
                        returner = theCallback.onQueryPageDiscard(sourceHandle);
                        break;

                    case PAGEDISCARDED:
                        returner = theCallback.onPageDiscarded(sourceHandle);
                        break;

                    case PAGECANCELLED:
                        returner = theCallback.onPageCancelled(sourceHandle);
                        break;

                    case PAGEFAILED:
                        returner = theCallback.onPageFailed(sourceHandle);
                        break;

                    case TRANSFERSTRIPREADY:
                        returner = theCallback.onTransferStripReady(sourceHandle);
                        break;

                    case TRANSFERSTRIPDONE:
                        returner = theCallback.onTransferStripDone(sourceHandle);
                        break;

                    case TRANSFERSTRIPFAILED:
                        returner = theCallback.onTransferStripFailed(sourceHandle);
                        break;

                    case IMAGEINFOERROR:
                        returner = theCallback.onImageInfoError(sourceHandle);
                        break;

                    case CLIPTRANSFERDONE:
                        returner = theCallback.onClipTransferDone(sourceHandle);
                        break;

                    case PROCESSEDDIBFINAL:
                        returner = theCallback.onProcessedDibFinal(sourceHandle);
                        break;

                    case EOJDETECTED:
                        returner = theCallback.onEOJDetected(sourceHandle);
                        break;

                    case EOJDETECTED_XFERDONE:
                        returner = theCallback.onEOJDetectedEndTransfer(sourceHandle);
                        break;

                    case TWAINPAGECANCELLED:
                        returner = theCallback.onTwainPageCancelled(sourceHandle);
                        break;

                    case TWAINPAGEFAILED:
                        returner = theCallback.onTwainPageFailed(sourceHandle);
                        break;

                    case DEVICEEVENT:
                        returner = theCallback.onDeviceEvent(sourceHandle);
                        break;

                    case CROPFAILED:
                        returner = theCallback.onCropImageFailedEvent(sourceHandle);
                        break;

                    case BLANKPAGEDETECTED1:
                        returner = theCallback.onBlankPageDetectedOriginalImage(sourceHandle);
                        break;

                    case BLANKPAGEDETECTED2:
                        returner = theCallback.onBlankPageDetectedAdjustedImage(sourceHandle);
                        break;

                    case BLANKPAGEDETECTED3:
                        returner = theCallback.onBlankPageDetectedEvent3(sourceHandle);
                        break;

                    case BLANKPAGEDISCARDED1:
                        returner = theCallback.onBlankPageDiscardedEvent1(sourceHandle);
                        break;

                    case BLANKPAGEDISCARDED2:
                        returner = theCallback.onBlankPageDiscardedEvent2(sourceHandle);
                        break;

                    case OCRTEXTRETRIEVED:
                        returner = theCallback.onOCRTextRetrieved(sourceHandle);
                        break;

                    case QUERYOCRTEXT:
                        returner = theCallback.onQueryOCRText(sourceHandle);
                        break;

                    case PDFOCRREADY:
                        returner = theCallback.onPDFOCRReady(sourceHandle);
                        break;

                    case PDFOCRDONE:
                        returner = theCallback.onPDFOCRDone(sourceHandle);
                        break;

                    case PDFOCRERROR:
                        returner = theCallback.onPDFOCRError(sourceHandle);
                        break;

                    case SETCALLBACKINIT:
                        returner = theCallback.onSetCallBackInit(sourceHandle);
                        break;

                    case SETCALLBACK64INIT:
                        returner = theCallback.onSetCallBack64Init(sourceHandle);
                        break;

                    case FILENAMECHANGING:
                        returner = theCallback.onFileNameChangint(sourceHandle);
                        break;

                    case FILENAMECHANGED:
                        returner = theCallback.onFileNameChanged(sourceHandle);
                        break;

                    case PROCESSEDAUDIOFINAL:
                        returner = theCallback.onProcessedAudioFinal(sourceHandle);
                        break;

                    case PROCESSAUDIOFINALACCEPTED:
                        returner = theCallback.onProcessAudioFinalAccepted(sourceHandle);
                        break;

                    case PROCESSEDAUDIOFILE:
                        returner = theCallback.onProcessedAudioFile(sourceHandle);
                        break;

                    case APPUPDATEDDIB:
                        returner = theCallback.onAppUpdatedDIB(sourceHandle);
                        break;

                    case ENDOFJOBDETECTED:
                        returner = theCallback.onEndOfJobDetected(sourceHandle);
                        break;

                    case EOJBEGINFILESAVE:
                        returner = theCallback.onEOJBeginFileSave(sourceHandle);
                        break;

                    case EOJENDFILESAVE:
                        returner = theCallback.onEOJEndFileSave(sourceHandle);
                        break;

                    case FEEDERLOADED:
                        returner = theCallback.onFeederLoaded(sourceHandle);
                        break;

                    case FEEDERNOTLOADED:
                        returner = theCallback.onFeederNotLoaded(sourceHandle);
                        break;

                    case GENERALERROR:
                        returner = theCallback.onGeneralError(sourceHandle);
                        break;
                    case INVALIDIMAGEFORMAT:
                        returner = theCallback.onInvalidImageFormat(sourceHandle);
                        break;

                    case MANDUPACQUIREDONE:
                        returner = theCallback.onManDupAcquireDone(sourceHandle);
                        break;

                    case MANDUPFILEERROR:
                        returner = theCallback.onManDupFileError(sourceHandle);
                        break;

                    case MANDUPFILESAVEERROR:
                        returner = theCallback.onManDupFileSaveError(sourceHandle);
                        break;
                    case MANDUPFLIPPAGES:
                        returner = theCallback.onManDupFlipPages(sourceHandle);
                        break;

                    case MANDUPMEMORYERROR:
                        returner = theCallback.onManDupMemoryError(sourceHandle);
                        break;

                    case MANDUPMERGEERROR:
                        returner = theCallback.onManDupMergeError(sourceHandle);
                        break;
                    case MANDUPPAGECOUNTERROR:
                        returner = theCallback.onManDupPageCountError(sourceHandle);
                        break;
                    case MANDUPSIDE1DONE:
                        returner = theCallback.onManDupSide1Done(sourceHandle);
                        break;
                    case MANDUPSIDE1START:
                        returner = theCallback.onManDupSide1Start(sourceHandle);
                        break;
                    case MANDUPSIDE2DONE:
                        returner = theCallback.onManDupSide2Done(sourceHandle);
                        break;
                    case MANDUPSIDE2START:
                        returner = theCallback.onManDupSide2Start(sourceHandle);
                        break;
                    case PROCESSDIBACCEPTED:
                        returner = theCallback.onProcessDIBAccepted(sourceHandle);
                        break;
                    case PROCESSDIBFINALACCEPTED:
                        returner = theCallback.onProcessDIBFinalAccepted(sourceHandle);
                        break;
                    case UIOPENFAILURE:
                        returner = theCallback.onUIOpenFailure(sourceHandle);
                        break;
                    case UIOPENING:
                        returner = theCallback.onUIOpening(sourceHandle);
                        break;
                    case TWAINTRIPLETBEGIN:
                        returner = theCallback.onTwainTripletBegin(sourceHandle);
                        break;
                    case TWAINTRIPLETEND:
                        returner = theCallback.onTwainTripletEnd(sourceHandle);
                        break;
                    default:
                        break;
                }
            }
        }
        return returner;
    }


    private int defaultImpl(long sourceHandle)
    {
        if (logUnhandledEvents)
        {
            String output = "Source handle: " + sourceHandle + "\n";
            try
            {
                outputStream.write(output.getBytes());
            }
            catch (IOException e)
            {
                return 1;
            }
        }
        return 1;
    }

    private int defaultImpl(int event, long sourceHandle)
    {
        if (logUnhandledEvents)
        {
            String output = "Unknown event code " + event + ".  Source handle: " + sourceHandle + "\n";
            try
            {
                outputStream.write(output.getBytes());
            }
            catch (IOException e)
            {
                return 1;
            }
        }
        return 1;
    }

    public int onAcquireStarted(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onAcquireDone(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onUIOpened(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onUIClosing(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onUIClosed(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTransferReady(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTransferDone(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTransferCancelled(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onInvalidImageFormat(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onAcquireFailed(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onAcquireCancelled(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onProcessedDib(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFileSaveCancelled(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFileSaveOk(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFileSaveError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFilePageSaveOk(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFilePageSaveError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onAcquireTerminated(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onPageContinue(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onQueryPageDiscard(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFilePageSaving(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onPageCancelled(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onPageFailed(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onPageDiscarded(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTransferStripReady(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTransferStripDone(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTransferStripFailed(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onImageInfoError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onClipTransferDone(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onProcessedDibFinal(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onEOJDetected(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onEOJDetectedEndTransfer(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTwainPageCancelled(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTwainPageFailed(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onDeviceEvent(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onCropImageFailedEvent(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onBlankPageDetectedOriginalImage(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onBlankPageDetectedAdjustedImage(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onBlankPageDetectedEvent3(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onBlankPageDiscardedEvent1(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onBlankPageDiscardedEvent2(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onOCRTextRetrieved(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onQueryOCRText(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onPDFOCRReady(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onPDFOCRDone(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onPDFOCRError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onSetCallBackInit(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onSetCallBack64Init(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFileNameChangint(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFileNameChanged(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onProcessedAudioFinal(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onProcessAudioFinalAccepted(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onProcessedAudioFile(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onAppUpdatedDIB(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onEndOfJobDetected(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onEOJBeginFileSave(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onEOJEndFileSave(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFeederLoaded(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onGeneralError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupAcquireDone(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupFileError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupFileSaveError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupFlipPages(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupMemoryError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupMergeError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupPageCountError(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupSide1Done(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupSide1Start(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupSide2Done(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onManDupSide2Start(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onProcessDIBAccepted(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onProcessDIBFinalAccepted(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onUIOpenFailure(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onUIOpening(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTwainTripletBegin(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onTwainTripletEnd(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int onFeederNotLoaded(long sourceHandle)
    {
        return defaultImpl(sourceHandle);
    }

    public int catchUnknown(int event, long sourceHandle)
    {
        return defaultImpl(event, sourceHandle);
    }
}
