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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.exceptions.DTwainRuntimeException;
import com.dynarithmic.twain.lowlevel.TwainConstantMapper;
import com.dynarithmic.twain.lowlevel.TwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.TW_FILESYSTEM;

public class DeviceCameraInfo
{
    Map<Integer, List<String>> allCamerasMap = new HashMap<>();
    List<String> topCameras = new ArrayList<>();
    List<String> bottomCameras = new ArrayList<>();
    List<String> allCameras = new ArrayList<>();
    
    TwainSource m_Source = null;
    private static final TwainConstantMapper<TW_FILESYSTEM> cameraToStringMap = 
            new TwainConstantMapper<>(TwainConstants.TW_FILESYSTEM.class);

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
        List<Integer> allConstants = cameraToStringMap.getInts();
        for (int i = 0; i < allConstants.size(); ++i)
        {
            int cameraToGet = allConstants.get(i);
            String [] theCameras = handle.DTWAIN_EnumCamerasEx(theSource.getSourceHandle(), cameraToGet);
            if ( theCameras.length > 0 )
            {
                List<String> theCameraList = Arrays.asList(theCameras);
                allCamerasMap.put(cameraToGet, theCameraList);
                allCameras.addAll(theCameraList);
            }
        }
        topCameras = allCamerasMap.get(TW_FILESYSTEM.TWFY_CAMERATOP);
        bottomCameras = allCamerasMap.get(TW_FILESYSTEM.TWFY_CAMERABOTTOM);
        m_Source = theSource;
        return this;
    }

    public List<String> getAllCameraNames()
    {
        return allCameras;
    }

    public List<String> getTopCameraNames()
    {
        return topCameras;
    }

    public List<String> getBottomCameraNames()
    {
        return bottomCameras;
    }

    private boolean checkCameraInList(List<String> cameraList, String name)
    {
        List<String> result = cameraList.stream()
                .filter(a -> Objects.equals(a, name))
                .collect(Collectors.toList());     
            return result.size() > 0;
    }

    boolean isTopCamera(String name)
    {
        return checkCameraInList(topCameras, name);
    }

    boolean isBottomCamera(String name)
    {
        return checkCameraInList(bottomCameras, name);
    }

    boolean isCamera(String name)
    {
        return checkCameraInList(allCameras, name);
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
    
    int getCameraType(String name)
    {
        Iterator<Map.Entry<Integer, List<String>>> iterator = allCamerasMap.entrySet().iterator();
        while (iterator.hasNext()) 
        {
            Map.Entry<Integer, List<String>> entry = iterator.next();
            if (checkCameraInList(entry.getValue(), name))
                return entry.getKey();
        }        
        return -1;
    }
}
