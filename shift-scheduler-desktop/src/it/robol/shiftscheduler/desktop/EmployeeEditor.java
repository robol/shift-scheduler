/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.desktop;

import it.robol.shiftscheduler.backend.Employee;
import it.robol.shiftscheduler.backend.EmployeeStorage;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Leonardo Robol <leo@robol.it>
 */
public class EmployeeEditor extends javax.swing.JDialog {
    
    private int index;
    private Employee employee;

    /**
     * Creates new form EmployeeEditor
     */
    public EmployeeEditor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        ResourceBundle bundle = java.util.ResourceBundle.getBundle
                (Constants.TRANSLATION_BUNDLE);
        
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
        model.addElement(bundle.getString("EmployeeEditor.ShiftPreference.SAME"));
        model.addElement(bundle.getString("EmployeeEditor.ShiftPreference.SHORTER_SHIFTS"));
        model.addElement(bundle.getString("EmployeeEditor.ShiftPreference.FREE_DAYS"));
        
        shiftPreferenceComboBox.setModel(model);
    }
    
    public void setEmployee (int i) {
        index = i;
        employee = EmployeeStorage.getInstance().getEmployee(i);
        
        switch (employee.shift_preference) {
            case SAME:
                shiftPreferenceComboBox.setSelectedIndex(0);
                break;
            case SHORTER_SHIFTS:
                shiftPreferenceComboBox.setSelectedIndex(1);
                break;
            case FREE_DAYS:
                shiftPreferenceComboBox.setSelectedIndex(2);
                break;
        }
        
        setTitle("Editing: " + employee.name);
        experiencedCheckBox.setSelected(employee.experienced);
        
        for (Integer ii : employee.free_mornings) {
            switch (ii) {
                case 0:
                    mondayMorningCheckBox.setSelected(true);
                    break;
                case 1:
                    tuesdayMorningCheckBox.setSelected(true);
                    break;
                case 2:
                    wednesdayMorningCheckBox.setSelected(true);
                    break;
                case 3:
                    thursdayMorningCheckBox.setSelected(true);
                    break;
                case 4:
                    fridayMorningCheckBox.setSelected(true);
                    break;
                case 5:
                    saturdayMorningCheckBox.setSelected(true);
                    break;
            }
        }
            
         for (Integer ii : employee.free_afternoons) {
            switch (ii) {
                case 0:
                    mondayAfternoonCheckBox.setSelected(true);
                    break;
                case 1:
                    tuesdayAfternoonCheckBox.setSelected(true);
                    break;
                case 2:
                    wednesdayAfternoonCheckBox.setSelected(true);
                    break;
                case 3:
                    thursdayAfternoonCheckBox.setSelected(true);
                    break;
                case 4:
                    fridayAfternoonCheckBox.setSelected(true);
                    break;
                case 5:
                    saturdayAfternoonCheckBox.setSelected(true);
                    break;
            }
        }
         
        if (employee.free_sunday)
            sundayAfternoonCheckBox.setSelected(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        experiencedCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        mondayMorningCheckBox = new javax.swing.JCheckBox();
        mondayAfternoonCheckBox = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        tuesdayMorningCheckBox = new javax.swing.JCheckBox();
        tuesdayAfternoonCheckBox = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        wednesdayMorningCheckBox = new javax.swing.JCheckBox();
        wednesdayAfternoonCheckBox = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        thursdayMorningCheckBox = new javax.swing.JCheckBox();
        thursdayAfternoonCheckBox = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        fridayMorningCheckBox = new javax.swing.JCheckBox();
        fridayAfternoonCheckBox = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        saturdayMorningCheckBox = new javax.swing.JCheckBox();
        saturdayAfternoonCheckBox = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        sundayAfternoonCheckBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        shiftPreferenceComboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("it/robol/shiftscheduler/desktop/Bundle"); // NOI18N
        experiencedCheckBox.setText(bundle.getString("EmployeeEditor.experiencedCheckBox.text")); // NOI18N
        experiencedCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                experiencedCheckBoxActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel2.setText(bundle.getString("EmployeeEditor.jLabel2.text")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jLabel3.setText(bundle.getString("EmployeeEditor.jLabel3.text")); // NOI18N

        jButton1.setText(bundle.getString("EmployeeEditor.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(7, 3, 10, 5));

        jLabel4.setText(bundle.getString("EmployeeEditor.jLabel4.text")); // NOI18N
        jPanel1.add(jLabel4);

        mondayMorningCheckBox.setText(bundle.getString("EmployeeEditor.mondayMorningCheckBox.text")); // NOI18N
        jPanel1.add(mondayMorningCheckBox);

        mondayAfternoonCheckBox.setText(bundle.getString("EmployeeEditor.mondayAfternoonCheckBox.text")); // NOI18N
        jPanel1.add(mondayAfternoonCheckBox);

        jLabel5.setText(bundle.getString("EmployeeEditor.jLabel5.text")); // NOI18N
        jPanel1.add(jLabel5);

        tuesdayMorningCheckBox.setText(bundle.getString("EmployeeEditor.tuesdayMorningCheckBox.text")); // NOI18N
        jPanel1.add(tuesdayMorningCheckBox);

        tuesdayAfternoonCheckBox.setText(bundle.getString("EmployeeEditor.tuesdayAfternoonCheckBox.text")); // NOI18N
        jPanel1.add(tuesdayAfternoonCheckBox);

        jLabel6.setText(bundle.getString("EmployeeEditor.jLabel6.text")); // NOI18N
        jPanel1.add(jLabel6);

        wednesdayMorningCheckBox.setText(bundle.getString("EmployeeEditor.wednesdayMorningCheckBox.text")); // NOI18N
        jPanel1.add(wednesdayMorningCheckBox);

        wednesdayAfternoonCheckBox.setText(bundle.getString("EmployeeEditor.wednesdayAfternoonCheckBox.text")); // NOI18N
        jPanel1.add(wednesdayAfternoonCheckBox);

        jLabel7.setText(bundle.getString("EmployeeEditor.jLabel7.text")); // NOI18N
        jPanel1.add(jLabel7);

        thursdayMorningCheckBox.setText(bundle.getString("EmployeeEditor.thursdayMorningCheckBox.text")); // NOI18N
        jPanel1.add(thursdayMorningCheckBox);

        thursdayAfternoonCheckBox.setText(bundle.getString("EmployeeEditor.thursdayAfternoonCheckBox.text")); // NOI18N
        jPanel1.add(thursdayAfternoonCheckBox);

        jLabel8.setText(bundle.getString("EmployeeEditor.jLabel8.text")); // NOI18N
        jPanel1.add(jLabel8);

        fridayMorningCheckBox.setText(bundle.getString("EmployeeEditor.fridayMorningCheckBox.text")); // NOI18N
        jPanel1.add(fridayMorningCheckBox);

        fridayAfternoonCheckBox.setText(bundle.getString("EmployeeEditor.fridayAfternoonCheckBox.text")); // NOI18N
        jPanel1.add(fridayAfternoonCheckBox);

        jLabel9.setText(bundle.getString("EmployeeEditor.jLabel9.text")); // NOI18N
        jPanel1.add(jLabel9);

        saturdayMorningCheckBox.setText(bundle.getString("EmployeeEditor.saturdayMorningCheckBox.text")); // NOI18N
        jPanel1.add(saturdayMorningCheckBox);

        saturdayAfternoonCheckBox.setText(bundle.getString("EmployeeEditor.saturdayAfternoonCheckBox.text")); // NOI18N
        jPanel1.add(saturdayAfternoonCheckBox);

        jLabel10.setText(bundle.getString("EmployeeEditor.jLabel10.text")); // NOI18N
        jPanel1.add(jLabel10);
        jPanel1.add(filler1);

        sundayAfternoonCheckBox.setText(bundle.getString("EmployeeEditor.sundayAfternoonCheckBox.text")); // NOI18N
        sundayAfternoonCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sundayAfternoonCheckBoxActionPerformed(evt);
            }
        });
        jPanel1.add(sundayAfternoonCheckBox);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jLabel11.setText(bundle.getString("EmployeeEditor.jLabel11.text")); // NOI18N
        jPanel2.add(jLabel11);

        jPanel2.add(shiftPreferenceComboBox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(experiencedCheckBox)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(76, 133, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(experiencedCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void experiencedCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_experiencedCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_experiencedCheckBoxActionPerformed

    private void sundayAfternoonCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sundayAfternoonCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sundayAfternoonCheckBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Save the user data
        employee.experienced = experiencedCheckBox.isSelected();
        employee.free_sunday = sundayAfternoonCheckBox.isSelected();
        
        employee.free_mornings = new ArrayList<>();
        employee.free_afternoons = new ArrayList<>();
        
        if (mondayMorningCheckBox.isSelected())
            employee.free_mornings.add(0);
        if (mondayAfternoonCheckBox.isSelected())
            employee.free_afternoons.add(0);
        if (tuesdayMorningCheckBox.isSelected())
            employee.free_mornings.add(1);
        if (tuesdayAfternoonCheckBox.isSelected())
            employee.free_afternoons.add(1);
        if (wednesdayMorningCheckBox.isSelected())
            employee.free_mornings.add(2);
        if (wednesdayAfternoonCheckBox.isSelected())
            employee.free_afternoons.add(2);
        if (thursdayMorningCheckBox.isSelected())
            employee.free_mornings.add(3);
        if (thursdayAfternoonCheckBox.isSelected())
            employee.free_afternoons.add(3);
        if (fridayMorningCheckBox.isSelected())
            employee.free_mornings.add(4);
        if (fridayAfternoonCheckBox.isSelected())
            employee.free_afternoons.add(4);
        if (saturdayMorningCheckBox.isSelected())
            employee.free_mornings.add(5);
        if (saturdayAfternoonCheckBox.isSelected())
            employee.free_afternoons.add(5);        
        
        switch (shiftPreferenceComboBox.getSelectedIndex()) {
            case 0:
                employee.shift_preference = Employee.ShiftPreference.SAME;
                break;
            case 1:
                employee.shift_preference = Employee.ShiftPreference.SHORTER_SHIFTS;
                break;
            case 2:
                employee.shift_preference = Employee.ShiftPreference.FREE_DAYS;
                break;
        }
        
        EmployeeStorage.getInstance().updateEmployee(index, employee);
        
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmployeeEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EmployeeEditor dialog = new EmployeeEditor(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox experiencedCheckBox;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JCheckBox fridayAfternoonCheckBox;
    private javax.swing.JCheckBox fridayMorningCheckBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JCheckBox mondayAfternoonCheckBox;
    private javax.swing.JCheckBox mondayMorningCheckBox;
    private javax.swing.JCheckBox saturdayAfternoonCheckBox;
    private javax.swing.JCheckBox saturdayMorningCheckBox;
    private javax.swing.JComboBox shiftPreferenceComboBox;
    private javax.swing.JCheckBox sundayAfternoonCheckBox;
    private javax.swing.JCheckBox thursdayAfternoonCheckBox;
    private javax.swing.JCheckBox thursdayMorningCheckBox;
    private javax.swing.JCheckBox tuesdayAfternoonCheckBox;
    private javax.swing.JCheckBox tuesdayMorningCheckBox;
    private javax.swing.JCheckBox wednesdayAfternoonCheckBox;
    private javax.swing.JCheckBox wednesdayMorningCheckBox;
    // End of variables declaration//GEN-END:variables
}
