/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.desktop;

import it.robol.shiftscheduler.backend.ShiftStorage;
import javax.swing.AbstractListModel;

/**
 *
 * @author robol
 */
public class SundayShiftListModel extends AbstractListModel 
    implements ShiftStorage.Listener {
    
    private ShiftStorage ss = ShiftStorage.getInstance();
    
    public SundayShiftListModel() {
        ss.registerListener(this);
    }

    @Override
    public int getSize() {
        return ss.getSundayShiftNumber();
    }

    @Override
    public Object getElementAt(int index) {
        return ss.getSundayShift(index).name();
    }

    @Override
    public void onShiftChanged() {
        fireContentsChanged(this, 0, getSize() - 1);
    }    
    
}
