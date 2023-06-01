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
