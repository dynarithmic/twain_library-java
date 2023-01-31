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
