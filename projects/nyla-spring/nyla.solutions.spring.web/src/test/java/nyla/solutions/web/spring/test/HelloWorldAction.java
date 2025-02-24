package nyla.solutions.web.spring.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nyla.solutions.web.spring.web.AbstractDispatchAction;

import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 * HelloWorldControlder controller to print hello world;
 * 
 * Usage URL: http://<server>:<port>/iSpring/hello.action?method=sayHello
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class HelloWorldAction extends AbstractDispatchAction
{
   /**
    * Display helloword
    * 
    * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   public ModelAndView sayHello(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
   throws Exception
   {
      System.out.println("Hello World");
      ModelAndView mv = new ModelAndView("hello");
      mv.addObject("message", "Hello World!");
      return mv;
   }// --------------------------------------------


}
