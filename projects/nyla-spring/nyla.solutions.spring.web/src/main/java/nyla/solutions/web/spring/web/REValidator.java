package nyla.solutions.web.spring.web;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import nyla.solutions.global.util.Debugger;
import nyla.solutions.global.util.JavaBean;
import nyla.solutions.global.util.Text;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <pre>
 * REValidator provides a set of functions to validate beans based on regular expression.
 * 
 * Sample Spring XML Usage
 * 
 *  <bean id="statusTestValidator"
        class="org.solutions.ipresentation.spring.web.REValidator">
        <property name="rules">
            <map>
                <entry>
                    <key>
                        <value>name</value>
                    </key>
                    <value>[a-z]*</value>
                </entry>    
            </map>
        </property>
    </bean>
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class REValidator implements Validator
{
   /**
    * 
    * @see org.springframework.validation.Validator#supports(java.lang.Class)
    */
   public boolean supports(Class clazz)
   {
      if(clazz == null)
         return false;
      
      Method[] methods = clazz.getDeclaredMethods();
      
      return methods != null && methods.length > 0;
   }// --------------------------------------------

   /**
    * Validate target based on regular expressions.
    * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
    */
   public void validate(Object target, Errors errors)
   {    
      if(target == null || this.rules == null || this.rules.isEmpty())
         return; //nothing to validate
      
      try
      {
         //loop thru bean properties
         Map beanMap = JavaBean.toMap(target);
         
         if(beanMap == null || beanMap.isEmpty())
            return; //  nothing to validate
         
         String propertyName = null;
         Object value = null;
         String ruleRE = null;
         for (Iterator i = beanMap.keySet().iterator(); i.hasNext();)
         {
            propertyName = (String) i.next();
            
            //get rule  regular expression for property name
            ruleRE = (String)this.rules.get(propertyName);
            
            if(ruleRE == null || ruleRE.length() == 0)
               continue; //skip
            
            //test if value matches regular expresison
            value = beanMap.get(propertyName);
            
            //Debugger.println(this, "testing ruleRE="+ruleRE+" propertyName="+propertyName+" value="+value);
            if(!Text.matches(ruleRE,String.valueOf(value)))
            {
               Object[] values = {value }; 

               //Debugger.println(this, "not matches ruleRE="+ruleRE+" value"+value);
               errors.rejectValue(propertyName, IllegalArgumentException.class.getName(), values, "");
            }               
         }         
      }
      catch(PatternSyntaxException e)
      {
         throw e;
      }
      catch(Exception e)
      {
         Debugger.printError(e);
         errors.reject(iSpring.DEFAULT_ERROR_CODE, e.getMessage());
      }
   }// --------------------------------------------

   /**
    * @return the rules
    */
   public final Map getRules()
   {
      return rules;
   }// --------------------------------------------
   /**
    * @param rules the rules to set. The keyt is the bean name and the value is te regular expression.
    * 
    */
   public final void setRules(Map rules)
   {   
      this.rules = rules;
   }// --------------------------------------------

   
   private Map rules = null;   
}
