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
package com.dynarithmic.twain.lowlevel;

public class TW_TWAINDIRECT extends TwainLowLevel
{
    private TW_UINT32 SizeOf = new TW_UINT32();
    private TW_UINT16 CommunicationManager = new TW_UINT16();
    private TW_HANDLE Send = new TW_HANDLE();
    private TW_UINT32 SendSize = new TW_UINT32();
    private TW_HANDLE Receive = new TW_HANDLE();
    private TW_UINT32 ReceiveSize = new TW_UINT32();

    public TW_TWAINDIRECT()
    {
    }

    public TW_UINT32 getSizeOf()
    {
        return SizeOf;
    }

    public TW_UINT16 getCommunicationManager()
    {
        return CommunicationManager;
    }

    public TW_HANDLE getSend()
    {
        return Send;
    }

    public TW_UINT32 getSendSize()
    {
        return SendSize;
    }

    public TW_HANDLE getReceive()
    {
        return Receive;
    }

    public TW_UINT32 getReceiveSize()
    {
        return ReceiveSize;
    }

    public TW_TWAINDIRECT setSizeOf(TW_UINT32 sizeOf)
    {
        SizeOf = sizeOf;
        return this;
    }

    public TW_TWAINDIRECT setCommunicationManager(TW_UINT16 communicationManager)
    {
        CommunicationManager = communicationManager;
        return this;
    }

    public TW_TWAINDIRECT setSend(TW_HANDLE send)
    {
        Send = send;
        return this;
    }

    public TW_TWAINDIRECT setSendSize(TW_UINT32 sendSize)
    {
        SendSize = sendSize;
        return this;
    }

    public TW_TWAINDIRECT setReceive(TW_HANDLE receive)
    {
        Receive = receive;
        return this;
    }

    public TW_TWAINDIRECT setReceiveSize(TW_UINT32 receiveSize)
    {
        ReceiveSize = receiveSize;
        return this;
    }

    public TW_TWAINDIRECT setSizeOf(long sizeOf)
    {
        SizeOf.setValue(sizeOf);
        return this;
    }

    public TW_TWAINDIRECT setCommunicationManager(int communicationManager)
    {
        CommunicationManager.setValue(communicationManager);
        return this;
    }

    public TW_TWAINDIRECT setSendSize(long sendSize)
    {
        SendSize.setValue(sendSize);
        return this;
    }

    public TW_TWAINDIRECT setReceiveSize(long receiveSize)
    {
        ReceiveSize.setValue(receiveSize);
        return this;
    }
}
