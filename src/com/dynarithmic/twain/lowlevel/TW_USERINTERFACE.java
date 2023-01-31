package com.dynarithmic.twain.lowlevel;

public class TW_USERINTERFACE extends TwainLowLevel
{
    private TW_BOOL ShowUI = new TW_BOOL();
    private TW_BOOL ModalUI = new TW_BOOL();
    private TW_HANDLE hParent = new TW_HANDLE();

    public TW_USERINTERFACE()
    {
    }

    public TW_BOOL getShowUI()
    {
        return ShowUI;
    }

    public TW_BOOL getModalUI()
    {
        return ModalUI;
    }

    public TW_HANDLE gethParent()
    {
        return hParent;
    }

    public TW_USERINTERFACE setShowUI(TW_BOOL showUI)
    {
        ShowUI = showUI;
        return this;
    }

    public TW_USERINTERFACE setModalUI(TW_BOOL modalUI)
    {
        ModalUI = modalUI;
        return this;
    }

    public TW_USERINTERFACE sethParent(TW_HANDLE hParent)
    {
        this.hParent = hParent;
        return this;
    }

    public void setShowUI(boolean showUI)
    {
        ShowUI.setValue(showUI);
    }

    public void setModalUI(boolean modalUI)
    {
        ModalUI.setValue(modalUI);
    }
}
