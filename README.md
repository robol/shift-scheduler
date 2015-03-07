# ShiftScheduler

ShiftScheduler is a simple application that computes shift timetables 
according to a set of constraints and trying to optimize the desires
of the employees. It is thought as tool for small shops in order to
organize their shifts. 

It allows to specify a number of morning and afternoon shifts for every
day and a set of shifts for Sunday. Individual free days and week work
hours can be configured for every employee as well as the preference
for more (shorter) shifts or less working days. Additional constrained
can be added in order to require that at least an appropriate number
of experienced employees are always present and so that that are always
a sufficient number of people at opening/closing time. 

This tool is based on the open source GLPK library for integer optimization, 
and is release under the GNU GPL License version 3. 

This repository contains two Java packages implementing the problem solution
and the graphical user interface, respectively. 

The dependencies available on Maven are specified using Gradle, and binary files
for GLPK are provided in libs/ for Windows and Linux for convenience. 

