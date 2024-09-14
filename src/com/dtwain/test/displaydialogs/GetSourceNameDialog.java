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
package com.dtwain.test.displaydialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;


class GetSourceNameDialog extends JDialog {
    private JTextField textField;
    private String sourceName;

    public String getSelectedSource()
    {
        return sourceName;
    }
    /**
     * Create the frame
     */
    public GetSourceNameDialog() {
        //super();
        super();
        setModal(true);
        addComponentListener(new ComponentAdapter() {
            public void componentHidden(final ComponentEvent e) {
                sourceName = textField.getText();
            }
        });
        setTitle("Select Source");
        getContentPane().setLayout(null);
        setBounds(100, 100, 288, 162);
        setResizable(false);

        final JLabel sourcesLabel = new JLabel();
        sourcesLabel.setText("Source Name:");
        sourcesLabel.setBounds(12, 12, 86, 30);
        getContentPane().add(sourcesLabel);

        textField = new JTextField();
        textField.setBounds(120, 15, 153, 26);
        getContentPane().add(textField);

        final JButton okButton = new JButton();
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                sourceName = textField.getText();
                setVisible(false);
            }
        });
        okButton.setActionCommand("CloseDialog");
        okButton.setText("Ok");
        okButton.setBounds(105, 75, 80, 35);
        getContentPane().add(okButton);
    }
}
