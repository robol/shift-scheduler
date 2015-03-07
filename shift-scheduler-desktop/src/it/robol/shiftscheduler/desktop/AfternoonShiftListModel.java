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
public class AfternoonShiftListModel extends AbstractListModel
    implements ShiftStorage.Listener {
    
    private ShiftStorage ss = ShiftStorage.getInstance();
    
    public AfternoonShiftListModel() {
        ss.registerListener(this);
    }

    @Override
    public int getSize() {
        return ss.getAfternoonShiftNumber();
    }

    @Override
    public Object getElementAt(int index) {
        return ss.getAfternoonShift(index).name();
    }

    @Override
    public void onShiftChanged() {
        fireContentsChanged(this, 0, getSize() - 1);
    }    
    
}
