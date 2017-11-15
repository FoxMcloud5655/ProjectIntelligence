/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brandon3055.projectintelligence.client.gui.swing;

import com.brandon3055.brandonscore.handlers.FileHandler;
import com.brandon3055.projectintelligence.PIHelpers;

import javax.swing.*;
import java.awt.event.*;
import java.util.Map;

/**
 *
 * @author brandon3055
 */
public class UINewMod extends javax.swing.JDialog {

    private final Map<String, String> idNameMap;
    private boolean canceled = false;

    /**
     * Creates new form UINewMod
     */
    public UINewMod(java.awt.Frame parent, Map<String, String> idNameMap) {
        super(parent, true);
        this.idNameMap = idNameMap;
        initComponents();
        idNameMap.keySet().forEach(idSelector::addItem);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        idSelector = new JComboBox<>();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        nameField = new JTextField();
        jLabel3 = new JLabel();
        versionField = new JTextField();
        jButton1 = new JButton();
        jButton2 = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Mod");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Please ether select a mod from the list of installed mods (prefered) or manually type the modid and name of the mod you wish to add \ndocumentation for. (modid must be correct)\n\nThe mod version is the minimum version of the mod to which this documentation applies. If you want this to apply to all versions of the mod \nthen leave the default value of 0.0.0");
        jScrollPane1.setViewportView(jTextArea1);

        idSelector.setEditable(true);
        idSelector.setModel(new DefaultComboBoxModel<>(new String[] {}));
        idSelector.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                modChange(evt);
            }
        });
        idSelector.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                modChangeType(evt);
            }
        });

        jLabel1.setText("Mod ID:");

        jLabel2.setText("Mod Name:");

        jLabel3.setText("Min Mod Version:");

        versionField.setText("0.0.0");
        versionField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                versionFieldActionPerformed(evt);
            }
        });

        jButton1.setText("Add Mod");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addAction(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelAction(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel3))
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(versionField)
                                                .addComponent(idSelector, 0, 281, Short.MAX_VALUE)
                                                .addComponent(nameField)))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(idSelector, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(versionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(jButton2))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>


    private void versionFieldActionPerformed(ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void addAction(ActionEvent evt) {
        String error = null;

        if (PIHelpers.getSupportedMods().contains(getModID())) {
            error = "Mod already exists! Please update existing documentation for this mod instead.";
        }
        else if (getModID().isEmpty()) {
            error = "Please specify a mod id!";
        }
        else if (getModName().isEmpty()) {
            error = "Please specify a mod name!";
        }
        else if (!FileHandler.FILE_NAME_VALIDATOR.test(getModID())) {
            error = "Detected invalid mod id!";
        }
        else if (!FileHandler.FILE_NAME_VALIDATOR.test(getModVersion())) {
            error = "Detected invalid version string!";
        }

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            dispose();
        }
    }                          

    private void cancelAction(ActionEvent evt) {                              
        canceled = true;
        dispose();
    }                             

    private void modChange(ItemEvent evt) {
        String name = idNameMap.get(idSelector.getSelectedItem());
        if (name != null) {
            nameField.setText(name);
        }
    }

    private void modChangeType(KeyEvent evt) {}

    public String getModID() {
        if (idSelector.getSelectedItem() == null) {
            return null;
        }
        return idSelector.getSelectedItem().toString();
    }
    
    public String getModName() {
        return nameField.getText();
    }
    
    public String getModVersion() {
        return versionField.getText();
    }

    public boolean isCanceled() {
        return canceled;
    }

    // Variables declaration - do not modify
    private JComboBox<String> idSelector;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private JTextField nameField;
    private JTextField versionField;
    // End of variables declaration                   
}
