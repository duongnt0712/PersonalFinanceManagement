package vn.com.courseman.it4.model;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;

/**
 * Represents an elective module (a subclass of Module)
 * @author dmle
 *
 */
@DClass(schema="courseman")
public class ElectiveModule extends CourseModule {
  // extra attribute of elective module
  @DAttr(name="deptName",type=Type.String,length=50,optional=false)
  private String deptName;
  
  // constructor method
  // the order of the arguments must be this: 
  // - super-class arguments first, then sub-class
//  @DOpt(type=DOpt.Type.ObjectFormConstructor)
//  public ElectiveModule(@AttrRef("name") String name, 
//      @AttrRef("semester") int semester, @AttrRef("credits") int credits, 
//      @AttrRef("deptName") String deptName) {
//    this(null, null, name, semester, credits, deptName);
//  }
  
  // the order of the arguments must be this: 
  // - super-class arguments first, then sub-class
  @DOpt(type=DOpt.Type.ObjectFormConstructor)
  public ElectiveModule(
      @AttrRef("deptName") String deptName,
      @AttrRef("name") String name, 
      @AttrRef("semester") Integer semester, @AttrRef("credits") Integer credits 
      ) {
    this(null, null, name, semester, credits, deptName);
  }
  
  @DOpt(type=DOpt.Type.DataSourceConstructor)
  public ElectiveModule(Integer id, String code, String name, Integer semester, Integer credits, String deptName) {
    super(id, code,name,semester,credits);
    this.deptName = deptName;
  }
  
  // setter method 
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }
  
  // getter method
  public String getDeptName() {
    return deptName;
  }
}
