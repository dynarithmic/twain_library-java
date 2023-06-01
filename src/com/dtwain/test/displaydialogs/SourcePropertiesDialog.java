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
package com.dtwain.test.displaydialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSourceInfo;

class SourcePropertiesDialog extends JDialog {
    private JList caplist;

    public SourcePropertiesDialog(TwainSource currentSource, String [] capNames)
    {
        super();
        initialize();
        setModal(true);
        String sProductName = null;
        String sManufacturer = null;
        String sFamilyName = null;
        String sVersionInfo = null;
        int majorversion=0, minorversion=0;
        int numCustomCaps=0, numExtCaps=0;
        TwainSourceInfo info = currentSource.getInfo();
        sProductName = info.getProductName();
        sManufacturer = info.getManufacturer();
        sFamilyName = info.getProductFamily();
        sVersionInfo = info.getVersionInfo();
        majorversion = info.getMajorNum();
        minorversion = info.getMinorNum();
        numCustomCaps = currentSource.getCapabilityInterface().getNumCustomCaps();
        numExtCaps = currentSource.getCapabilityInterface().getNumExtendedCaps();
        Arrays.sort(capNames);
        getContentPane().setLayout(null);

        setTitle("Source Properties");
        setBounds(100, 100, 337, 425);
        setResizable(false);

        final JLabel productNameLabel = new JLabel();
        productNameLabel.setText("Product Name:");
        productNameLabel.setBounds(4, 6, 120, 30);
        getContentPane().add(productNameLabel);

        final JLabel productNameInfo = new JLabel();
        productNameInfo.setText(sProductName);
        productNameInfo.setBounds(140, 7, 215, 30);
        getContentPane().add(productNameInfo);

        final JLabel familyNameLabel = new JLabel();
        familyNameLabel.setText("Family Name:");
        familyNameLabel.setBounds(4, 31, 120, 30);
        getContentPane().add(familyNameLabel);

        final JLabel familyNameInfo = new JLabel();
        familyNameInfo.setText(sFamilyName);
        familyNameInfo.setBounds(140, 32, 215, 30);
        getContentPane().add(familyNameInfo);

        final JLabel manufaturerLabel = new JLabel();
        manufaturerLabel.setText("Manufaturer:");
        manufaturerLabel.setBounds(4, 56, 120, 30);
        getContentPane().add(manufaturerLabel);

        final JLabel manufacturerInfo = new JLabel();
        manufacturerInfo.setText(sManufacturer);
        manufacturerInfo.setBounds(140, 57, 215, 30);
        getContentPane().add(manufacturerInfo);

        final JLabel versionInfoLabel = new JLabel();
        versionInfoLabel.setText("Version Info:");
        versionInfoLabel.setBounds(4, 80, 120, 30);
        getContentPane().add(versionInfoLabel);

        final JLabel versionInfoInfo = new JLabel();
        versionInfoInfo.setText(sVersionInfo);
        versionInfoInfo.setBounds(140, 81, 215, 30);
        getContentPane().add(versionInfoInfo);

        final JLabel versionNumberLabel = new JLabel();
        versionNumberLabel.setText("Version Number:");
        versionNumberLabel.setBounds(4, 108, 120, 30);
        getContentPane().add(versionNumberLabel);

        final JLabel versionNumberInfo = new JLabel();
        String sVersion = majorversion + "." + minorversion;
        versionNumberInfo.setText(sVersion);
        versionNumberInfo.setBounds(140, 109, 215, 30);
        getContentPane().add(versionNumberInfo);


        final JLabel capabilitiesLabel = new JLabel();
        capabilitiesLabel.setText("Capabilities:");
        capabilitiesLabel.setBounds(4, 146, 120, 30);
        getContentPane().add(capabilitiesLabel);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(3, 175, 182, 171);
        getContentPane().add(scrollPane);

        caplist = new JList(capNames);
        scrollPane.setViewportView(caplist);

        final JLabel totalCapsLabel = new JLabel();
        totalCapsLabel.setText("Total Caps:");
        totalCapsLabel.setBounds(190, 175, 95, 30);
        getContentPane().add(totalCapsLabel);

        final JLabel totalCapsValue = new JLabel();
        totalCapsValue.setText("" + capNames.length);
        totalCapsValue.setBounds(305, 175, 76, 30);
        getContentPane().add(totalCapsValue);

        final JLabel customCapsLabel = new JLabel();
        customCapsLabel.setText("Custom Caps:");
        customCapsLabel.setBounds(190, 208, 95, 30);
        getContentPane().add(customCapsLabel);

        final JLabel customCapsValue = new JLabel();
        customCapsValue.setText("" + numCustomCaps);
        customCapsValue.setBounds(305, 208, 76, 30);
        getContentPane().add(customCapsValue);

        final JLabel extendedCapsLabel = new JLabel();
        extendedCapsLabel.setText("Extended Caps:");
        extendedCapsLabel.setBounds(190, 242, 95, 30);
        getContentPane().add(extendedCapsLabel);

        final JLabel extendedCapsValue = new JLabel();
        extendedCapsValue.setText("" + numExtCaps);
        extendedCapsValue.setBounds(305, 242, 76, 30);
        getContentPane().add(extendedCapsValue);

        final JButton okButton = new JButton();
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setText("Ok");
        okButton.setBounds(105, 353, 120, 30);
        okButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                setVisible(false);
            }
        }
        );

        getContentPane().add(okButton);

        final JSeparator separator = new JSeparator();
        separator.setBounds(18, 145, 295, 30);
        getContentPane().add(separator);

    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        this.setSize(new Dimension(348, 271));

    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
