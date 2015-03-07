/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.backend;

import java.io.File;
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
 * @author Leonardo Robol <leo@robol.it>
 */
@XmlRootElement(name = "settings")
public class SettingsStorage {
    
    private static SettingsStorage instance = null;
    
    @XmlElement(name = "experience-required")
    public int minimum_experienced_employees = 0;
    
    @XmlElement(name = "minimum-open-employee")
    public int minimum_open_employee = 0;
    
    @XmlElement(name = "minimum-close-employee")
    public int minimum_close_employee = 0;
    
    @XmlElementWrapper(name = "minimum-working-people-morning")
    @XmlElement(name = "number")
    public int[] minimum_working_people_morning;
    
    @XmlElementWrapper(name = "minimum-working-people-afternoon")
    @XmlElement(name = "number")    
    public int[] minimum_working_people_afternoon;
    
    @XmlElement(name = "minimum-working-people-sunday")
    public int minimum_working_people_sunday = 0;
    
    private SettingsStorage() { 
        minimum_working_people_morning = new int[6];
        minimum_working_people_afternoon = new int[6];
        
        for (int i = 0; i < 6; i++) {
            minimum_working_people_morning[i] = 0;
            minimum_working_people_afternoon[i] = 0;
        }
    }
    
    private static File getDb() {
        String path = SystemUtilities.getDataDir() + "/settings.xml";
        return new File(path);
    }
    
    public static SettingsStorage getInstance() {
        if (instance == null) {
            File db = getDb();
            
            try {
                if (db.exists()) {
                    JAXBContext ctx = JAXBContext.newInstance(SettingsStorage.class);
                    Unmarshaller m = ctx.createUnmarshaller();
                    instance = (SettingsStorage) m.unmarshal(db);
                }
                else {
                    instance = new SettingsStorage();
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        
        return instance;
    }
    
    public void save() {
        try {
            File db = getDb();
            JAXBContext ctx = JAXBContext.newInstance(SettingsStorage.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(this, db);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
}
