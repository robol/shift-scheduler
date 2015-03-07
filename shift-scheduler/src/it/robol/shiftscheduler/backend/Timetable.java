/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @brief The TimeTable class provides a map from the set of Employee stored
 * in the {@link EmployeeStorage} and the Shifts defined in the {@link ShiftStorage}. 
 * 
 * You can access that {@link Shift} for any given day by calling the appropriate
 * getMorningShift(), getAfternoonShift and getSundayShift() functions. 
 * 
 * Note that these methods may return null if no Shift was assigned in that
 * time frame. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 */
public class Timetable {
    
    private final ArrayList<Map<Employee, Shift>> morning_shifts;
    private final ArrayList<Map<Employee, Shift>> afternoon_shifts;
    private final Map<Employee, Shift> sunday_shift;
        
    public Timetable() {
        morning_shifts = new ArrayList<>();
        afternoon_shifts = new ArrayList<>();
        
        for (int i = 0 ; i < 6; i++) {
            morning_shifts.add(new HashMap<Employee, Shift>());
            afternoon_shifts.add(new HashMap<Employee, Shift>());
        }
        
        sunday_shift = new HashMap<>();
    }
    
    public Shift getMorningShift(Employee e, int i) {
        return morning_shifts.get(i).get(e);
    }
    
    public Shift getAfternoonShift(Employee e, int i) {
        return afternoon_shifts.get(i).get(e);
    }
    
    public Shift getSundayShift(Employee e) {
        return sunday_shift.get(e);
    }
    
    public void setMorningShift(Employee e, int i, Shift shift) {
        morning_shifts.get(i).put(e, shift);
    }
    
    public void setAfternoonShift(Employee e, int i, Shift shift) {
        afternoon_shifts.get(i).put(e, shift);
    }    
    
    public void setSundayShift(Employee e, Shift shift) {
        sunday_shift.put(e, shift);
    }    
    
}
