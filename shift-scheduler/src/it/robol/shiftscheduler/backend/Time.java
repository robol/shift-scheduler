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
public class Time {
    
    @XmlElement(name = "hour")
    public int hour;
    
    @XmlElement(name = "minute")
    public int minute;
    
    public Time() {
    }
    
    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }
    
    public Time(String input) {
        String pieces[] = input.split(":");
        this.hour = Integer.parseInt(pieces[0]);
        this.minute = Integer.parseInt(pieces[1]);
    }
    
    @Override
    public String toString() {
        return String.format("%02d", hour) + ":" + 
               String.format("%02d", minute);
    }
    
    public double subtract(Time other) {
        return (minute - other.minute) / 60.0 + (hour - other.hour) * 1.0;
    }
    
    /**
     * @brief Detect of this instance of Time is bigger, equal or
     * less than another one. 
     * 
     * @param other Another instance of Time to be compared. 
     * @return 1 if this > other, 0, if this == other, -1 otherwise. 
     */
    public int compare(Time other) {
        int difference = 60 * (this.hour - other.hour) + (this.minute - other.minute);
        
        if (difference > 0)
            return 1;
        else if (difference == 0)
            return 0;
        else
            return -1;
    }
    
    /**
     * Check if two Time objects represents the same time. 
     * 
     * @param other The other time to check
     * @return true if they match, false otherwise. 
     */
    public boolean equal(Time other) {
        return compare(other) == 0;
    }
    
}
