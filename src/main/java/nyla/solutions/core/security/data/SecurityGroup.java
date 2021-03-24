package nyla.solutions.core.security.data;

import nyla.solutions.core.patterns.expression.BooleanExpression;

import java.io.Serializable;
import java.security.Principal;
import java.util.*;

/**
 * <pre>
 * SecurityGroup provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class SecurityGroup
implements Principal, Serializable, BooleanExpression<Principal>
{
 
   public SecurityGroup(String aGroupName)
   {
      groupMembers = new HashSet<Principal>();
     
      if(aGroupName != null)
    	  aGroupName = aGroupName.trim().toUpperCase();
      
      
       name = aGroupName;
   }//--------------------------------------------
   public boolean addMember(Principal principal)
   {
       if(groupMembers.contains(principal))
           return false;

       groupMembers.add(principal);
       return true;
   }//--------------------------------------------
   public boolean removeMember(Principal principal)
   {
       return groupMembers.remove(principal);
   }//--------------------------------------------
  public Enumeration<Principal> members()
  {
       return Collections.enumeration(groupMembers);
  }//--------------------------------------------

  
  /* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj)
{
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (!SecurityGroup.class.isAssignableFrom(obj.getClass()))
		return false;
	
	SecurityGroup other = (SecurityGroup) obj;

	return this.name.equals(other.getName());

}
public String toString()
   {
       return name;
   }//--------------------------------------------
   /* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode()
{
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
}
   /**
    * 
    * Recursively look into groups for members
    * @param principal the principal to check 
    * @return true if the principal is a member of the group
    */
   public boolean isMember(Principal principal)
   {
       if(groupMembers.contains(principal))
       {
           return true;
       } 
       else
       {
           Vector<Principal> vector = new Vector<Principal>(10);
           return isMemberRecurse(principal, vector);
       }
   }//--------------------------------------------
   public String getName()
   {
       return name;
   }//--------------------------------------------
   private boolean isMemberRecurse(Principal principal, Vector<Principal> vector)
   {
       for(Enumeration<Principal> enumeration = members(); enumeration.hasMoreElements();)
       {
           boolean flag = false;
           Principal principal1 = (Principal)enumeration.nextElement();
           if(principal1.equals(principal))
               return true;
           if(principal1 instanceof SecurityGroup)
           {
              SecurityGroup groupimpl = (SecurityGroup)principal1;
               vector.addElement(this);
               if(!vector.contains(groupimpl))
                   flag = groupimpl.isMemberRecurse(principal, vector);
           } 
           else
           if(principal1 instanceof SecurityGroup)
           {
        	   SecurityGroup group1 = (SecurityGroup)principal1;
               if(!vector.contains(group1))
                   flag = group1.isMember(principal);
           }

           if(flag)
               return flag;
       }

       return false;
   }//--------------------------------------------
   @Override
   public Boolean apply(Principal obj)
   {
	   if (this == obj)
			return Boolean.TRUE;
		if (obj == null)
			return Boolean.FALSE;
		if (!SecurityGroup.class.isAssignableFrom(obj.getClass()))
			return Boolean.FALSE;
		
		SecurityGroup other = (SecurityGroup) obj;

		return Boolean.valueOf(this.name.equals(other.getName()));
   }

   private Set<Principal> groupMembers = null;
   private final String name;   
   static final long serialVersionUID = 1;
}
