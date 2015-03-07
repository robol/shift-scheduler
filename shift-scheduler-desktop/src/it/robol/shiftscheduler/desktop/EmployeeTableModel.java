/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.desktop;

import it.robol.shiftscheduler.backend.Employee;
import it.robol.shiftscheduler.backend.EmployeeStorage;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author robol
 */
public class EmployeeTableModel extends AbstractTableModel
    implements EmployeeStorage.Listener {
    
    private final EmployeeStorage storage;
    
    public EmployeeTableModel() {
        storage = EmployeeStorage.getInstance();
        storage.registerListener(this);
    }

    @Override
    public int getRowCount() {
        return storage.getEmployeeNumber();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee e = storage.getEmployee(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return e.name;
            case 1:
                return e.hours;
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Hours";
            default:
                return null;
        }
    }

    @Override
    public void onDataChanged() {
        fireTableDataChanged();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int colIndex) {
        return true;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int colIndex) {
        Employee e = storage.getEmployee(rowIndex);
        switch (colIndex) {
            case 0:
                e.name = (String) aValue;
                break;
            case 1:
                e.hours = Double.parseDouble((String) aValue);
                break;
        }
        
        storage.updateEmployee(rowIndex, e);
    }
    
    
    
}
