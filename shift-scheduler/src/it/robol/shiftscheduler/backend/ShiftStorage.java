/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author robol
 */

@XmlRootElement(name = "shift-storage")
public class ShiftStorage {
    
    @XmlElementWrapper(name = "morning")
    @XmlElement(name = "shift")
    private ArrayList<Shift> morning_shifts = new ArrayList<>();
    
    @XmlElementWrapper(name = "afternoon")
    @XmlElement(name = "shift")
    private ArrayList<Shift> afternoon_shifts = new ArrayList<>();
    
    @XmlElementWrapper(name = "sunday")
    @XmlElement(name = "shift")
    private ArrayList<Shift> sunday_shifts = new ArrayList<>();
    
    private static ShiftStorage self = null;
    
    public interface Listener {
        public void onShiftChanged();
    }
    
    private ArrayList<Listener> listeners = null;
    
    private ShiftStorage() {
        listeners = new ArrayList<>();
    }
    
    public static ShiftStorage getInstance() {
        if (self == null) {
            try {
                File db = new File(getDbPath());
                if (db.exists()) {                
                    JAXBContext ctx = JAXBContext.newInstance(ShiftStorage.class);
                    Unmarshaller m = ctx.createUnmarshaller();

                    self = (ShiftStorage) m.unmarshal(db);
                }
                else {
                    self = new ShiftStorage();
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        
        return self;
    }
    
    public void registerListener(Listener l) {
        listeners.add(l);
    }
    
    public void unregisterListener(Listener l) {
        listeners.remove(l);
    }
    
    public int getMorningShiftNumber() {
        return morning_shifts.size();
    }
    
    public int getAfternoonShiftNumber() {
        return afternoon_shifts.size();
    }
    
    public int getSundayShiftNumber() {
        return sunday_shifts.size();
    }
    
    public Shift getMorningShift(int i) {
        return morning_shifts.get(i);
    }
    
    public Shift getAfternoonShift(int i) {
        return afternoon_shifts.get(i);
    }
    
    public Shift getSundayShift(int i) {
        return sunday_shifts.get(i);
    }
    
    private static String getDbPath() {
        return SystemUtilities.getDataDir() + "/shifts.xml";
    }
    
    public void addMorningShift(Shift s) {
        morning_shifts.add(s);
        saveShifts();
    }
    
    public void addAfternoonShift(Shift s) {
        afternoon_shifts.add(s);
        saveShifts();
    }
    
    public void addSundayShift(Shift s) {
        sunday_shifts.add(s);
        saveShifts();
    }
    
    public void removeMorningShift(Shift s) {
        morning_shifts.remove(s);
        saveShifts();
    }
    
    public void removeAfternoonShift(Shift s) {
        afternoon_shifts.remove(s);
        saveShifts();
    }
    
    public void removeSundayShift(Shift s) {
        sunday_shifts.remove(s);
        saveShifts();
    }
    
    private void saveShifts() {
        File db = new File(getDbPath());
        
        try {
            JAXBContext ctx = JAXBContext.newInstance(ShiftStorage.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            m.marshal(this, new File(getDbPath()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        
        for (Listener l : listeners) {
            l.onShiftChanged();
        }
    }   
}
