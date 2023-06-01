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
package com.dynarithmic.twain.lowlevel;

public class TW_CIECOLOR extends TwainLowLevel
{
    private TW_UINT16           ColorSpace = new TW_UINT16();
    private TW_INT16            LowEndian = new TW_INT16();
    private TW_INT16            DeviceDependent = new TW_INT16();
    private TW_INT32            VersionNumber = new TW_INT32();
    private TW_TRANSFORMSTAGE   StageABC = new TW_TRANSFORMSTAGE();
    private TW_TRANSFORMSTAGE   StageLMN = new TW_TRANSFORMSTAGE();
    private TW_CIEPOINT         WhitePoint = new TW_CIEPOINT();
    private TW_CIEPOINT         BlackPoint = new TW_CIEPOINT();
    private TW_CIEPOINT         WhitePaper = new TW_CIEPOINT();
    private TW_CIEPOINT         BlackInk = new TW_CIEPOINT();
    private TW_FIX32[] Samples = new TW_FIX32[0];

    public TW_CIECOLOR() {}

    public TW_CIECOLOR setNumSamples(int val)
    {
        if ( val != Samples.length )
        {
            TW_FIX32 [] temp = new TW_FIX32[val];
            for (int i = 0; i < val; ++i)
                temp[i] = new TW_FIX32();
            int minSamples = Math.min(val, Samples.length);
            System.arraycopy(Samples, 0, temp, 0, minSamples);
            Samples = temp;
        }
        return this;
    }

    public int getNumSamples()
    {
        return Samples.length;
    }

    public TW_UINT16 getColorSpace()
    {
        return ColorSpace;
    }

    public TW_INT16 getLowEndian()
    {
        return LowEndian;
    }

    public TW_INT16 getDeviceDependent()
    {
        return DeviceDependent;
    }

    public TW_INT32 getVersionNumber()
    {
        return VersionNumber;
    }

    public TW_TRANSFORMSTAGE getStageABC()
    {
        return StageABC;
    }

    public TW_TRANSFORMSTAGE getStageLMN()
    {
        return StageLMN;
    }

    public TW_CIEPOINT getWhitePoint()
    {
        return WhitePoint;
    }

    public TW_CIEPOINT getBlackPoint()
    {
        return BlackPoint;
    }

    public TW_CIEPOINT getWhitePaper()
    {
        return WhitePaper;
    }

    public TW_CIEPOINT getBlackInk()
    {
        return BlackInk;
    }

    public TW_FIX32[] getSamples()
    {
        return Samples;
    }

    public TW_FIX32 getSample(int i)
    {
        return Samples[i];
    }

    public TW_CIECOLOR setColorSpace(TW_UINT16 colorSpace)
    {
        ColorSpace = colorSpace;
        return this;
    }

    public TW_CIECOLOR setColorSpace(int colorSpace)
    {
        ColorSpace.setValue(colorSpace);
        return this;
    }

    public TW_CIECOLOR setLowEndian(TW_INT16 lowEndian)
    {
        LowEndian = lowEndian;
        return this;
    }

    public TW_CIECOLOR setLowEndian(short lowEndian)
    {
        LowEndian.setValue(lowEndian);
        return this;
    }

    public TW_CIECOLOR setDeviceDependent(TW_INT16 deviceDependent)
    {
        DeviceDependent = deviceDependent;
        return this;
    }

    public TW_CIECOLOR setDeviceDependent(short deviceDependent)
    {
        DeviceDependent.setValue(deviceDependent);
        return this;
    }

    public TW_CIECOLOR setVersionNumber(TW_INT32 versionNumber)
    {
        VersionNumber = versionNumber;
        return this;
    }

    public TW_CIECOLOR setVersionNumber(int versionNumber)
    {
        VersionNumber.setValue(versionNumber);
        return this;
    }

    public TW_CIECOLOR setStageABC(TW_TRANSFORMSTAGE stageABC)
    {
        StageABC = stageABC;
        return this;
    }

    public TW_CIECOLOR setStageLMN(TW_TRANSFORMSTAGE stageLMN)
    {
        StageLMN = stageLMN;
        return this;
    }

    public TW_CIECOLOR setWhitePoint(TW_CIEPOINT whitePoint)
    {
        WhitePoint = whitePoint;
        return this;
    }

    public TW_CIECOLOR setBlackPoint(TW_CIEPOINT blackPoint)
    {
        BlackPoint = blackPoint;
        return this;
    }

    public TW_CIECOLOR setWhitePaper(TW_CIEPOINT whitePaper)
    {
        WhitePaper = whitePaper;
        return this;
    }

    public TW_CIECOLOR setBlackInk(TW_CIEPOINT blackInk)
    {
        BlackInk = blackInk;
        return this;
    }

    public TW_CIECOLOR setSamples(TW_FIX32[] samples)
    {
        setNumSamples(samples.length);
        Samples = samples;
        return this;
    }

    public TW_CIECOLOR setSamples(double[] samples)
    {
        setNumSamples(samples.length);
        for (int i = 0; i < samples.length; ++i)
            Samples[i].setValue(samples[i]);
        return this;
    }

    public TW_CIECOLOR setSample(TW_FIX32 sample, int which)
    {
        Samples[which] = sample;
        return this;
    }

}
