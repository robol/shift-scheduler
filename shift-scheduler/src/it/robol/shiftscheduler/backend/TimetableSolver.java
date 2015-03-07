/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.backend;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_iocp;
import org.gnu.glpk.glp_prob;

/**
 *
 * @author robol
 */
public class TimetableSolver {
    
    private EmployeeStorage storage = EmployeeStorage.getInstance();
    private ShiftStorage shift_storage = ShiftStorage.getInstance();
    private SettingsStorage settings = SettingsStorage.getInstance();
    
    /**
     * @brief The number of shift in the morning. 
     */
    private int mm;
    
    /**
     * @brief The number of shift in the afternoon.
     */
    private int ma;
    
    /**
     * @brief The number of shift of Sunday.
     */
    private int ms;

    /**
     * @brief The total number of shift in a day.
     */    
    private int m;
    
    /**
     * @brief The number of non-sunday days in a week. We keep this as a 
     * variable to make the code easier to read. 
     */
    private final int d = 6;
    
    /**
     * @brief The number of employees. 
     */
    private int n;
    
    // We are assuming that the number of coefficients is smaller than
    // 2^15. This is quite reasonable for our small problems, buy maybe
    // a precise computation could be better here. 
    private final int MAX_SIZE = 32768;
        
    private SWIGTYPE_p_int ia;
    private SWIGTYPE_p_int ja;
    private SWIGTYPE_p_double va;
    
    /**
     * @brief The total number of elements inserted until now.
     */
    int el;
    
    /**
     * @brief The total number of rows inserted. 
     */
    int r;
        
    private glp_prob problem = null;
    
    private boolean debug_enabled = false;
    
    public TimetableSolver() {        
    }
    
    public void setDebug(boolean debug) {
        debug_enabled = debug;
    }
    
    /**
     * @brief Retrieve the dimensions that will be useful in creating 
     * the problem, such as the number of shifts per day. 
     */
    private void computeBounds() {
        mm = shift_storage.getMorningShiftNumber() + 1;
        ma = shift_storage.getAfternoonShiftNumber() + 1;
        ms = shift_storage.getSundayShiftNumber() + 1;        
                
        m = mm + ma;
        n = storage.getEmployeeNumber();
    }
    
    private void prepareVariables() {
        // These are the int and double arrays needed by GLPK to describe
        // the (sparse) matrix that represent the constraints. 
        ia = GLPK.new_intArray(MAX_SIZE);        
        ja = GLPK.new_intArray(MAX_SIZE);
        va = GLPK.new_doubleArray(MAX_SIZE);
        
        // This index counts the elements inserted in the matrix. 
        el = 0;
        
        // This index count the number of the current row. 
        r  = 1;
    }
    
    public Timetable solveProblem() {
        
        // Prepare appropriate bounds and empty the arrays that will hold
        // the values of the constraint matrix. 
        computeBounds(); 
        prepareVariables();
        
        problem = GLPK.glp_create_prob();
        
        // Setup all the variables needed. 
        GLPK.glp_add_cols(problem, n * (m * d + ms));
        
        // Make sure that GLPK know that our columns are BV
        for (int i = 1; i <= n*(m*d+ms); i++) {
            GLPK.glp_set_col_kind(problem, i, GLPK.GLP_BV);
        }
        
        // Part 1: Select only one shift per every morning and one shift for
        // every afternoon. 
        requireOneShiftOnly();
        
        // Part 2: Make sure that each one has the correct number of
        // working hours in the week. 
        requireCorrectWorkingHours();
        
        // Part 3: Make sure that we have at least three people every day at
        // work. We get this by summing the number of empty shifts each day
        // and asking that number to be smaller than n - 3. In principle it
        // could be possible to specify an array of minimum people working, 
        // so we keep an array here with the correct numbers in place
        requireMinimumWorkingPeople();
        
        // Part 4: Make sure that each employee doesn't work on his/her
        // free days. 
        requireCoherentFreeDays();
                
        // Part 5: Make sure that in each day there all
        // the working hours are covered. In particular, we need to have  
        // at least a fixed amount of people to open and a fixed amount of
        // people to close. 
        requireOpeningTimesCoverage();
        
        // Part 6: Make sure that in each shift we have always k experienced
        // employee present, where k is defined by the appropriate value
        // in the SettingsStorage. 
        // We split this analysis in parts: we construct the set of times where
        // a shift ends. For each of these times we check that until that
        // timeframe we had the required number of employees. 
        requireExperiencedEmployees(settings.minimum_experienced_employees);
        
        // Setup an objective function according to the 
        // preferences of the employees. 
        setupObjectiveFunction();
                
        // Load matrix in the problem and solve it
        GLPK.glp_load_matrix(problem, el, ia, ja, va);
        
        // The following code can be used to debug the system matrix
        if (debug_enabled) {
            debugSystemMatrix(System.out);
        }
        
        glp_iocp parm = new glp_iocp();
        GLPK.glp_init_iocp(parm);
        parm.setPresolve(1);
        parm.setTm_lim(10000);
        
        int return_code = GLPK.glp_intopt(problem, parm);
        int status = GLPK.glp_mip_status(problem);
        
        // In case an error occurred or no solution was found, due to its
        // non existance or to the solver not being able to find it, return null
        if (return_code != GLPKConstants.GLP_ETMLIM && 
           (return_code != 0 || (status == GLPKConstants.GLP_NOFEAS ||
                                 status == GLPKConstants.GLP_UNDEF))) {
            return null;
        }
        
        // The following is here only for debug purpose.
        if (debug_enabled)
            debugComputedShifts(System.out);
        
        System.out.println("Computed function: " + GLPK.glp_mip_obj_val(problem));
        
        return recoverTimeTable();
    }
    
    private Timetable recoverTimeTable() {
        Timetable t = new Timetable();
        
        // Retrieve the values of the shifts computed. 
        for (int i = 1; i <= n; i++) {
            Employee e = storage.getEmployee(i-1);
            // System.out.print(e.name + ": ");
            
            for (int ii = 1; ii <= d; ii++) {
                int morning_choice = 0;
                int afternoon_choice = 0;
                
                for (int iii = 1; iii <= mm; iii++) {
                    if (GLPK.glp_mip_col_val(problem, (i-1)*(m*d+ms) + (ii-1)*m + iii) == 1.0) {
                        morning_choice = iii;
                    }
                }
                
                for (int iii = 1; iii <= ma; iii++) {
                    if (GLPK.glp_mip_col_val(problem, (i-1)*(m*d+ms) + (ii-1)*m + mm + iii) == 1.0) {
                        afternoon_choice = iii;
                    }
                }
                                
                t.setMorningShift(e, ii-1, 
                    (morning_choice < mm) ? shift_storage.getMorningShift(morning_choice-1) : null);
                t.setAfternoonShift(e, ii-1, 
                    (afternoon_choice < ma) ? shift_storage.getAfternoonShift(afternoon_choice-1) : null);
            }
            
            int sunday_choice = 0;
            for (int iii = 1; iii <= ms; iii++) {
                if (GLPK.glp_mip_col_val(problem, (i-1)*(m*d+ms) + d*m + iii) == 1.0) {
                    sunday_choice = iii;
                }
            }
            
            // System.out.println(sunday_choice);            
            t.setSundayShift(e, 
                (sunday_choice < ms) ? shift_storage.getSundayShift(sunday_choice - 1) : null);
        }
        
        return t;
    }
    
    private void requireOneShiftOnly() {
        GLPK.glp_add_rows(problem, n * (2*d+1));
        for (int i = 0; i < n; i++) {
            int shift = i * (d * m + ms);
            
            for (int ii = 0; ii < d; ii++) {                
                // One shift only in the morning
                for (int iii = shift + ii*m + 1; iii <= shift + ii*m + mm; iii++) {
                    GLPK.intArray_setitem(ia, ++el, r);
                    GLPK.intArray_setitem(ja, el, iii);
                    GLPK.doubleArray_setitem(va, el, 1);
                }
                GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_FX, 1.0, 1.0);
                r++;
                
                // One shift only in the afternoon
                for (int iii = shift + ii*m + mm + 1; iii <= shift + ii*m + m; iii++) {
                    GLPK.intArray_setitem(ia, ++el, r);
                    GLPK.intArray_setitem(ja, el, iii);
                    GLPK.doubleArray_setitem(va, el, 1);
                }
                GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_FX, 1.0, 1.0);
                r++;
            }
            
            // One shift only on Sunday
            for (int iii = shift + m*d + 1; iii <= shift + m*d + ms; iii++) {                
                GLPK.intArray_setitem(ia, ++el, r);
                GLPK.intArray_setitem(ja, el, iii);
                GLPK.doubleArray_setitem(va, el, 1);
            }
            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_FX, 1.0, 1.0);
            r++;
        }        
    }
    
    private void requireCorrectWorkingHours() {
        GLPK.glp_add_rows(problem, n);
        for (int i = 0; i < n; i++) {
            int shift = i * (d*m+ms);
            
            for (int ii = 1; ii <= d; ii++) {
                for (int iii = 1; iii <= mm; iii++) {
                    GLPK.intArray_setitem(ia, ++el, r);
                    GLPK.intArray_setitem(ja, el, shift + (ii-1)*m + iii);
                    GLPK.doubleArray_setitem(va, el, 
                            (iii < mm) ? shift_storage.getMorningShift(iii-1).length() : 0.0);
                }
                for (int iii = 1; iii <= ma; iii++) {
                    GLPK.intArray_setitem(ia, ++el, r);
                    GLPK.intArray_setitem(ja, el, shift + (ii-1)*m + mm + iii);
                    GLPK.doubleArray_setitem(va, el, 
                            (iii < ma) ? shift_storage.getAfternoonShift(iii-1).length() : 0.0);
                }
            }
            
            // Sunday shifts are at the end
            for (int iii = 1; iii <= ms; iii++) {
                GLPK.intArray_setitem(ia, ++el, r);
                GLPK.intArray_setitem(ja, el, shift + d*m + iii);
                GLPK.doubleArray_setitem(va, el, 
                        (iii < ms) ? shift_storage.getSundayShift(iii-1).length() : 0.0);
            }
            
            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_FX, 
                    storage.getEmployee(i).hours, storage.getEmployee(i).hours);
            r++;
        }
    }
    
    private void requireMinimumWorkingPeople() {
        int minimum_morning_people[] =  settings.minimum_working_people_morning;
        int minimum_afternoon_people[] = settings.minimum_working_people_afternoon;
        int minimum_sunday_people = (ms > 1) ? settings.minimum_working_people_sunday : 0;
        
        GLPK.glp_add_rows(problem, 2*d + 1);
        for (int i = 1 ; i <= d; i++) {
            // Counting shifts in the morning
            for (int ii = 1; ii <= n; ii++) {
                GLPK.intArray_setitem(ia, ++el, r);
                GLPK.intArray_setitem(ja, el, (ii-1)*(d*m+ms) + (i-1)*m + mm);
                GLPK.doubleArray_setitem(va, el, 1.0);
            }
            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_UP, 0.0, n-minimum_morning_people[i-1]);
            r++;
            
            // Counting shifts in the afternoon
            for (int ii = 1; ii <= n; ii++) {
                GLPK.intArray_setitem(ia, ++el, r);
                GLPK.intArray_setitem(ja, el, (ii-1)*(d*m+ms) + (i-1)*m + m);
                GLPK.doubleArray_setitem(va, el, 1.0);
            }
            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_UP, 0.0, n-minimum_afternoon_people[i-1]);
            r++;
        }
        
        // Counting shifts on Sundays
        for (int ii = 1; ii <= n; ii++) {
            GLPK.intArray_setitem(ia, ++el, r);
            GLPK.intArray_setitem(ja, el, (ii-1)*(d*m+ms) + d*m+ms);
            GLPK.doubleArray_setitem(va, el, 1.0);
        }
        GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_UP, 0.0, n-minimum_sunday_people);
        r++;
    }
    
    private void requireCoherentFreeDays() {
        for (int i = 0; i < n; i++) {
            Employee e = storage.getEmployee(i);
            int shift = i*(d*m+ms);
            
            for (Integer free_morning : e.free_mornings) {
                GLPK.glp_add_rows(problem, 1);
                for (int ii = 1; ii < mm; ii++) {
                    GLPK.intArray_setitem(ia, ++el, r);
                    GLPK.intArray_setitem(ja, el, shift + ii + free_morning * m);
                    GLPK.doubleArray_setitem(va, el, 1.0);
                }
                GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_FX, 0.0, 0.0);
                r++;
            }
            
            for (Integer free_afternoon : e.free_afternoons) {
                GLPK.glp_add_rows(problem, 1);
                for (int ii = 1; ii < ma; ii++) {
                    GLPK.intArray_setitem(ia, ++el, r);
                    GLPK.intArray_setitem(ja, el, shift + ii + mm + free_afternoon * m);
                    GLPK.doubleArray_setitem(va, el, 1.0);
                }
                GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_FX, 0.0, 0.0);
                r++;
            }
            
            if (e.free_sunday) {
                GLPK.glp_add_rows(problem, 1);
                for (int ii = 1; ii < ms; ii++) {
                    GLPK.intArray_setitem(ia, ++el, r);
                    GLPK.intArray_setitem(ja, el, shift + ii + d*m);
                    GLPK.doubleArray_setitem(va, el, 1.0);
                }
                GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_UP, 0.0, 0.0);
                r++;
            }
        }        
    }
    
    private void requireOpeningTimesCoverage() {
        // First of all, find the minimum of the start of the morning shifts        
        // and check that at least settings.minimum_open_employee are present
        // in the morning and in the afternoon. 
        GLPK.glp_add_rows(problem, 4*d+2);
        
        for (int i = 0; i < d; i++) {            
                        
            Time minimum_start = getMinimumMorningStart();
            Time maximum_end   = getMaximumMorningEnd();
            
            for (int ii = 0; ii < shift_storage.getMorningShiftNumber(); ii++) {
                Shift s = shift_storage.getMorningShift(ii);
                if (s.start.compare(minimum_start) == 0) {
                    for (int iii = 0; iii < n; iii++) {
                        int shift = iii*(d*m+ms);
                        GLPK.intArray_setitem(ia, ++el, r);
                        GLPK.intArray_setitem(ja, el, shift + i*m + ii + 1);
                        GLPK.doubleArray_setitem(va, el, 1.0);
                    }
                }
                if (s.end.compare(maximum_end) == 0) {
                    for (int iii = 0; iii < n; iii++) {
                        int shift = iii*(d*m+ms);
                        GLPK.intArray_setitem(ia, ++el, r+1);
                        GLPK.intArray_setitem(ja, el, shift + i*m + ii + 1);
                        GLPK.doubleArray_setitem(va, el, 1.0);
                    }
                }
            }            
            
            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_LO, settings.minimum_open_employee, 0.0);
            GLPK.glp_set_row_bnds(problem, r+1, GLPKConstants.GLP_LO, settings.minimum_close_employee, 0.0);
            
            r = r + 2;
            
            minimum_start = getMinimumAfternoonStart();
            maximum_end = getMaximumAfternoonEnd();
            
            for (int ii = 0; ii < shift_storage.getAfternoonShiftNumber(); ii++) {
                Shift s = shift_storage.getAfternoonShift(ii);
                if (s.start.compare(minimum_start) == 0) {
                    for (int iii = 0; iii < n; iii++) {
                        int shift = iii*(d*m+ms);
                        GLPK.intArray_setitem(ia, ++el, r);
                        GLPK.intArray_setitem(ja, el, shift + i*m + mm + ii + 1);
                        GLPK.doubleArray_setitem(va, el, 1.0);
                    }
                }
                if (s.end.compare(maximum_end) == 0) {
                    for (int iii = 0; iii < n; iii++) {
                        int shift = iii*(d*m+ms);
                        GLPK.intArray_setitem(ia, ++el, r+1);
                        GLPK.intArray_setitem(ja, el, shift + i*m + mm + ii + 1);
                        GLPK.doubleArray_setitem(va, el, 1.0);
                    }
                }
            }              
            
            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_LO, settings.minimum_open_employee, 0.0);
            GLPK.glp_set_row_bnds(problem, r+1, GLPKConstants.GLP_LO, settings.minimum_close_employee, 0.0);
            
            r = r + 2;
        }
        
        Time minimum_start = getMinimumSundayStart();
        Time maximum_end = getMaximumSundayEnd();
        
        for (int ii = 0; ii < shift_storage.getSundayShiftNumber(); ii++) {
            Shift s = shift_storage.getSundayShift(ii);
            if (s.start.compare(minimum_start) == 0) {
                for (int iii = 0; iii < n; iii++) {
                    int shift = iii*(d*m+ms);
                    GLPK.intArray_setitem(ia, ++el, r);
                    GLPK.intArray_setitem(ja, el, shift + d*m + ii + 1);
                    GLPK.doubleArray_setitem(va, el, 1.0);
                }
            }
            if (s.end.compare(maximum_end) == 0) {
                for (int iii = 0; iii < n; iii++) {
                    int shift = iii*(d*m+ms);
                    GLPK.intArray_setitem(ia, ++el, r+1);
                    GLPK.intArray_setitem(ja, el, shift + d*m  + ii + 1);
                    GLPK.doubleArray_setitem(va, el, 1.0);
                }
            }
        }         
        
        if (ms > 1) {
            GLPK.glp_set_row_bnds(problem, r,   GLPKConstants.GLP_LO, settings.minimum_open_employee, 0.0);
            GLPK.glp_set_row_bnds(problem, r+1, GLPKConstants.GLP_LO, settings.minimum_close_employee, 0.0);
        }
        
        r = r + 2;        
    }
    
    private void requireExperiencedEmployees(int k) {
        Set<Time> shiftChange = new HashSet<>();
        
        Time minimum_working_time = getMinimumMorningStart();
        
        for (int i = 0; i < shift_storage.getMorningShiftNumber(); i++) {
            Shift s = shift_storage.getMorningShift(i);
            shiftChange.add(s.end);
        }
        
        GLPK.glp_add_rows(problem, (shiftChange.size() + 1) * d);
        for (Time t : shiftChange) {
            // Make sure that the sum of exprienced people working in the shift
            // ending at a time == t is at least k. This has to be repeated 
            // for every working day. 
            for (int i = 0; i < d; i++) {
                for (int ii = 0; ii < shift_storage.getMorningShiftNumber(); ii++) {
                    Shift s = shift_storage.getMorningShift(ii);
                    
                    if (s.start.compare(t) < 0 && s.end.compare(t) >= 0) {
                        for (int iii = 0; iii < n; iii++) {
                            Employee e = storage.getEmployee(iii);
                            if (e.experienced) {
                                GLPK.intArray_setitem(ia, ++el, r);
                                GLPK.intArray_setitem(ja, el, iii*(d*m+ms) + m*i + ii + 1);
                                GLPK.doubleArray_setitem(va, el, 1.0);
                            }
                        }
                    }
                }
                
                GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_LO, k, 0.0);
                r = r + 1;
            }            
        }
        
        for (int i = 0; i < d; i++) {
            for (int ii = 0; ii < shift_storage.getMorningShiftNumber(); ii++) {
                Shift s = shift_storage.getMorningShift(ii);
                if (s.start.equal(minimum_working_time)) {
                    for (int iii = 0; iii < n; iii++) {
                        Employee e = storage.getEmployee(iii);
                        if (e.experienced) {
                            GLPK.intArray_setitem(ia, ++el, r);
                            GLPK.intArray_setitem(ja, el, iii*(d*m+ms) + m*i + ii + 1);
                            GLPK.doubleArray_setitem(va, el, 1.0);
                        }
                    }
                }
            }
            
            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_LO, k, 0.0);
            r = r + 1;
        }
        
        // Reset everything and repeat for the afternoons.         
        shiftChange = new HashSet<>();        
        minimum_working_time = getMinimumAfternoonStart();
        
        for (int i = 0; i < shift_storage.getAfternoonShiftNumber(); i++) {
            Shift s = shift_storage.getAfternoonShift(i);
            shiftChange.add(s.end);
        }
        
        GLPK.glp_add_rows(problem, (shiftChange.size() + 1) * d);
        for (Time t : shiftChange) {
            // Make sure that the sum of exprienced people working in the shift
            // ending at a time >= t and starting at time < t is at least k. 
            // This has to be repeated  for every working day. 
            for (int i = 0; i < d; i++) {
                for (int ii = 0; ii < shift_storage.getAfternoonShiftNumber(); ii++) {
                    Shift s = shift_storage.getAfternoonShift(ii);
                    
                    if (s.start.compare(t) < 0 && s.end.compare(t) >= 0) {
                        for (int iii = 0; iii < n; iii++) {
                            Employee e = storage.getEmployee(iii);
                            if (e.experienced) {
                                GLPK.intArray_setitem(ia, ++el, r);
                                GLPK.intArray_setitem(ja, el, iii*(d*m+ms) + m*i + mm + ii + 1);
                                GLPK.doubleArray_setitem(va, el, 1.0);
                            }
                        }
                    }
                }
                
                GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_LO, k, 0.0);
                r = r + 1;
            }            
        }
        
        for (int i = 0; i < d; i++) {
            for (int ii = 0; ii < shift_storage.getAfternoonShiftNumber(); ii++) {
                Shift s = shift_storage.getAfternoonShift(ii);
                if (s.start.equal(minimum_working_time)) {
                    for (int iii = 0; iii < n; iii++) {
                        Employee e = storage.getEmployee(iii);
                        if (e.experienced) {
                            GLPK.intArray_setitem(ia, ++el, r);
                            GLPK.intArray_setitem(ja, el, iii*(d*m+ms) + m*i + mm + ii + 1);
                            GLPK.doubleArray_setitem(va, el, 1.0);
                        }
                    }
                }
            }
            
            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_LO, k, 0.0);
            r = r + 1;
        }        
        
        // Reset everything again and reimpose the constraint on Sundays
        shiftChange = new HashSet<>();        
        minimum_working_time = getMinimumSundayStart();
        
        for (int i = 0; i < shift_storage.getSundayShiftNumber(); i++) {
            Shift s = shift_storage.getSundayShift(i);
            shiftChange.add(s.end);
        }
        
        GLPK.glp_add_rows(problem, shiftChange.size() + 1);
        for (Time t : shiftChange) {
            // Make sure that the sum of exprienced people working in the shift
            // ending at a time >= t and starting at time < t is at least k. 
            for (int ii = 0; ii < shift_storage.getSundayShiftNumber(); ii++) {
                Shift s = shift_storage.getSundayShift(ii);

                if (s.start.compare(t) < 0 && s.end.compare(t) >= 0) {
                    for (int iii = 0; iii < n; iii++) {
                        Employee e = storage.getEmployee(iii);
                        if (e.experienced) {
                            GLPK.intArray_setitem(ia, ++el, r);
                            GLPK.intArray_setitem(ja, el, iii*(d*m+ms) + m*d + ii + 1);
                            GLPK.doubleArray_setitem(va, el, 1.0);
                        }
                    }
                }
            }

            GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_LO, k, 0.0);
            r = r + 1;
        }            
        
        for (int ii = 0; ii < shift_storage.getSundayShiftNumber(); ii++) {
            Shift s = shift_storage.getSundayShift(ii);
            if (s.start.equal(minimum_working_time)) {
                for (int iii = 0; iii < n; iii++) {
                    Employee e = storage.getEmployee(iii);
                    if (e.experienced) {
                        GLPK.intArray_setitem(ia, ++el, r);
                        GLPK.intArray_setitem(ja, el, iii*(d*m+ms) + m*d + ii + 1);
                        GLPK.doubleArray_setitem(va, el, 1.0);
                    }
                }
            }
        }

        GLPK.glp_set_row_bnds(problem, r, GLPKConstants.GLP_LO, k, 0.0);
        r = r + 1;
    }
    
    private Time getMinimumMorningStart() {
        Time minimum_start = new Time(24,0);
        
        for (int i = 0; i < 6; i++) {
            for (int ii = 0; ii < shift_storage.getMorningShiftNumber(); ii++) {
                Shift s = shift_storage.getMorningShift(ii);
                if (s.start.compare(minimum_start) < 0) {
                    minimum_start = s.start;
                }
            }
        }        
        
        return minimum_start;
    }
    
    private Time getMaximumMorningEnd() {
        Time maximum_end = new Time(0,0);
        
        for (int i = 0; i < 6; i++) {
            for (int ii = 0; ii < shift_storage.getMorningShiftNumber(); ii++) {
                Shift s = shift_storage.getMorningShift(ii);
                if (s.end.compare(maximum_end) > 0) {
                    maximum_end = s.end;
                }
            }
        }        
        
        return maximum_end;
    }

    private Time getMinimumAfternoonStart() {
     Time minimum_start = new Time(24,0);

     for (int i = 0; i < 6; i++) {
         for (int ii = 0; ii < shift_storage.getAfternoonShiftNumber(); ii++) {
             Shift s = shift_storage.getAfternoonShift(ii);
             if (s.start.compare(minimum_start) < 0) {
                 minimum_start = s.start;
             }
         }
     }        

     return minimum_start;
 }

 private Time getMaximumSundayEnd() {
     Time maximum_end = new Time(0,0);

     for (int i = 0; i < 6; i++) {
         for (int ii = 0; ii < shift_storage.getSundayShiftNumber(); ii++) {
             Shift s = shift_storage.getSundayShift(ii);
             if (s.end.compare(maximum_end) > 0) {
                 maximum_end = s.end;
             }
         }
     }        

     return maximum_end;
 }
 
    private Time getMinimumSundayStart() {
     Time minimum_start = new Time(24,0);

     for (int i = 0; i < 6; i++) {
         for (int ii = 0; ii < shift_storage.getSundayShiftNumber(); ii++) {
             Shift s = shift_storage.getSundayShift(ii);
             if (s.start.compare(minimum_start) < 0) {
                 minimum_start = s.start;
             }
         }
     }        

     return minimum_start;
 }

 private Time getMaximumAfternoonEnd() {
     Time maximum_end = new Time(0,0);

     for (int i = 0; i < 6; i++) {
         for (int ii = 0; ii < shift_storage.getAfternoonShiftNumber(); ii++) {
             Shift s = shift_storage.getAfternoonShift(ii);
             if (s.end.compare(maximum_end) > 0) {
                 maximum_end = s.end;
             }
         }
     }        

     return maximum_end;
 } 
 
 private void setupObjectiveFunction() {          
     GLPK.glp_set_obj_dir(problem, GLPKConstants.GLP_MIN);
     
     for (int i = 0; i < n; i++) {
         Employee e = storage.getEmployee(i);
         
         for (int ii = 0; ii < d; ii++) {
             for (int iii = 0; iii < shift_storage.getMorningShiftNumber(); iii++) {
                Shift s = shift_storage.getMorningShift(iii);
                double l = 0.0;
                int shift = i*(m*d+ms);
                                           
                switch (e.shift_preference) {
                    case FREE_DAYS:
                        l = 1.0;
                        break;
                    case SHORTER_SHIFTS:
                        l = s.length() * s.length();
                        break;
                }                
                
                GLPK.glp_set_obj_coef(problem, shift + ii*d + iii, l);
             }
             
             for (int iii = 0; iii < shift_storage.getAfternoonShiftNumber(); iii++) {
                Shift s = shift_storage.getMorningShift(iii);
                double l = 0.0;
                int shift = i*(m*d+ms);
                                           
                switch (e.shift_preference) {
                    case FREE_DAYS:
                        l = 1.0;
                        break;
                    case SHORTER_SHIFTS:
                        l = s.length() * s.length();
                        break;
                }                
                
                GLPK.glp_set_obj_coef(problem, shift + ii*d + mm + iii, l); 
             }
         }
         
         for (int iii = 0; iii < shift_storage.getSundayShiftNumber(); iii++) {
            Shift s = shift_storage.getMorningShift(iii);
            double l = 0.0;
            int shift = i*(m*d+ms);

            switch (e.shift_preference) {
                case FREE_DAYS:
                    l = 1.0;
                    break;
                case SHORTER_SHIFTS:
                    l = s.length() * s.length();
                    break;
            }                

            GLPK.glp_set_obj_coef(problem, shift + m*d + iii, l);             
         }
     }
 }
 
 private void debugSystemMatrix(PrintStream outStream) {
        for (int i = 1; i <= GLPK.glp_get_num_rows(problem); i++) {
            int length = GLPK.glp_get_mat_row(problem, i, ja, va);
            for (int j = 1; j <= length; j++) {
                outStream.println("A[" + i + "," + 
                        GLPK.intArray_getitem(ja, j)+ "] = " + 
                        GLPK.doubleArray_getitem(va, j));
            }
            outStream.println(GLPK.glp_get_row_lb(problem, i) + 
                    " <= b[" + i + "] <= " + GLPK.glp_get_row_ub(problem, i));     
        }       
 }
 
 private void debugComputedShifts(PrintStream outStream) {
     for (int i = 1; i <= n; i++) {
            Employee e = storage.getEmployee(i-1);
            outStream.print(e.name + ": ");
            
            for (int ii = 1; ii <= d*m+ms; ii++) {
                outStream.print(GLPK.glp_mip_col_val(problem, (i-1)*(d*m+ms) + ii) + " ");
            }
            outStream.println("");
        }
 }
 
}
