/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.desktop;
import it.robol.shiftscheduler.backend.SettingsStorage;

/**
 *
 * @author Leonardo Robol <leo@robol.it>
 */
public class SettingsDialog extends javax.swing.JDialog {

    /**
     * Creates new form SettingsDialog
     */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        SettingsStorage settings = SettingsStorage.getInstance();
        experience_employee.setValue(settings.minimum_experienced_employees);
        open_employee.setValue(settings.minimum_open_employee);
        close_employee.setValue(settings.minimum_close_employee);
        
        for (int i = 0; i < 6; i++)
            minimumWorkingEmployeesTable.getModel().setValueAt(
                    settings.minimum_working_people_morning[i], 0, i);
        minimumWorkingEmployeesTable.getModel().setValueAt(
                settings.minimum_working_people_sunday, 0, 6);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        experience_employee = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        open_employee = new javax.swing.JSpinner();
        close_employee = new javax.swing.JSpinner();
        saveButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        minimumWorkingEmployeesTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("it/robol/shiftscheduler/desktop/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("SettingsDialog.jLabel1.text")); // NOI18N

        jLabel2.setText(bundle.getString("SettingsDialog.jLabel2.text")); // NOI18N

        jLabel3.setText(bundle.getString("SettingsDialog.jLabel3.text")); // NOI18N

        saveButton.setText(bundle.getString("SettingsDialog.saveButton.text")); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("SettingsDialog.jLabel4.text")); // NOI18N

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        minimumWorkingEmployeesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        minimumWorkingEmployeesTable.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(minimumWorkingEmployeesTable);
        if (minimumWorkingEmployeesTable.getColumnModel().getColumnCount() > 0) {
            minimumWorkingEmployeesTable.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("SettingsDialog.minimumWorkingEmployeesTable.columnModel.title0")); // NOI18N
            minimumWorkingEmployeesTable.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("SettingsDialog.minimumWorkingEmployeesTable.columnModel.title1")); // NOI18N
            minimumWorkingEmployeesTable.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("SettingsDialog.minimumWorkingEmployeesTable.columnModel.title2")); // NOI18N
            minimumWorkingEmployeesTable.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("SettingsDialog.minimumWorkingEmployeesTable.columnModel.title3")); // NOI18N
            minimumWorkingEmployeesTable.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("SettingsDialog.minimumWorkingEmployeesTable.columnModel.title4")); // NOI18N
            minimumWorkingEmployeesTable.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("SettingsDialog.minimumWorkingEmployeesTable.columnModel.title5")); // NOI18N
            minimumWorkingEmployeesTable.getColumnModel().getColumn(6).setHeaderValue(bundle.getString("SettingsDialog.minimumWorkingEmployeesTable.columnModel.title6")); // NOI18N
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(open_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(experience_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(close_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(experience_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(open_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(close_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        SettingsStorage settings = SettingsStorage.getInstance();
        
        settings.minimum_close_employee = (int) close_employee.getValue();
        settings.minimum_open_employee  = (int) open_employee.getValue();
        settings.minimum_experienced_employees = (int) experience_employee.getValue();
        
        for (int i = 0; i < 6; i++) {
            int minimum = (int) minimumWorkingEmployeesTable.getModel().getValueAt(0, i);
            settings.minimum_working_people_morning[i] = minimum;
            settings.minimum_working_people_afternoon[i] = minimum;
        }
        
        settings.minimum_working_people_sunday = (int) minimumWorkingEmployeesTable.
                getModel().getValueAt(0, 6);
        
        settings.save();
        dispose();
    }//GEN-LAST:event_saveButtonActionPerformed
    
    
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
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SettingsDialog dialog = new SettingsDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JSpinner close_employee;
    private javax.swing.JSpinner experience_employee;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable minimumWorkingEmployeesTable;
    private javax.swing.JSpinner open_employee;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
