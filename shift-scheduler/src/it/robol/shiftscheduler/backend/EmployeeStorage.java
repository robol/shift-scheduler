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
 * @author robol
 */

@XmlRootElement(name = "employee-storage")
public class EmployeeStorage {
    
    private static EmployeeStorage instance = null;
    
    @XmlElementWrapper(name = "employees")
    @XmlElement(name = "employee")
    private ArrayList<Employee> employees = new ArrayList<>();
    
    private ArrayList<Listener> listeners = null;
    
    private final static String HEADER = ""
            + "# This text file contains the database of the Employees. "
            + "# Each line below contains an entry with the following"
            + "# format: Name, Week hours";            
    
    /**
     * @brief Interface implemented by Listeners that want to 
     * be notified on changes of the underlying data. 
     */
    public interface Listener {
        public void onDataChanged();
    }
    
    public void registerListener(Listener l) {
        listeners.add(l);
    }
    
    public void unregisterListener(Listener l) {
        listeners.remove(l);
    }
    
    private EmployeeStorage() {
        listeners = new ArrayList<>();
    }
    
    private static String getDbPath() {
        String pathToData = SystemUtilities.getDataDir();
        return pathToData + "/employees.xml";
    }
    
    public static EmployeeStorage getInstance() {
        if (instance == null) {
            try {
                File db = new File(getDbPath());
                
                if (db.exists()) {
                    JAXBContext ctx = JAXBContext.newInstance(EmployeeStorage.class);
                    Unmarshaller m = ctx.createUnmarshaller();
                    instance = (EmployeeStorage) m.unmarshal(db);
                }
                else {
                    instance = new EmployeeStorage();
                    
                }

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        
        return instance;
    }
    
    public void updateEmployee(int i, Employee e) {
        employees.set(i, e);
        saveEmployees();
    }
    
    public boolean saveEmployees() {
        
        try {
            JAXBContext ctx = JAXBContext.newInstance(EmployeeStorage.class);
            Marshaller  m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            File db = new File(getDbPath());
            
            m.marshal(this, db);
            
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        
        for (Listener l : listeners) {
            l.onDataChanged();
        }
        
        return true;
    }
    
    public boolean addEmployee(String name, double hours) {
        employees.add(new Employee(name, hours));
        return saveEmployees();
    }
    
    public boolean deleteEmployee(Employee e) {
        employees.remove(e);
        return saveEmployees();
    }
    
    public boolean deleteEmployee(int index) {
        employees.remove(index);
        return saveEmployees();
    }
    
    public int getEmployeeNumber() {
        return (employees == null) ? 0 : employees.size();
    }
    
    public Employee getEmployee (int i) {
        return employees.get(i);
    }
    
}
