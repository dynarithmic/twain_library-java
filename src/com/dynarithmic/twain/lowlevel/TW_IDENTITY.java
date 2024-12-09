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
package com.dynarithmic.twain.lowlevel;

public class TW_IDENTITY extends TwainLowLevel
{
    private TW_UINT32  Id = new TW_UINT32();
    private TW_VERSION Version = new TW_VERSION();
    private TW_UINT16 ProtocolMajor = new TW_UINT16();
    private TW_UINT16 ProtocolMinor = new TW_UINT16();
    private TW_UINT32 SupportedGroups = new TW_UINT32();
    private TW_STR32  Manufacturer = new TW_STR32();
    private TW_STR32  ProductFamily = new TW_STR32();
    private TW_STR32  ProductName = new TW_STR32();

    public TW_IDENTITY()
    {
    }

    public TW_UINT32 getId()
    {
        return Id;
    }

    public TW_VERSION getVersion()
    {
        return Version;
    }

    public TW_UINT16 getProtocolMajor()
    {
        return ProtocolMajor;
    }

    public TW_UINT16 getProtocolMinor()
    {
        return ProtocolMinor;
    }

    public TW_UINT32 getSupportedGroups()
    {
        return SupportedGroups;
    }

    public TW_STR32 getManufacturer()
    {
        return Manufacturer;
    }

    public TW_STR32 getProductFamily()
    {
        return ProductFamily;
    }

    public TW_STR32 getProductName()
    {
        return ProductName;
    }

    public TW_IDENTITY setId(TW_UINT32 id)
    {
        Id = id;
        return this;
    }

    public TW_IDENTITY setVersion(TW_VERSION version)
    {
        Version = version;
        return this;
    }

    public TW_IDENTITY setProtocolMajor(TW_UINT16 protocolMajor)
    {
        ProtocolMajor = protocolMajor;
        return this;
    }

    public TW_IDENTITY setProtocolMinor(TW_UINT16 protocolMinor)
    {
        ProtocolMinor = protocolMinor;
        return this;
    }

    public TW_IDENTITY setSupportedGroups(TW_UINT32 supportedGroups)
    {
        SupportedGroups = supportedGroups;
        return this;
    }

    public TW_IDENTITY setManufacturer(TW_STR32 manufacturer)
    {
        Manufacturer = manufacturer;
        return this;
    }

    public TW_IDENTITY setProductFamily(TW_STR32 productFamily)
    {
        ProductFamily = productFamily;
        return this;
    }

    public TW_IDENTITY setProductName(TW_STR32 productName)
    {
        ProductName = productName;
        return this;
    }

    public TW_IDENTITY setId(long id)
    {
        Id.setValue(id);
        return this;
    }

    public TW_IDENTITY setProtocolMajor(int protocolMajor)
    {
        ProtocolMajor.setValue(protocolMajor);
        return this;
    }

    public TW_IDENTITY setProtocolMinor(int protocolMinor)
    {
        ProtocolMinor.setValue(protocolMinor);
        return this;
    }

    public TW_IDENTITY setSupportedGroups(long supportedGroups)
    {
        SupportedGroups.setValue(supportedGroups);
        return this;
    }

    public TW_IDENTITY setManufacturer(String manufacturer)
    {
        Manufacturer.setValue(manufacturer);
        return this;
    }

    public TW_IDENTITY setProductFamily(String productFamily)
    {
        ProductFamily.setValue(productFamily);
        return this;
    }

    public TW_IDENTITY setProductName(String productName)
    {
        ProductName.setValue(productName);
        return this;
    }

    public String asString()
    {
        return
                "ID: " + Id.getValue() + "\n" +
                "ProtocolMajor: " + ProtocolMajor.getValue() + "\n" +
                "ProtocolMinor: " + ProtocolMinor.getValue() + "\n" +
                "SupportedGroups: " + SupportedGroupsHandler.asString((int) SupportedGroups.getValue()) + "\n" +
                "Manufacturer: " + Manufacturer.getValue() + "\n" +
                "ProductFamily: " + ProductFamily.getValue() + "\n" +
                "ProductName: " + ProductName.getValue();
    }
}

