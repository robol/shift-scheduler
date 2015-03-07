/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.desktop;

import it.robol.shiftscheduler.backend.Employee;
import it.robol.shiftscheduler.backend.EmployeeStorage;
import it.robol.shiftscheduler.backend.Shift;
import it.robol.shiftscheduler.backend.Timetable;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Leonardo Robol <leo@robol.it>
 */
public class TimetableTableModel extends AbstractTableModel {
    
    private Timetable timetable;
    
    public TimetableTableModel(Timetable t) {
        timetable = t;
    }

    @Override
    public int getRowCount() {
        // We do have 7 week days, and 2 turns for every day. We add another
        // row on the bottom with the total number of hours. 
        return 14 + 1;
    }

    @Override
    public int getColumnCount() {
        // One for each Employee plus an additional column for
        // the name of the day
        return EmployeeStorage.getInstance().getEmployeeNumber() + 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ResourceBundle bundle = java.util.ResourceBundle.getBundle(
                "it/robol/shiftscheduler/desktop/Bundle");
        columnIndex = columnIndex - 1;
        
        if (columnIndex == -1) {
            if (rowIndex == 14) {
                return bundle.getString("TimetableTableModel.TotalHours");
            }
            
            if (rowIndex % 2 == 0) {
                switch (rowIndex / 2) {
                    case 0:
                        return bundle.getString("TimetableTableModel.Monday");
                    case 1:
                        return bundle.getString("TimetableTableModel.Tuesday");
                    case 2:
                        return bundle.getString("TimetableTableModel.Wednesday");
                    case 3:
                        return bundle.getString("TimetableTableModel.Thursday");
                    case 4:
                        return bundle.getString("TimetableTableModel.Friday");
                    case 5:
                        return bundle.getString("TimetableTableModel.Saturday");
                    case 6:
                        return bundle.getString("TimetableTableModel.Sunday");
                }
            }
            else {
                return "";
            }
        }
        
        Employee e = EmployeeStorage.getInstance().getEmployee(columnIndex);
        
        if (rowIndex < 12) {
            Shift shift;
            if (rowIndex % 2 == 0)
                shift = timetable.getMorningShift(e, rowIndex / 2);
            else
                shift = timetable.getAfternoonShift(e, rowIndex / 2);            
            
            return ((shift == null) ? "X" : shift.name());
        }
        else if (rowIndex < 14) {
            Shift sunday_shift = timetable.getSundayShift(e);
            if (rowIndex % 2 == 0)
                return "X";
            else
                return (sunday_shift == null) ? "X" : sunday_shift.name();
        }
        else {
            double total = 0;
            Shift sunday_shift = timetable.getSundayShift(e);
            total = total + ((sunday_shift == null) ? 0 : sunday_shift.length());
            
            for (int ii = 0; ii < 6; ii++) {
                Shift morning_shift = timetable.getMorningShift(e, ii);
                Shift afternoon_shift = timetable.getAfternoonShift(e, ii);
                
                total = total + ((morning_shift == null) ? 0 : morning_shift.length()) + 
                        ((afternoon_shift == null) ? 0 : afternoon_shift.length());
            }
            
            return String.format("%2.1f", total);
        }
    }
    
    @Override
    public String getColumnName(int colIndex) {
        if (colIndex > 0)
            return EmployeeStorage.getInstance().getEmployee(colIndex - 1).name;
        else
            return "";
    }
    
}
