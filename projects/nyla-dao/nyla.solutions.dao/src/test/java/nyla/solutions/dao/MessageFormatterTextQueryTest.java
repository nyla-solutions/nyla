package nyla.solutions.dao;

import java.text.MessageFormat;
import junit.framework.TestCase;

public class MessageFormatterTextQueryTest extends TestCase
{

	public MessageFormatterTextQueryTest(String name)
	{
		super(name);
	}
	
	public void testFormat() throws Exception
	{
		String sql = "select  stc.SOURCETYPENAME, 		\n	stc.SUBSTANCETYPENAME, 			 psc.PHYSICALSTATENAME, 			acd_mfcd.SUBSTANCENAME MDLNUMBER, 			        acd_common_name.SUBSTANCENAME NAME, 			        acd_cas.SUBSTANCENAME CASNUM, 			        mcidb.SUBSTANCENAME COMPOUND_ID, 			        o.DATABASEID RESPONSIBLEUSER,esc.EDITSTATUSNAME, 			        s.* 			from substance s, 			SUBSTANCETYPECODES stc, org o, EDITSTATUSCODES esc, 			PHYSICALSTATECODES  psc, 			SOURCETYPECODES stc, 			(select ss.SYNONYMTYPEID, ss.SUBSTANCENAME, ss.substanceid 			from SYNONYMTYPECODES stc, SUBSTANCESYNONYM ss 			where stc.SYNONYMTYPENAME = MDL# 			   and ss.SYNONYMTYPEID = stc.SYNONYMTYPEID) acd_mfcd, 			(select ss.SYNONYMTYPEID, ss.SUBSTANCENAME, ss.substanceid 			from SYNONYMTYPECODES stc, SUBSTANCESYNONYM ss 			where stc.SYNONYMTYPENAME = Common Name 			  and stc.SYNONYMTYPEID = ss.SYNONYMTYPEID) acd_common_name, 			(select ss.SYNONYMTYPEID, ss.SUBSTANCENAME, ss.substanceid 			from SYNONYMTYPECODES stc, SUBSTANCESYNONYM ss 			where stc.SYNONYMTYPENAME = CAS 			    and stc.SYNONYMTYPEID = ss.SYNONYMTYPEID) acd_cas, 			(select ss.SYNONYMTYPEID, ss.SUBSTANCENAME, ss.substanceid 			from SYNONYMTYPECODES stc, SUBSTANCESYNONYM ss 			where stc.SYNONYMTYPENAME = Customer Compound ID 			    and stc.SYNONYMTYPEID = ss.SYNONYMTYPEID 			    and ss.SUBSTANCENAME like L-%) mcidb 			where  s.SUBSTANCETYPEID = stc.SUBSTANCETYPEID 			and s.SOURCETYPEID = stc.SOURCETYPEID 			and s.RESPONSIBLEID = o.ORGID 			and s.EDITSTATUSID = esc.EDITSTATUSID 			and s.PHYSICALSTATEID = psc.PHYSICALSTATEID 			and s.substanceid in 			(select sl.substanceid 			from SUBSTANCELOT sl, CONTAINEROFSUBSTANCELOT csl, 			container c, rooms r, sites s, 			CONTAINERSTATUSCODES csc 			where s.name in (KENILWORTH, RYN, BMB , WPP) 			and csc.CONTAINERSTATUSNAME = IN USE 			and csc.ACTIVE = Y 			and c.useroomid = r.roomid 			and r.siteid = s.siteid 			and sl.SUBSTANCELOTID = csl.SUBSTANCELOTID 			and csl.CONTAINERID = c.containerid 			and c.CONTAINERSTATUSID  = csc.CONTAINERSTATUSID) 			and s.substanceid = acd_mfcd.substanceid (+) 			      and s.substanceid = acd_common_name.substanceid (+) 			       and s.substanceid = acd_cas.substanceid(+) 			       and s.substanceid =  mcidb.substanceid(+) 			    \r\n   and s.lastmoddt > TO_DATE(''{0}'',MM/DD/YYYY)";
		
		String result = MessageFormat.format(sql, "3/2/2202");
		
		//Debugger.println("results=",result);
		
		assertTrue(result.indexOf("3/2/2202") > 0);
		
		Object[] inputes = {"TODO"};
        result = MessageFormat.format(sql, inputes);
		
        assertTrue(result.indexOf("TODO") > 0);
        

		
	}

}
