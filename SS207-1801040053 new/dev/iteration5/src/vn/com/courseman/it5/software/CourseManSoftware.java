package vn.com.courseman.it5.software;

import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.software.DomainAppToolSoftware;
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
public class CourseManSoftware extends DomainAppToolSoftware {
  
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
  
  /* (non-Javadoc)
   * @see vn.com.courseman.software.Software#getModel()
   */
  /**
   * @effects 
   *  return {@link #model}.
   */
  @Override
  protected Class[] getModel() {
    return model;
  }

  /**
   * The main method
   * @effects 
   *  run software with a command specified in args[0] and with the model 
   *  specified by {@link #getModel()}. 
   *  
   *  <br>Throws NotPossibleException if failed for some reasons.
   */
  public static void main(String[] args) throws NotPossibleException {
    new CourseManSoftware().exec(args);
  }
}
