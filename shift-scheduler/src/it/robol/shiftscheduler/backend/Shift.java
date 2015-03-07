/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.backend;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author robol
 */
public class Shift {
    
    @XmlElement(name = "start")
    Time start;
    
    @XmlElement(name = "end")
    Time end;
    
    public Shift (Time start, Time end) {
        this.start = start;
        this.end = end;
    }
    
    public Shift() {
        this.start = null;
        this.end = null;
    }
    
    public String name() {
        if (start != null) {
            return start + " -> " + end;
        }
        else {
            return "Not working";
        }
    }
    
    public double length() {
        if (start != null) {
            return (end.subtract(start));
        }
        else 
            return 0.0;
    }
    
}
