/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.backend;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author robol
 */
public class Employee {
    
    @XmlElement(name = "name")
    public String name;
    
    @XmlElement(name = "hours")
    public double hours;
    
    @XmlElement(name = "experienced")
    public boolean experienced;
    
    @XmlElementWrapper(name = "free-mornings")
    @XmlElement(name = "morning")
    public ArrayList<Integer> free_mornings = new ArrayList<>();
    
    @XmlElementWrapper(name = "free-afternoons")
    @XmlElement(name = "afternoon")
    public ArrayList<Integer> free_afternoons = new ArrayList<>();
    
    @XmlElement(name = "free-sunday")
    public boolean free_sunday = false;
    
    public enum ShiftPreference {
        FREE_DAYS,
        SHORTER_SHIFTS,
        SAME
    }
    
    @XmlElement(name = "shift-preference")
    public ShiftPreference shift_preference = ShiftPreference.SAME;
    
    public Employee() {
        name = null;
        hours = 0.0;
        experienced = false;
    }
    
    public Employee(String name, double hours) {
        this.name = name;
        this.hours = hours;
    }
    
}
