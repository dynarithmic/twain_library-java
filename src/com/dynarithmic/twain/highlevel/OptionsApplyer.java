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
package com.dynarithmic.twain.highlevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AcquireCharacteristics;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AudibleAlarmsOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AutoAdjustOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AutoCaptureOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.AutoScanningOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.BarcodeDetectionOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.CapNegotiationOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.ColorOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.CompressionOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.DeviceEventOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.DeviceParamsOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.DoublefeedOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.ImageInformationOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.ImageParameterOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.ImageTypeOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.ImprinterOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.LanguageOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.MICROptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PagesOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PaperHandlingOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PatchcodeDetectionOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.PowerMonitorOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.ResolutionOptions;
import com.dynarithmic.twain.highlevel.acquirecharacteristics.UserInterfaceOptions;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;
import com.dynarithmic.twain.lowlevel.TW_FRAME;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class OptionsApplyer
{
    public enum OptionsApplyerFunction
    {
        LANGUAGESUPPORTOPTIONS,
        DEVICEPARAMSOPTIONS,
        PAGESSUPPORTOPTIONS,
        POWERMONITOROPTIONS,
        DOUBLEFEEDDETECTIONOPTIONS,
        AUTOADJUSTOPTIONS,
        BARCODEDETECTIONOPTIONS,
        PATCHCODEDETECTIONOPTIONS,
        AUTOCAPTUREOPTIONS,
        IMAGETYPEOPTIONS,
        IMAGEINFORMATIONOPTIONS,
        USERINTERFACEOPTIONS,
        IMAGEPARAMETEROPTIONS,
        AUDIBLEALARMSOPTIONS,
        DEVICEEVENTSOPTIONS,
        RESOLUTIONOPTIONS,
        PAPERHANDLINGOPTIONS,
        COLORSUPPORTOPTIONS,
        CAPNEGOTIATIONOPTIONS,
        MICRSUPPORTOPTIONS,
        IMPRINTERSUPPORTOPTIONS,
        AUTOSCANNINGOPTIONS
    }

    public class OptionsApplyerInfo
    {
        public OptionsApplyerFunction applyerFunction;
        public int priority;
        public boolean enabled;
        public OptionsApplyerInfo(OptionsApplyerFunction func, int priority, boolean enabled)
        {
            applyerFunction = func;
            this.priority = priority;
            this.enabled = enabled;
        }
    }
    
    public List<OptionsApplyerInfo> applyerInfoList = new ArrayList<>();
    
    public OptionsApplyer()
    {
        resetAllOptions();
    }
    
    public List<OptionsApplyerInfo> getOptionsApplyerList() 
    {
        return new ArrayList<>(applyerInfoList);        
    }
    
    public void setOptionPriority(OptionsApplyerFunction func, int priority, boolean enable) throws IllegalArgumentException
    {
        if ( priority < -1 || priority >= OptionsApplyerFunction.values().length)
            throw new IllegalArgumentException();
        
        // Find the function
        Iterator<OptionsApplyerInfo> myItr = this.applyerInfoList.iterator();

        while (myItr.hasNext()) 
        {
            OptionsApplyerInfo curInfo = myItr.next();
            if (curInfo.applyerFunction == func)
            {
                int priorityToUse = curInfo.priority;
                if ( priority != -1 )
                    priorityToUse = priority;
                else
                {
                    curInfo.enabled = enable;
                    break;
                }
                OptionsApplyerInfo copyInfo = new OptionsApplyerInfo(curInfo.applyerFunction, 
                        priorityToUse, enable);
                this.applyerInfoList.remove(curInfo);
                this.applyerInfoList.add(priority, copyInfo);
                break;
            }
        }        
    }

    public OptionsApplyerInfo getOptionApplyerInfo(OptionsApplyerFunction func)
    {
        // Find the function
        Iterator<OptionsApplyerInfo> myItr = this.applyerInfoList.iterator();

        while (myItr.hasNext()) 
        {
            OptionsApplyerInfo curInfo = myItr.next();
            if (curInfo.applyerFunction == func)
                return new OptionsApplyerInfo(curInfo.applyerFunction, curInfo.priority, curInfo.enabled);
        }
        return null;
    }
    
    public void enableAllOptions(boolean enable)
    {
        Iterator<OptionsApplyerInfo> myItr = this.applyerInfoList.iterator();

        while (myItr.hasNext()) 
        {
            OptionsApplyerInfo curInfo = myItr.next();
            curInfo.enabled = enable;
        }
    }
    
    public void resetAllOptions()
    {
        int curPriority = 0;
        for (OptionsApplyerFunction value : OptionsApplyerFunction.values()) 
        { 
            applyerInfoList.add(new OptionsApplyerInfo(value, curPriority, true));
            ++curPriority;
        }
    }
    
    public static void applyAll(TwainSource source) throws DTwainJavaAPIException
    {
        List<OptionsApplyerInfo> optionsApplyerList = 
                source.getOptionsApplyer().getOptionsApplyerList();
        Iterator<OptionsApplyerInfo> myItr = optionsApplyerList.iterator();
        AcquireCharacteristics ac = source.getAcquireCharacteristics();
        while (myItr.hasNext()) 
        {
            OptionsApplyerInfo curInfo = myItr.next();
            if ( !curInfo.enabled )
                continue;
            switch(curInfo.applyerFunction)
            {
                case LANGUAGESUPPORTOPTIONS:
                    apply(source, ac.getLanguageSupportOptions());
                break;
                case DEVICEPARAMSOPTIONS:
                    apply(source, ac.getDeviceParamsOptions());
                break;
                case PAGESSUPPORTOPTIONS:
                    apply(source, ac.getPagesSupportOptions());
                break;
                case POWERMONITOROPTIONS:
                    apply(source, ac.getPowerMonitorOptions());
                break;
                case DOUBLEFEEDDETECTIONOPTIONS:
                    apply(source, ac.getDoublefeedDetectionOptions());
                break;
                case AUTOADJUSTOPTIONS:
                    apply(source, ac.getAutoAdjustOptions());
                break;
                case BARCODEDETECTIONOPTIONS:
                    apply(source, ac.getBarcodeDetectionOptions());
                break;
                case PATCHCODEDETECTIONOPTIONS:
                    apply(source, ac.getPatchcodeDetectionOptions());
                break;
                case AUTOCAPTUREOPTIONS:
                    apply(source, ac.getAutoCaptureOptions());
                break;
                case IMAGETYPEOPTIONS:
                    apply(source, ac.getImageTypeOptions());
                break;
                case IMAGEINFORMATIONOPTIONS:
                    apply(source, ac.getImageInformationOptions());
                break;
                case USERINTERFACEOPTIONS:
                    apply(source, ac.getUserInterfaceOptions());
                break;
                case IMAGEPARAMETEROPTIONS:
                    apply(source, ac.getImageParameterOptions());
                break;
                case AUDIBLEALARMSOPTIONS:
                    apply(source, ac.getAudibleAlarmsOptions());
                break;
                case DEVICEEVENTSOPTIONS:
                    apply(source, ac.getDeviceEventsOptions());
                break;
                case RESOLUTIONOPTIONS:
                    apply(source, ac.getResolutionSupportOptions());
                break;
                case PAPERHANDLINGOPTIONS:
                    apply(source, ac.getPaperHandlingOptions());
                break;
                case COLORSUPPORTOPTIONS:
                    apply(source, ac.getColorSupportOptions());
                break;
                case CAPNEGOTIATIONOPTIONS:
                    apply(source, ac.getCapNegotiationOptions());
                break;
                case MICRSUPPORTOPTIONS:
                    apply(source, ac.getMICRSupportOptions());
                break;
                case IMPRINTERSUPPORTOPTIONS:
                    apply(source, ac.getImprinterSupportOptions());
                break;
                case AUTOSCANNINGOPTIONS:
                    apply(source, ac.getAutoScanningOptions());
                break;
            }
        }
    }

    public static void apply(TwainSource source, PagesOptions po ) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Integer> intList = Arrays.asList(0);
        CAP_SEGMENTED segmentedValue = po.getSegmentedValue();
        if ( segmentedValue != PagesOptions.defaultSegmentedValue)
        {
            intList.set(0, segmentedValue.ordinal());
            ci.setSegmented(intList, op);
        }
        else
            ci.setSegmented(null, op);

        int maxFrames = po.getMaxFrames();
        if ( maxFrames != PagesOptions.defaultMaxFrames)
        {
            intList.set(0, maxFrames);
            ci.setMaxFrames(intList, op);
        }
        else
            ci.setMaxFrames(null, op);

        TW_FRAME frame = po.getFrame();
        if ( frame.isFrameValid() )
        {
            TwainFrameDouble twFrame = new TwainFrameDouble(
                    frame.getLeft().getValue(),
                    frame.getTop().getValue(),
                    frame.getRight().getValue(),
                    frame.getBottom().getValue());
            ci.setFrames(Arrays.asList(twFrame), op);
        }
        ICAP_SUPPORTEDSIZES pageSize = po.getPageSize();
        if ( pageSize != PagesOptions.defaultPageSize)
        {
            intList.set(0, pageSize.ordinal());
            ci.setSupportedSizes(intList, op);
        }
        else
            ci.setSupportedSizes(null, op);
    }

    public static void apply(TwainSource source, LanguageOptions lo) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        if ( lo.getLanguage() != LanguageOptions.defaultLanguage)
            ci.setLanguage(Arrays.asList(lo.getLanguage().ordinal()), op);
        else
            ci.setLanguage(null, op);
    }

    public static void apply(TwainSource source, DeviceParamsOptions dp) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Integer> singleList = Arrays.asList(0);
        ICAP_UNITS unit = dp.getUnit();
        if ( unit != ICAP_UNITS.TWUN_DEFAULT )
        {
            singleList.set(0, unit.ordinal());
            ci.setUnits(singleList, op);
        }
        else
            ci.setUnits(null, op);

        Double exposureTime = dp.getExposureTime();
        if ( exposureTime != DeviceParamsOptions.defaultExposureTime )
            ci.setExposureTime(Arrays.asList(exposureTime), op);
        else
            ci.setExposureTime(null, op);

        ICAP_FLASHUSED2 flashUsed = dp.getFlashUsed();
        if ( flashUsed != DeviceParamsOptions.defaultFlashUsed)
        {
            singleList.set(0, flashUsed.ordinal());
            ci.setFlashUsed2(singleList, op);
        }
        else
            ci.setFlashUsed2(null, op);

        ICAP_IMAGEFILTER imageFilter = dp.getImageFilter();
        if (imageFilter != DeviceParamsOptions.defaultImageFilter )
        {
            singleList.set(0, imageFilter.ordinal());
            ci.setImageFilter(singleList, op);
        }
        else
            ci.setImageFilter(null, op);

        ICAP_LIGHTPATH lightPath = dp.getLightPath();
        if ( lightPath != DeviceParamsOptions.defaultLightPath )
        {
            singleList.set(0, lightPath.ordinal());
            ci.setLightPath(singleList, op);
        }
        else
            ci.setLightPath(null, op);

        ICAP_FILMTYPE filmType = dp.getFilmType();
        if ( filmType != DeviceParamsOptions.defaultFilmType)
        {
            singleList.set(0, filmType.ordinal());
            ci.setFilmType(singleList, op);
        }
        else
            ci.setFilmType(null, op);

        ICAP_LIGHTSOURCE lightSource = dp.getLightSource();
        if ( lightSource != DeviceParamsOptions.defaultLightSource)
        {
            singleList.set(0, lightSource.ordinal());
            ci.setLightSource(singleList, op);
        }
        else
            ci.setLightSource(null, op);

        ICAP_NOISEFILTER noiseFilter = dp.getNoiseFilter();
        if ( noiseFilter != DeviceParamsOptions.defaultNoiseFilter)
        {
            singleList.set(0, noiseFilter.ordinal());
            ci.setNoiseFilter(singleList, op);
        }
        else
            ci.setNoiseFilter(null, op);

        ICAP_OVERSCAN overscan = dp.getOverscan();
        if ( overscan != DeviceParamsOptions.defaultOverscan)
        {
            singleList.set(0, overscan.ordinal());
            ci.setOverscan(singleList, op);
        }
        else
            ci.setOverscan(null, op);

        int zoomFactor = dp.getZoomFactor();
        if ( zoomFactor != DeviceParamsOptions.defaultZoomFactor)
        {
            singleList.set(0, zoomFactor);
            ci.setZoomFactor(singleList, op);
        }
        else
            ci.setZoomFactor(null, op);
    }

    public static void apply(TwainSource source, PowerMonitorOptions po) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        int powerSaveTime = po.getPowerSaveTime();
        if (powerSaveTime != PowerMonitorOptions.defaultPowerSaveTime)
            ci.setPowerSaveTime(Arrays.asList(powerSaveTime), op);
        else
            ci.setPowerSaveTime(null, op);
    }

    public static void apply(TwainSource source, DoublefeedOptions df) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Integer> singleList = Arrays.asList(0);

        CAP_DOUBLEFEEDDETECTION detection = df.getType();
        if (detection != DoublefeedOptions.defaultDetectionType)
        {
            singleList.set(0,  detection.ordinal());
            ci.setDoublefeedDetection(singleList, op);
        }
        else
            ci.setDoublefeedDetection(null, op);

        CAP_DOUBLEFEEDDETECTIONSENSITIVITY sensitivity = df.getSensitivity();
        if ( sensitivity != DoublefeedOptions.defaultSensitivity)
        {
            singleList.set(0, sensitivity.ordinal());
            ci.setDoublefeedDetectionSensitivity(singleList, op);
        }
        else
            ci.setDoublefeedDetectionSensitivity(null, op);

        double detectLength = df.getLength();
        if (detectLength != DoublefeedOptions.defaultDetectionLength)
            ci.setDoublefeedDetectionLength(Arrays.asList(detectLength), op);
        else
            ci.setDoublefeedDetectionLength(null, op);

        List<Integer> responseList = df.getResponsesAsInt();
        if ( !responseList.isEmpty())
            ci.setDoublefeedDetectionResponse(responseList, op);
        else
            ci.setDoublefeedDetectionResponse(null, op);
    }

    public static void apply(TwainSource source, AutoAdjustOptions ao) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Boolean> singleList = Arrays.asList(false);

        singleList.set(0, ao.isSenseMediumEnabled());
        ci.setAutomaticSenseMedium(singleList, op);

        singleList.set(0, ao.isBorderDetectionEnabled());
        ci.setAutomaticBorderDetection(singleList, op);

        singleList.set(0, ao.isColorEnabled());
        ci.setAutomaticColorEnabled(singleList, op);

        singleList.set(0, ao.isDeskewEnabled());
        ci.setAutomaticDeskew(singleList, op);

        singleList.set(0, ao.isLengthDetectionEnabled());
        ci.setAutomaticLengthDetection(singleList, op);

        singleList.set(0, ao.isRotateEnabled());
        ci.setAutomaticRotate(singleList, op);

        List<Integer> singleListInt = Arrays.asList(0);
        singleListInt.set(0,  ao.getDiscardBlankPages());
        ci.setAutoDiscardBlankPages(singleListInt, op);

        ICAP_PIXELTYPE pixType = ao.getColorNonColorPixelType();
        if (pixType != AutoAdjustOptions.defaultcolorNonColorPixelType )
        {
            singleListInt.set(0,  pixType.ordinal());
            ci.setAutomaticColorNonColorPixelType(singleListInt, op);
        }
        else
            ci.setAutomaticColorNonColorPixelType(null, op);

        ICAP_AUTOSIZE autoSize = ao.getAutoSize();
        singleListInt.set(0, autoSize.ordinal());
        ci.setAutoSize(singleListInt, op);
        
        singleListInt.set(0, ao.getFlipRotation().ordinal());
        ci.setFlipRotation(singleListInt, op);
        
        singleListInt.set(0, ao.getImageMerge().ordinal());
        ci.setImageMerge(singleListInt, op);
        
        List<Double> singleListDouble = Arrays.asList(0.0);
        singleListDouble.set(0, ao.getMergeHeightThreshold());
        ci.setImageMergeHeightThreshold(singleListDouble, op);
        
    }

    public static void apply(TwainSource source, BarcodeDetectionOptions bo) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        boolean detectionEnabled = bo.isEnabled();
        ci.setBarcodeDetectionEnabled(Arrays.asList(detectionEnabled), op);
        List<Integer> singleList = Arrays.asList(0);
        if ( detectionEnabled )
        {
            int maxRetries = bo.getMaxRetries();
            if ( maxRetries != BarcodeDetectionOptions.defaultMaxRetries)
            {
                singleList.set(0, maxRetries);
                ci.setBarcodeMaxRetries(singleList, op);
            }
            else
                ci.setBarcodeMaxRetries(null, op);
            int searchPriorities = bo.getMaxSearchPriorities();
            if ( searchPriorities != BarcodeDetectionOptions.defaultMaxSearchPriorities)
            {
                singleList.set(0, searchPriorities);
                ci.setBarcodeMaxSearchPriorities(singleList, op);
            }
            else
                ci.setBarcodeMaxSearchPriorities(null, op);

            ICAP_BARCODESEARCHMODE searchMode = bo.getSearchMode();
            if ( searchMode != BarcodeDetectionOptions.defaultSearchMode)
            {
                singleList.set(0,  searchMode.ordinal());
                ci.setBarcodeSearchMode(singleList, op);
            }
            else
                ci.setBarcodeSearchMode(null, op);

            ci.setBarcodeSearchPriorities(bo.getSearchPrioritiesAsInt(), op);
            int timeout = bo.getTimeout();
            if (timeout != BarcodeDetectionOptions.defaultTimeout )
            {
                singleList.set(0,  timeout);
                ci.setBarcodeTimeout(singleList, op);
            }
            else
                ci.setBarcodeTimeout(null, op);
        }
    }

    public static void apply(TwainSource source, PatchcodeDetectionOptions po) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        boolean detectionEnabled = po.isEnabled();
        ci.setPatchcodeDetectionEnabled(Arrays.asList(detectionEnabled), op);
        List<Integer> singleList = Arrays.asList(0);
        if ( detectionEnabled )
        {
            int maxRetries = po.getMaxRetries();
            if ( maxRetries != PatchcodeDetectionOptions.defaultMaxRetries)
            {
                singleList.set(0, maxRetries);
                ci.setPatchcodeMaxRetries(singleList, op);
            }
            else
                ci.setPatchcodeMaxRetries(null, op);
            int searchPriorities = po.getMaxSearchPriorities();
            if ( searchPriorities != PatchcodeDetectionOptions.defaultMaxSearchPriorities)
            {
                singleList.set(0, searchPriorities);
                ci.setPatchcodeMaxSearchPriorities(singleList, op);
            }
            else
                ci.setPatchcodeMaxSearchPriorities(null, op);

            ICAP_PATCHCODESEARCHMODE searchMode = po.getSearchMode();
            if ( searchMode != PatchcodeDetectionOptions.defaultSearchMode)
            {
                singleList.set(0,  searchMode.ordinal());
                ci.setPatchcodeSearchMode(singleList, op);
            }
            else
                ci.setPatchcodeSearchMode(null, op);

            ci.setPatchcodeSearchPriorities(po.getSearchPrioritiesAsInt(), op);
            int timeout = po.getTimeout();
            if (timeout != PatchcodeDetectionOptions.defaultTimeout )
            {
                singleList.set(0,  timeout);
                ci.setPatchcodeTimeout(singleList, op);
            }
            else
                ci.setPatchcodeTimeout(null, op);
        }
    }

    public static void apply(TwainSource source, AutoCaptureOptions co) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Integer> singleList = Arrays.asList(0);
        int numImages = co.getNumImages();
        if ( numImages != AutoCaptureOptions.defaultNumImages)
        {
            singleList.set(0, co.getNumImages());
            ci.setAutomaticCapture(singleList, op);
        }
        else
            ci.setAutomaticCapture(null, op);

        int timebefore = co.getTimeBeforeFirstCapture();
        if ( timebefore != AutoCaptureOptions.defaultTimeBeforeFirstCapture)
        {
            singleList.set(0, timebefore);
            ci.setTimeBeforeFirstCapture(singleList, op);
        }
        else
            ci.setTimeBeforeFirstCapture(null, op);

        int timebetween = co.getTimeBetweenCaptures();
        if ( timebetween != AutoCaptureOptions.defaultTimeBetweenCaptures)
        {
            singleList.set(0, timebetween);
            ci.setTimeBetweenCaptures(singleList, op);
        }
        else
            ci.setTimeBetweenCaptures(null, op);
    }

    public static void apply(TwainSource source, ImageTypeOptions io) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Integer> singleList = Arrays.asList(0);

        ICAP_PIXELTYPE pixType = io.getPixelType();
        if ( pixType != ImageTypeOptions.defaultPixelType)
        {
            singleList.set(0,  pixType.ordinal());
            ci.setPixelType(singleList, op);
        }
        else
            ci.setPixelType(null, op);

        int bitDepth = io.getBitDepth();
        if ( bitDepth != ImageTypeOptions.defaultBitDepth)
        {
            singleList.set(0, bitDepth);
            ci.setBitDepth(singleList, op);
        }

        ICAP_BITDEPTHREDUCTION reduction = io.getBitDepthReduction();
        if (reduction != ImageTypeOptions.defaultBitDepthReduction)
        {
            singleList.set(0,  reduction.ordinal());
            ci.setBitDepthReduction(singleList, op);
        }
        else
            ci.setBitDepthReduction(null, op);

        ICAP_BITORDER bitOrder = io.getBitOrder();
        if ( bitOrder != ImageTypeOptions.defaultBitOrder)
        {
            singleList.set(0,  bitOrder.ordinal());
            ci.setBitOrder(singleList, op);
        }
        else
            ci.setBitOrder(null, op);

        ICAP_PIXELFLAVOR flavor = io.getPixelFlavor();
        if (flavor != ImageTypeOptions.defaultPixelFlavor)
        {
            singleList.set(0, flavor.ordinal());
            ci.setPixelFlavor(singleList, op);
        }
        else
            ci.setPixelFlavor(null, op);

        double threshold = io.getThreshold();
        if ( threshold != ImageTypeOptions.defaultThreshold )
            ci.setThreshold(Arrays.asList(threshold), op);
        else
            ci.setThreshold(null, op);

        String halftone = io.getHalftone();
        if ( halftone.compareTo(ImageTypeOptions.defaultHalftone) != 0)
            ci.setHalftones(Arrays.asList(halftone), op);
        else
            ci.setHalftones(null, op);

        if (!io.getCustomHalfTone().isEmpty())
            ci.setCustHalftone(io.getCustomHalfTone(), op);
        else
            ci.setCustHalftone(null, op);
    }

    public static void apply(TwainSource source, ImageInformationOptions io) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<String> singleList = Arrays.asList(new String());
        String author = io.getAuthor();
        singleList.set(0, author);
        ci.setAuthor(singleList, op);
        String caption = io.getCaption();
        singleList.set(0, caption);
        ci.setCaption(singleList, op);
        ci.setExtImageInfo(Arrays.asList(io.isExtendedImageInfoAvailable()), op);
    }

    public static void apply(TwainSource source, UserInterfaceOptions ui) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        boolean indicators = ui.isShowIndicators();
        ci.setIndicators(Arrays.asList(indicators), op);
        List<Integer> intList= ui.getIndicatorsModeAsInt();
        if ( !intList.isEmpty())
            ci.setIndicatorsMode(intList, op);
        else
            ci.setIndicatorsMode(null, op);
    }

    public static void apply(TwainSource source, ImageParameterOptions io) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Double> doubleList = Arrays.asList(0.0);
        double val = io.getBrightness();
        if ( val != ImageParameterOptions.defaultBrightness )
        {
            doubleList.set(0,  val);
            ci.setBrightness(doubleList, op);
        }
        else
            ci.setBrightness(null, op);

        val = io.getContrast();
        if ( val != ImageParameterOptions.defaultContrast )
        {
            doubleList.set(0,  val);
            ci.setContrast(doubleList, op);
        }
        else
            ci.setContrast(null, op);

        val = io.getHighlight();
        if ( val != ImageParameterOptions.defaultHighlight )
        {
            doubleList.set(0,  val);
            ci.setHighlight(doubleList, op);
        }
        else
            ci.setHighlight(null, op);

        val = io.getShadow();
        if ( val != ImageParameterOptions.defaultShadow )
        {
            doubleList.set(0,  val);
            ci.setShadow(doubleList, op);
        }
        else
            ci.setShadow(null, op);

        val = io.getXScaling();
        if ( val != ImageParameterOptions.defaultXScaling )
        {
            doubleList.set(0,  val);
            ci.setXScaling(doubleList, op);
        }
        else
            ci.setXScaling(null, op);

        val = io.getYScaling();
        if ( val != ImageParameterOptions.defaultYScaling )
        {
            doubleList.set(0,  val);
            ci.setYScaling(doubleList, op);
        }
        else
            ci.setYScaling(null, op);

        List<Boolean> boolList = Arrays.asList(false);
        boolList.set(0, io.isThumbnailsEnabled());
        ci.setThumbnailsEnabled(boolList, op);
        boolList.set(0, io.isAutoBrightEnabled());
        ci.setAutoBright(boolList, op);
        ci.setImageDataSet(io.getImageDataSets(), op);
        
        List<Integer> intList = Arrays.asList(0);
        intList.set(0, io.getMirror().ordinal());
        ci.setMirror(intList, op);
        
        intList.set(0, io.getOrientation().ordinal());
        ci.setOrientation(intList, op);
    }

    public static void apply(TwainSource source, AudibleAlarmsOptions aa) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        int volume = aa.getVolume();
        if ( volume != AudibleAlarmsOptions.defaultAlarmVolume )
            ci.setAlarmVolume(Arrays.asList(volume), op);
        else
            ci.setAlarmVolume(null, op);

        List<Integer> intList = aa.getAlarmsAsInt();
        if ( !intList.isEmpty())
            ci.setAlarms(intList, op);
        else
            ci.setAlarms(null, op);
    }

    public static void apply(TwainSource source, DeviceEventOptions dopt) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        ci.setDeviceEvent(dopt.getDeviceEventsAsInt(), op);
    }

    public static void apply(TwainSource source, PaperHandlingOptions po) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Boolean> boolList = Arrays.asList(false);

        boolList.set(0,  po.isAutoFeedEnabled());
        ci.setAutoFeed(boolList, op);

        boolList.set(0,  po.isDuplexEnabled());
        ci.setDuplexEnabled(boolList, op);

        boolList.set(0,  po.isFeederEnabled());
        ci.setFeederEnabled(boolList, op);

        boolList.set(0,  po.isFeederPrepEnabled());
        ci.setFeederPrep(boolList, op);

        List<Integer> intList = Arrays.asList(0);
        ICAP_FEEDERTYPE feederType = po.getFeederType();
        if ( feederType != PaperHandlingOptions.defaultFeederType)
        {
            intList.set(0, feederType.ordinal());
            ci.setFeederType(intList, op);
        }
        else
            ci.setFeederType(null, op);

        CAP_FEEDERORDER feederOrder = po.getFeederOrder();
        if ( feederOrder != PaperHandlingOptions.defaultFeederOrder)
        {
            intList.set(0, feederOrder.ordinal());
            ci.setFeederOrder(intList, op);
        }
        else
            ci.setFeederOrder(null, op);

        ci.setFeederPocket(po.getFeederPocketsAsInt(), op);

        CAP_PAPERHANDLING paperHandling = po.getPaperHandling();
        if ( paperHandling != PaperHandlingOptions.defaultPaperHandling)
        {
            intList.set(0, paperHandling.ordinal());
            ci.setPaperHandling(intList, op);
        }
        else
            ci.setPaperHandling(null, op);
    }

    public static void apply(TwainSource source, ColorOptions co) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Integer> singleList = Arrays.asList(0);
        ICAP_ICCPROFILE profile = co.getICCProfile();
        if ( profile != ColorOptions.defaultICCProfile )
        {
            singleList.set(0, profile.ordinal());
            ci.setICCProfile(singleList, op);
        }
        else
            ci.setICCProfile(null, op);
        ICAP_PLANARCHUNKY planarChunky = co.getPlanarChunky();
        if ( planarChunky != ColorOptions.defaultPlanarChunkyValue)
        {
            singleList.set(0,  planarChunky.ordinal());
            ci.setPlanarChunky(singleList, op);
        }
        else
            ci.setPlanarChunky(null, op);

        double gamma = co.getGamma();
        if ( gamma != ColorOptions.defaultGamma)
            ci.setGamma(Arrays.asList(gamma), op);
        else
            ci.setGamma(null, op);

        ci.setColorManagementEnabled(Arrays.asList(co.isEnabled()), op);
    }

    public static void apply(TwainSource source, ResolutionOptions res) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Double> singleList = Arrays.asList(0.0);
        double resX = res.getXResolution();
        double resY = res.getYResolution();
        if ( resX != ResolutionOptions.defaultXResolution)
        {
            singleList.set(0,  resX);
            ci.setXResolution(singleList, op);
        }
        else
            ci.setXResolution(null, op);
        if ( resY != ResolutionOptions.defaultYResolution)
        {
            singleList.set(0,  resY);
            ci.setYResolution(singleList, op);
        }
        else
            ci.setYResolution(null, op);
    }

    public static void apply(TwainSource source, CapNegotiationOptions cn) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        if ( cn.isEnabled() )
            ci.setExtendedCaps(cn.getExtendedCaps(), op);
        else
            ci.setExtendedCaps(null, op);
    }

    public static void apply(TwainSource source, MICROptions ms) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        ci.setMicrEnabled(Arrays.asList(ms.isEnabled()), op);
    }

    public static void apply(TwainSource source, ImprinterOptions io) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Integer> intList = Arrays.asList(0);
        boolean printerEnabled = io.isEnabled();
        ci.setPrinterEnabled(Arrays.asList(printerEnabled), op);
        if ( printerEnabled )
        {
            intList.set(0,  io.getIndex());
            ci.setPrinterIndex(intList, op);

            intList.set(0, io.getCharRotation());
            ci.setPrinterCharRotation(intList, op);

            intList.set(0, io.getMaxIndex());
            ci.setPrinterIndexMaxValue(intList, op);

            intList.set(0,  io.getNumDigits());
            ci.setPrinterIndexNumDigits(intList, op);

            intList.set(0,  io.getIndexStep());
            ci.setPrinterIndexStep(intList, op);

            CAP_PRINTERMODE printMode = io.getStringMode();
            if ( printMode != ImprinterOptions.defaultStringMode )
            {
                intList.set(0,  printMode.ordinal());
                ci.setPrinterMode(intList, op);
            }
            else
                ci.setPrinterMode(null, op);
            ci.setPrinterFontStyle(io.getFontStylesAsInt(), op);
            ci.setPrinterString(io.getStrings(), op);
            ci.setPrinterIndexTrigger(io.getIndexTriggersAsInt(), op);
            List<String> stringList = Arrays.asList("");
            stringList.set(0, io.getSuffixString());
            ci.setPrinterSuffix(stringList, op);
            stringList.set(0,  io.getLeadChar());
            ci.setPrinterIndexLeadChar(stringList, op);
        }
    }

    public static void apply(TwainSource source, CompressionOptions co) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        List<Integer> intList = Arrays.asList(0);

        if ( co.getCompression() != CompressionOptions.defaultCompression )
        {
            intList.set(0, co.getCompression().ordinal());
            ci.setCompression(intList, op);
        }
        else
            ci.setCompression(null, op);


        if ( co.getBitOrderValue() != CompressionOptions.defaultBitOrderValue)
        {
            intList.set(0, co.getBitOrderValue().ordinal());
            ci.setBitOrderCodes(intList, op);
        }
        else
            ci.setBitOrderCodes(null, op);

        if ( co.getCCITTKFactor() != CompressionOptions.defaultCCITTKFactor )
        {
            intList.set(0, co.getCCITTKFactor());
            ci.setCCITTKFactor(intList, op);
        }
        else
            ci.setCCITTKFactor(null, op);

        if ( co.getJpegPixelType() != CompressionOptions.defaultJPEGPixelType)
        {
            intList.set(0, co.getJpegPixelType().ordinal());
            ci.setJpegPixelType(intList, op);
        }
        else
            ci.setJpegPixelType(null, op);

        if ( co.getJpegQuality() != CompressionOptions.defaultJPEGQuality)
        {
            intList.set(0,  co.getJpegQuality());
            ci.setJpegQuality(intList, op);
        }
        else
            ci.setJpegQuality(null, op);

        if ( co.getJpegSubSampling() != CompressionOptions.defaultJPEGSubSampling)
        {
            intList.set(0,  co.getJpegSubSampling().ordinal());
            ci.setJpegSubsampling(intList, op);
        }
        else
            ci.setJpegSubsampling(null, op);

        if (co.getPixelFlavorCodes() != CompressionOptions.defaultPixelFlavorCodes)
        {
            intList.set(0, co.getPixelFlavorCodes().ordinal());
            ci.setPixelFlavorCodes(intList, op);
        }
        else
            ci.setPixelFlavorCodes(null, op);

        if (co.getTimeFill() != CompressionOptions.defaultTimeFill)
        {
            intList.set(0, co.getTimeFill());
            ci.setTimefill(intList, op);
        }
        else
            ci.setTimefill(null, op);
    }
    
    public static void apply(TwainSource source, AutoScanningOptions ao) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = source.getCapabilityInterface();
        CapabilityInterface.SetCapOperation op = ci.set();
        
        boolean autoscanEnabled = ao.isAutoScanEnabled();
        ci.setAutoscan(Arrays.asList(autoscanEnabled), op);
        
        if ( ao.getMaxBatchBuffers() != AutoScanningOptions.defaultMaxBatchBuffers )
        {
            List<Integer> intList = Arrays.asList(0);
            intList.set(0,  ao.getMaxBatchBuffers());
            ci.setMaxBatchBuffers(intList, op);
        }
    }        
}