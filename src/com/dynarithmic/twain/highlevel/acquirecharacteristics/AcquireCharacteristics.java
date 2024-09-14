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
package com.dynarithmic.twain.highlevel.acquirecharacteristics;

/**
 * @author Dynarithmic Software
 * <p>AcquireCharacteristics class details the set of capabilities and features the device will use when
 * acquiring an image.<p>
 * The AcquireCharacteristics class acts as a device-independent method of specifying what capabilities the device will
 * use when acquiring an image, and only when the call to TwainSource.acquire() will the settings in the
 * AcquireCharacteristics be used to actually set the device's capabilities.<p>
 * For example, the application can set the duplex option (PaperHandlingOptions.enableDuplex()), even if the
 * device cannot perform duplex scanning.  Only when the TwainSource.acquire() is called will the duplexer be
 * attempted to be enabled.  If there isn't a duplex unit, then the "enable" setting is simply ignored.<p>
 * Note the difference between AcquireCharacteristics and CapabilityInterface:  The AcquireCharacteristics makes
 * no calls to set any capabilities of the device, while the CapabilityInterface will directly set or get
 * capabilities of the device.<p>
 *
 *  Most categories in AcquireCharacteristics roughly follow the TWAIN 2.4 Specification Chapter 10.3
 *  "Capabilities in Categories of Functionality".
 *
 */
public class AcquireCharacteristics
{
    private AudibleAlarmsOptions    audibleAlarms        = new AudibleAlarmsOptions();
    private AutoAdjustOptions       autoAdjust           = new AutoAdjustOptions();
    private AutoCaptureOptions      autoCapture          = new AutoCaptureOptions();
    private AutoScanningOptions     autoScanning         = new AutoScanningOptions();
    private BarcodeDetectionOptions barCodeDetection     = new BarcodeDetectionOptions();
    private BufferedTransferOptions bufferedTransferOptions = new BufferedTransferOptions();
    private CapNegotiationOptions   capNegotiation       = new CapNegotiationOptions();
    private BlankPageHandlingOptions blankPageHandling   = new BlankPageHandlingOptions();
    private ColorOptions            colorSupport         = new ColorOptions();
    private CompressionOptions compressionSupport        = new CompressionOptions();
    private DeviceEventOptions deviceEvents              = new DeviceEventOptions();
    private DeviceParamsOptions deviceParams             = new DeviceParamsOptions();
    private DoublefeedOptions doublefeedDetection = new DoublefeedOptions();
    private FileTransferOptions fileTransfer           = new FileTransferOptions();
    private GeneralOptions generalOptions              = new GeneralOptions();
    private ImageInformationOptions  imageInformation  = new ImageInformationOptions();
    private ImageParameterOptions    imageParameter    = new ImageParameterOptions();
    private ImageTypeOptions imageType                 = new ImageTypeOptions();
    private ImprinterOptions imprinterSupport   = new ImprinterOptions();
    private JobControlOptions jobControl        = new JobControlOptions();
    private LanguageOptions languageSupport     = new LanguageOptions();
    private MICROptions MICRSupport             = new MICROptions();
    private PagesOptions pagesSupport           = new PagesOptions();
    private PaperHandlingOptions paperHandling  = new PaperHandlingOptions();
    private PatchcodeDetectionOptions patchcodeDetection = new PatchcodeDetectionOptions();
    private PDFOptions pdfOptions               = new PDFOptions();
    private PowerMonitorOptions powerMonitor    = new PowerMonitorOptions();
    private ResolutionOptions resolutionSupport = new ResolutionOptions();
    private UserInterfaceOptions userInterface  = new UserInterfaceOptions();

    /**
     * @return The AudibleAlarmsOptions (see "Audible Alarms", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public AudibleAlarmsOptions getAudibleAlarmsOptions()
    {
        return this.audibleAlarms;
    }

    /**
     * @return The AutoAdjustOptions (see "Automatic Adjustments", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public AutoAdjustOptions getAutoAdjustOptions()
    {
        return autoAdjust;
    }

    /**
     * @return The AutoCapturetOptions (see "Automatic Capture", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public AutoCaptureOptions getAutoCaptureOptions()
    {
        return autoCapture;
    }

    /**
     * @return The AutoScanningOptions (see "Automatic Scanning", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public AutoScanningOptions getAutoScanning()
    {
        return autoScanning;
    }

    /**
     * @return The BarCodeDetectionOptions (see "Bar Code Detection Search Parameters", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public BarcodeDetectionOptions getBarcodeDetectionOptions()
    {
        return barCodeDetection;
    }

    public BufferedTransferOptions getBufferedTransferOptions()
    {
        return this.bufferedTransferOptions;
    }
    /**
     * @return The CapNegotiationOptions (see "Capability Negotiation Parameters", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public CapNegotiationOptions getCapNegotiationOptions()
    {
        return this.capNegotiation;
    }

    /**
     * @return The ColorSupportOptions (see "Color", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public ColorOptions getColorSupportOptions()
    {
        return colorSupport;
    }


    /**
     * @return The CompressionSupportOptions (see "Compression", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public CompressionOptions getCompressionSupportOptions()
    {
        return compressionSupport;
    }

    /**
     * @return The DeviceEventsOptions.
     */
    public DeviceEventOptions getDeviceEventsOptions()
    {
        return deviceEvents;
    }

    /**
     * @return The DeviceParamsOptions (see "Device Parameters", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public DeviceParamsOptions getDeviceParamsOptions()
    {
        return deviceParams;
    }


    /**
     * @return The DoublefeedDetectionOptions (see "Doublefeed Detection", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public DoublefeedOptions getDoublefeedDetectionOptions()
    {
        return doublefeedDetection;
    }

    /**
     * @return The FileTransferOptions that will be used when acquiring the image
     */
    public FileTransferOptions getFileTransferOptions()
    {
        return fileTransfer;
    }


    /**
     * @return The ImageInformationOptions (see "Image Information", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public ImageInformationOptions getImageInformationOptions()
    {
        return imageInformation;
    }

    /**
     * @return The ImageParameterOptions (see "Image Parameters for Acquire", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public ImageParameterOptions getImageParameterOptions()
    {
        return imageParameter;
    }

    /**
     * @return The ImageTypeOptions (see "Image Type", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public ImageTypeOptions getImageTypeOptions()
    {
        return imageType;
    }

    /**
     * @return The ImprinterSupportOptions (see "Imprinter/Endorser Functionality", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public ImprinterOptions getImprinterSupportOptions()
    {
        return imprinterSupport;
    }

    /**
     * @return The JobControlOptions
     */
    public JobControlOptions getJobControlOptions()
    {
        return jobControl;
    }

    /**
     * @return The LanguageSupportOptions (see "Language Support", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public LanguageOptions getLanguageSupportOptions()
    {
        return languageSupport;
    }


    /**
     * @return The MICRSupportOptions (see "MICR", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public MICROptions getMICRSupportOptions()
    {
        return MICRSupport;
    }

    /**
     * @return The PagesSupportOptions (see "Pages", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public PagesOptions getPagesSupportOptions()
    {
        return pagesSupport;
    }

    /**
     * @return The PaperHandlingOptions (see "Paper Handling", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public PaperHandlingOptions getPaperHandlingOptions()
    {
        return paperHandling;
    }

    /**
     * @return The PatchodeDetectionOptions (see "Patch Code Detection", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public PatchcodeDetectionOptions getPatchcodeDetectionOptions()
    {
        return patchcodeDetection;
    }

    /**
     * @return The PowerMonitorOptions (see "Power Monitoring", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public PowerMonitorOptions getPowerMonitorOptions()
    {
        return powerMonitor;
    }

    /**
     * @return The ResolutionSupportOptions (see "Resolution", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public ResolutionOptions getResolutionSupportOptions()
    {
        return resolutionSupport;
    }

    /**
     * @return The UserInterfaceOptions (see "User Interface", Chapter 10-3 of the TWAIN 2.4 specification)
     */
    public UserInterfaceOptions getUserInterfaceOptions()
    {
        return userInterface;
    }

    /**
     * @return The BlankPageHandling options
     */
    public BlankPageHandlingOptions getBlankPageHandlingOptions()
    {
        return blankPageHandling;
    }

    /**
     * @return The GeneralOptions options.  The GeneralOptions contains basic items (acquire type, number of pages to acquire), etc.
     */
    public GeneralOptions getGeneralOptions()
    {
        return generalOptions;
    }

    /**
     * @return The PDFOptions used when saving acquired images to a PDF file
     */
    public PDFOptions getPDFOptions()
    {
        return this.pdfOptions;
    }
}
