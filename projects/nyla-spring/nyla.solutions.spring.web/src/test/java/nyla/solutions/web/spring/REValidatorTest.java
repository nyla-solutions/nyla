package nyla.solutions.web.spring;


import junit.framework.Assert;
import junit.framework.TestCase;
import nyla.solutions.global.data.Status;
import nyla.solutions.global.patterns.servicefactory.ServiceFactory;
import nyla.solutions.global.util.Debugger;
import nyla.solutions.global.util.Text;
import nyla.solutions.web.spring.test.MockErrors;
import nyla.solutions.web.spring.web.REValidator;


/**
 * <pre>
 * REValidatorTest provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class REValidatorTest 
//extends TestCase
{

   public REValidatorTest(String aTest)
   {
      //super(aTest);
   }// --------------------------------------------

   /**
    * Test method for {@link nyla.solutions.web.spring.web.REValidator#validate(java.lang.Object, org.springframework.validation.Errors)}.
    */
   public void ignoreValidate()
   {
      ServiceFactory factory = ServiceFactory.getInstance();
      
      REValidator validator = (REValidator)factory.create("statusTestValidator");
      
      Assert.assertNotNull(validator);
      
      Status status = new Status();
      status.setName("1234");
      status.setValue("1212");
      
      
      MockErrors errors = new MockErrors();
      Assert.assertTrue(errors.getMap().isEmpty());
      Assert.assertTrue(validator.supports(Status.class));
      
      validator.validate(status, errors);
      
      Assert.assertTrue( Text.matches("[0-9]*", "123"));
      
      Assert.assertTrue(!Text.matches("[0-9]*", "123a"));
      
      Assert.assertTrue(!Text.matches("[a-z]*", "123a"));
      Assert.assertTrue(Text.matches("[0-9][a-z]*", "1ass"));
      
      Assert.assertTrue(errors.getMap().toString(), !errors.getMap().isEmpty());
      
      Debugger.println("error="+errors.getMap());
      
   }// --------------------------------------------


}
