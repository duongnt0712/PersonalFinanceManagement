package vn.com.courseman.it5.software;

import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.software.DomainAppToolSoftware;
import domainapp.software.SoftwareFactory;
import domainapp.softwareimpl.DomSoftware;
import vn.com.courseman.it5.model.City;
import vn.com.courseman.it5.model.CompulsoryModule;
import vn.com.courseman.it5.model.CourseModule;
import vn.com.courseman.it5.model.ElectiveModule;
import vn.com.courseman.it5.model.Enrolment;
import vn.com.courseman.it5.model.SClass;
import vn.com.courseman.it5.model.Student;
import vn.com.courseman.it5.model.reports.StudentsByNameReport;

/**
 * @overview 
 *  Encapsulate the basic functions for setting up and running a software given its domain model.  
 *  
 * @author dmle
 *
 * @version 
 */
public class CourseManSoftware {
  
  // the domain model of software
  private static final Class[] model = {
      CourseModule.class, 
      CompulsoryModule.class, 
      ElectiveModule.class, 
      Enrolment.class, 
      Student.class, 
      City.class, 
      SClass.class,
      // reports
      StudentsByNameReport.class
  };
  
  /**
   * @effects 
   *  create and run a UI-based {@link DomSoftware} for a pre-defined model. 
   */
  public static void main(String[] args){
    // 2. create UI software
    DomSoftware sw = SoftwareFactory.createUIDomSoftware();
    
    // 3. run
    // create in memory configuration
    System.setProperty("domainapp.setup.SerialiseConfiguration", "false");
    
    // 3. run it
    try {
      sw.run(model);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }   
  }
}
