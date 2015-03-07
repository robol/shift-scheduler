/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.desktop;

import com.alee.laf.WebLookAndFeel;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author robol
 */
public class ShiftScheduler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new WebLookAndFeel());
        } catch (Exception e) {
            // Pass silently, since having the correct Look & Feel is
            // not vital to our application. 
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {  
                // Temporary solution to test the Italian locale
                Locale.setDefault(Locale.ITALY);
                
                final MainWindow w = new MainWindow();                
                w.setVisible(true);
            }
        });
    }
    
}
