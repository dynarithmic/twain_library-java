package com.dynarithmic.twain.highlevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.exceptions.DTwainRuntimeException;

public class DeviceCameraInfo
{
    Map<String, Boolean> allCamerasMap = new HashMap<>();
    TwainSource m_Source = null;

    public DeviceCameraInfo()
    {}

    public DeviceCameraInfo(TwainSource theSource) throws DTwainJavaAPIException, DTwainRuntimeException
    {
        attach(theSource);
    }

    public DeviceCameraInfo attach(TwainSource theSource) throws DTwainJavaAPIException
    {
        DTwainJavaAPI handle = theSource.getTwainSession().getAPIHandle();
        allCamerasMap.clear();
        String [] topCameras = handle.DTWAIN_EnumTopCameras(theSource.getSourceHandle());
        String [] bottomCameras = handle.DTWAIN_EnumBottomCameras(theSource.getSourceHandle());
        for (String camera : topCameras)
            allCamerasMap.put(camera, true);
        for (String camera : bottomCameras)
            allCamerasMap.put(camera, false);
        m_Source = theSource;
        return this;
    }

    public String [] getAllCameraNames()
    {
        String [] allCameras = new String[allCamerasMap.size()];
        int i = 0;
        for ( Entry<String, Boolean> entry : allCamerasMap.entrySet())
        {
            allCameras[i] = entry.getKey();
            ++i;
        }
        return allCameras;
    }

    private String [] cameraEnumerator(boolean which)
    {
        List<String> cameraList = new ArrayList<>();
        for ( Entry<String, Boolean> entry : allCamerasMap.entrySet())
        {
            if (entry.getValue() == which)
                cameraList.add(entry.getKey());
        }
        return cameraList.toArray(new String[cameraList.size()]);
    }

    public String [] getTopCameraNames()
    {
        return cameraEnumerator(true);
    }

    public String [] getBottomCameraNames()
    {
        return cameraEnumerator(false);
    }

    boolean isTopCamera(String name)
    {
        if (allCamerasMap.containsKey(name))
            return allCamerasMap.get(name) == true;
        return false;
    }

    boolean isBottomCamera(String name)
    {
        if (allCamerasMap.containsKey(name))
            return allCamerasMap.get(name) == false;
        return false;
    }

    boolean isCamera(String name)
    {
        return allCamerasMap.containsKey(name);
    }

    boolean setCamera(String name)
    {
        boolean ok = false;
        if ( m_Source != null)
        {
            DTwainJavaAPI handle = m_Source.getTwainSession().getAPIHandle();
            if ( isCamera(name))
                ok = handle.DTWAIN_SetCamera(m_Source.getSourceHandle(), name);
        }
        return ok;
    }
}
