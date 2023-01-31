package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import java.util.ArrayList;
import java.util.List;

import com.dynarithmic.twain.lowlevel.TwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;

/**
 * @author Dynarithmic Software
 * <p>AutoScanningOptions gets/sets the automatic scanning options described by the TWAIN 2.4 specification for
 * any TWAIN-enabled device.<br>
 * If the device does not support the automatic scanning option(s), these settings are ignored during the acquisition process.<br>
 *              (see "Automatic Scanning", Chapter 10-3 of the TWAIN 2.4 specification)
 */
public class AutoScanningOptions
{
    private boolean enableAutoScan = false;
    private boolean enableCameraHandling = false;
    private List<TwainConstants.ICAP_PIXELTYPE> cameraOrder = new ArrayList<>();
    private TwainConstants.CAP_CAMERASIDE cameraSide = TwainConstants.CAP_CAMERASIDE.TWCS_BOTH;
    private int maxBatchBuffers = defaultMaxBatchBuffers;

    public static final int defaultMaxBatchBuffers = Integer.MIN_VALUE;

    /**
     * @param enable Enable the automatic document scanning process.
     * @return The current object.
     * @see #isAutoScanEnabled()
     */
    public AutoScanningOptions enableAutoScan(boolean enable)
    {
        this.enableAutoScan = enable;
        return this;
    }

    /**
     * @param enableCameraHandling Enable camera handling from the current camera.
     * @return The current object.
     * @see #isCameraHandlingEnabled()
     * @see #setCameraSide(TwainConstants.CAP_CAMERASIDE)
     */
    public AutoScanningOptions enableCameraHandling(boolean enableCameraHandling)
    {
        this.enableCameraHandling = enableCameraHandling;
        return this;
    }

    /**
     * @param cameraOrder A list of pixel types that determines the order of output<br>
     *                    for Single Document Multiple Image (SDMI) mode.
     * @return The current object.
     * @see #getCameraOrder()
     */
    public AutoScanningOptions setCameraOrder(List<TwainConstants.ICAP_PIXELTYPE> cameraOrder)
    {
        this.cameraOrder = cameraOrder;
        return this;
    }

    /**
     * @param cameraSide Sets either the top, bottom, or both top and bottom cameras on a duplex device
     * @return The current object.
     * @see #getCameraSide()
     */
    public AutoScanningOptions setCameraSide(TwainConstants.CAP_CAMERASIDE cameraSide)
    {
        this.cameraSide = cameraSide;
        return this;
    }

    /**
     * @param maxBatchBuffers Set the number of pages that the scanner will buffer when auto scanning is enabled.
     * @return The current object.
     * @see #getMaxBatchBuffers()
     * @see #enableAutoScan(boolean)
     */
    public AutoScanningOptions setMaxBatchBuffers(int maxBatchBuffers)
    {
        this.maxBatchBuffers = maxBatchBuffers;
        return this;
    }

    /**
     * @return true if auto scanning is enabled, false otherwise.
     * @see #enableAutoScan(boolean)
     */
    public boolean isAutoScanEnabled()
    {
        return enableAutoScan;
    }


    /**
     * @return true if camera handling is enabled, false otherwise.
     * @see #enableCameraHandling(boolean)
     */
    public boolean isCameraHandlingEnabled()
    {
        return enableCameraHandling;
    }

    /**
     * @return A list of pixel types that descrie the camera order.
     * @see #setCameraOrder(List)
     */
    public List<TwainConstants.ICAP_PIXELTYPE> getCameraOrder()
    {
        return cameraOrder;
    }

    /**
     * @return The current camera(s) on a duplex device used to obtain the image.
     * @see #setCameraSide(TwainConstants.CAP_CAMERASIDE)
     */
    public TwainConstants.CAP_CAMERASIDE getCameraSide()
    {
        return cameraSide;
    }

    /**
     * @return The number of pages that the scanner will buffer when auto scanning is enabled
     * @see #setMaxBatchBuffers(int)
     */
    public int getMaxBatchBuffers()
    {
        return maxBatchBuffers;
    }

    static protected final int affectedCaps[] = {CAPS.CAP_AUTOSCAN,
                                                 CAPS.CAP_CAMERAENABLED,
                                                 CAPS.CAP_CAMERAORDER,
                                                 CAPS.CAP_CAMERASIDE,
                                                 CAPS.CAP_MAXBATCHBUFFERS};
    /**
     * @return an array of the TWAIN capabilities that the AutoScanningOptions will affect<br>
     *         when TwainSource.acquire() is called.
     */
    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
