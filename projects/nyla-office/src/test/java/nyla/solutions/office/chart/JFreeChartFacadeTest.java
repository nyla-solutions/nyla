package nyla.solutions.office.chart;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;

public class JFreeChartFacadeTest
{

   public JFreeChartFacadeTest(String name)
   {
	super(name);
   }

   protected void setUp() throws Exception
   {
	super.setUp();
   }// ----------------------------------------------

   @Test
   public void testGetBytes()
   throws Exception
   {
	   Chart chart =  new JFreeChartFacade();
	chart.setLegend(legend);
	
	chart.setCategoryLabel(categoryLabel);
	chart.setHeight(height);
	chart.setName(name);
	chart.setWidth(width);
	chart.setTitle(title);
	String label = "JVM";
	
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	
	cal.add(Calendar.DATE, -5);
	
	chart.plotValue(2,label, cal.getTime());
	cal.add(Calendar.DATE, +1);
	
	
	chart.plotValue(5, label, cal.getTime());
	cal.add(Calendar.DATE, +1);
	
	chart.plotValue(6,label, cal.getTime());
	
	cal.add(Calendar.DATE, +1);
	chart.plotValue(7,label, cal.getTime());

	cal.add(Calendar.DATE, +1);
	chart.plotValue(1,label, cal.getTime());
	
	/*
	 * 
	chart.plotValue(1,"c", new Integer(1));
	chart.plotValue(4,"c", new Integer(2));
	chart.plotValue(5,"c", new Integer(2));
	
	chart.plotValue(4,"b", new Integer(1));
	chart.plotValue(6,"b", new Integer(2));
	chart.plotValue(8,"b", new Integer(2));
	
	
	chart.plotValue(9,"e", new Integer(1));
	chart.plotValue(10,"e", new Integer(2));
	chart.plotValue(11,"e", new Integer(2));
	
	chart.plotValue(9,"f", new Integer(1));
	chart.plotValue(10,"f", new Integer(2));
	chart.plotValue(11,"f", new Integer(2));
	
	
	chart.plotValue(11,"g", new Integer(1));
	chart.plotValue(13,"g", new Integer(2));
	chart.plotValue(15,"g", new Integer(2));
	
	
	chart.plotValue(15,"h", new Integer(1));
	chart.plotValue(16,"h", new Integer(2));
	chart.plotValue(18,"h", new Integer(2));
	
	chart.plotValue(15,"h", new Integer(1));
	chart.plotValue(16,"h", new Integer(2));
	chart.plotValue(18,"h", new Integer(2));
	
	chart.plotValue(16,"i", new Integer(1));
	chart.plotValue(17,"i", new Integer(2));
	chart.plotValue(19,"i", new Integer(2));
	
	chart.plotValue(16,"j", new Integer(1));
	chart.plotValue(17,"j", new Integer(2));
	chart.plotValue(19,"j", new Integer(2));
	
	chart.plotValue(16,"k", new Integer(1));
	chart.plotValue(17,"k", new Integer(2));
	chart.plotValue(19,"k", new Integer(2));
	
	chart.plotValue(17,"l", new Integer(1));
	chart.plotValue(18,"l", new Integer(2));
	chart.plotValue(19,"l", new Integer(33));
	
	chart.plotValue(18,"m", new Integer(10));
	chart.plotValue(19,"m", new Integer(20));
	chart.plotValue(19,"m", new Integer(33));
	
	
	chart.plotValue(19,"n", new Integer(10));
	chart.plotValue(20,"n", new Integer(20));
	chart.plotValue(21,"n", new Integer(33));
	
	
	chart.plotValue(20,"o", new Integer(11));
	chart.plotValue(21,"o", new Integer(25));
	chart.plotValue(22,"o", new Integer(29));
	chart.plotValue(759,"ACD_-_AVAILABLE_CHEMICALS_DIRECTORY (2.947%)","Usage1");
	chart.plotValue(716,"CGRP (2.78%)","Usage");
	chart.plotValue(563,"MGLUR2 (2.186%)","Usage1");
	chart.plotValue(511,"PDE10 (1.984%)","Usage");
	chart.plotValue(421,"DGAT2_INHIBITOR (1.635%)","Usage1");
	chart.plotValue(418,"RNA_THERAPEUTICS (1.623%)","Usage");
	chart.plotValue(404,"SSTR3 (1.569%)","Usage");
	chart.plotValue(388,"CUSTOM_FRAGMENTS (1.507%)","Usage");
	chart.plotValue(382,"B3AR (1.483%)","Usage1");
	chart.plotValue(373,"CRTH2 (1.448%)","Usage");
	chart.plotValue(346,"SGC (1.343%)","Usage");
	chart.plotValue(339,"ROMK_BLOCKERS (1.316%)","Usage");
	chart.plotValue(337,"FAAH_INHIBITOR (1.309%)","Usage1");
	chart.plotValue(336,"DRUG_METABOLISM (1.305%)","Usage");
	chart.plotValue(324,"SERVICE_ASSAYS (1.258%)","Usage1");
	chart.plotValue(324,"MGLUR5_PAM (1.258%)","Usage1");
	*/
	/*chart.plotValue(308,"GPR119 (1.196%)","Usage");
	chart.plotValue(274,"TRPA1 (1.064%)","Usage");
	chart.plotValue(269,"SSTR5 (1.044%)","Usage");
	chart.plotValue(258,"CMET (1.002%)","Usage");
	chart.plotValue(256,"ASSAY_INFORMATION_BROWSER (0.994%)","Usage");
	chart.plotValue(242,"AMPK (0.94%)","Usage");
	chart.plotValue(225,"GPR7 (0.874%)","Usage");
	chart.plotValue(222,"HIV_RT (0.862%)","Usage");
	chart.plotValue(166,"MGLUR4 (0.645%)","Usage");
	chart.plotValue(164,"GSM (0.637%)","Usage");
	chart.plotValue(158,"HSD (0.613%)","Usage");
	chart.plotValue(149,"BETA_SECRETASE (0.579%)","Usage");
	chart.plotValue(139,"ASSAY_REGISTRATION_DETAIL (0.54%)","Usage");
	chart.plotValue(125,"DP_IV (0.485%)","Usage");
	chart.plotValue(124,"LRRK2 (0.481%)","Usage");
	chart.plotValue(112,"HTS_SCREENING_RESULTS (0.435%)","Usage");
	chart.plotValue(110,"OX2R (0.427%)","Usage");
	chart.plotValue(102,"HIV_PROTEASE (0.396%)","Usage");
	
	chart.plotValue(98,"HCV_PROTEASE (0.381%)","Usage");
	chart.plotValue(95,"P2X3 (0.369%)","Usage");
	chart.plotValue(78,"GLUCOCORTICOIDS (0.303%)","Usage");
	chart.plotValue(73,"PARP_INH_V0 (0.283%)","Usage");
	chart.plotValue(73,"ANPEP/LNPEP (0.283%)","Usage");
	chart.plotValue(72,"KINOME_PANEL (0.28%)","Usage");
	chart.plotValue(69,"CETP (0.268%)","Usage");
	chart.plotValue(69,"GLYT1 (0.268%)","Usage");
	chart.plotValue(60,"KAT1 (0.233%)","Usage");
	chart.plotValue(56,"HIV_INTEGRASE (0.217%)","Usage");
	chart.plotValue(55,"JAK3 (0.214%)","Usage");
	chart.plotValue(54,"HCV_POL_NI (0.21%)","Usage");
	chart.plotValue(52,"HIV_RNASE_H (0.202%)","Usage");
	chart.plotValue(47,"COMT (0.182%)","Usage");
	chart.plotValue(47,"PHYSICAL_CHEMISTRY (0.182%)","Usage");
	chart.plotValue(42,"GK (0.163%)","Usage");
	chart.plotValue(42,"PRCP (0.163%)","Usage");
	chart.plotValue(42,"PROJECT_REGISTRATION_DETAIL (0.163%)","Usage");
	chart.plotValue(42,"M1 (0.163%)","Usage");
	chart.plotValue(40,"CATHEPSINS (0.155%)","Usage");
	chart.plotValue(39,"BANYU_KINASE_PROJECT (0.151%)","Usage");
	chart.plotValue(39,"A1I (0.151%)","Usage");
	chart.plotValue(38,"EDG5 (0.148%)","Usage");
	chart.plotValue(37,"PDE2 (0.144%)","Usage");
	chart.plotValue(36,"PROJECT_INFORMATION_BROWSER (0.14%)","Usage");
	chart.plotValue(36,"CMC_-_COMPREHENSIVE_MEDICINAL_CHEMISTRY (0.14%)","Usage");
	chart.plotValue(35,"APOE_SC (0.136%)","Usage");
	chart.plotValue(32,"MDDR_-_MDL_DRUG_DATA_REPORT (0.124%)","Usage");
	chart.plotValue(32,"JAK (0.124%)","Usage");
	chart.plotValue(32,"HIF_PHD2 (0.124%)","Usage");
	chart.plotValue(31,"MC4R (0.12%)","Usage");
	chart.plotValue(30,"CDK5 (0.116%)","Usage");
	chart.plotValue(29,"TRAINING_APPLICATION (0.113%)","Usage");
	chart.plotValue(25,"AKT_KINASE (0.097%)","Usage");
	chart.plotValue(23,"GPR154 (0.089%)","Usage");
	chart.plotValue(22,"HEPC (0.085%)","Usage");
	chart.plotValue(21,"METABOLITE_BROWSER (0.082%)","Usage");
	chart.plotValue(20,"BOMBESIN (0.078%)","Usage");
	chart.plotValue(20,"NO-DIURETICS (0.078%)","Usage");
	chart.plotValue(19,"CANNABINOIDS (0.074%)","Usage");
	chart.plotValue(19,"APL (0.074%)","Usage");
	chart.plotValue(18,"GLUCAGON (0.07%)","Usage");
	chart.plotValue(18,"ARRHYTHMIA (0.07%)","Usage");
	chart.plotValue(18,"TEST_MDL_5.1_ECDSWRK (0.07%)","Usage");
	chart.plotValue(17,"OX2R_POT (0.066%)","Usage");
	chart.plotValue(17,"5HT (0.066%)","Usage");
	chart.plotValue(15,"FALERT_-_THERAPEUTIC_PATENT_FAST_ALERT (0.058%)","Usage");
	chart.plotValue(15,"SARMS (0.058%)","Usage");
	chart.plotValue(14,"BLA_INHIBITORS (0.054%)","Usage");
	chart.plotValue(13,"PANLAB (0.05%)","Usage");
	chart.plotValue(13,"PCSK9 (0.05%)","Usage");
	chart.plotValue(13,"DGAT (0.05%)","Usage");
	chart.plotValue(12,"BSRX_-_BIOSTER_DATABASE (0.047%)","Usage");
	chart.plotValue(12,"PN_CHANNEL (0.047%)","Usage");
	chart.plotValue(11,"ACC (0.043%)","Usage");
	chart.plotValue(10,"GPR131 (0.039%)","Usage");
	chart.plotValue(10,"KINASE_P38 (0.039%)","Usage");
	chart.plotValue(10,"MOGAT (0.039%)","Usage");
	chart.plotValue(10,"CP (0.039%)","Usage");
	chart.plotValue(10,"CAV22 (0.039%)","Usage");
	chart.plotValue(10,"TASK3 (0.039%)","Usage");
	chart.plotValue(10,"CANDIDATE_TRACKER (0.039%)","Usage");
	chart.plotValue(9,"ADRA2C (0.035%)","Usage");
	chart.plotValue(9,"SARCOPENIA (0.035%)","Usage");
	chart.plotValue(9,"PPAR (0.035%)","Usage");
	chart.plotValue(9,"X_VENDOR (0.035%)","Usage");
	chart.plotValue(9,"NEUROKININS (0.035%)","Usage");
	chart.plotValue(8,"BANYU_CHEM (0.031%)","Usage");
	chart.plotValue(8,"CCK1R (0.031%)","Usage");
	chart.plotValue(8,"NIACIN (0.031%)","Usage");
	chart.plotValue(8,"DUNDEE_KINASE (0.031%)","Usage");
	chart.plotValue(8,"GPR40 (0.031%)","Usage");
	chart.plotValue(8,"GPR120 (0.031%)","Usage");
	chart.plotValue(7,"MGLUR (0.027%)","Usage");
	chart.plotValue(7,"GHRELIN_ANTAGONIST (0.027%)","Usage");
	chart.plotValue(7,"BCRX_-_BIOCATALYSIS_DATABASE (0.027%)","Usage");
	chart.plotValue(7,"PDK1 (0.027%)","Usage");
	chart.plotValue(7,"SAFETY_ASSESSMENT (0.027%)","Usage");
	chart.plotValue(7,"NRF2 (0.027%)","Usage");
	chart.plotValue(6,"TOXICITY (0.023%)","Usage");
	chart.plotValue(6,"NO-ARB (0.023%)","Usage");
	chart.plotValue(6,"RNA_OPTIMIZATION (0.023%)","Usage");
	chart.plotValue(6,"HDAC_BOSTON (0.023%)","Usage");
	chart.plotValue(6,"TAU_MARK (0.023%)","Usage");
	chart.plotValue(6,"PKFILE (0.023%)","Usage");
	chart.plotValue(5,"ACC_SCREENING (0.019%)","Usage");
	chart.plotValue(5,"GPR103 (0.019%)","Usage");
	chart.plotValue(5,"UPSTATE_KINASE (0.019%)","Usage");
	chart.plotValue(4,"MOGAT_HTS (0.016%)","Usage");
	chart.plotValue(4,"PGRX_-_PROTECTING_GROUPS_DATABASE (0.016%)","Usage");
	chart.plotValue(4,"KINESINS (0.016%)","Usage");
	chart.plotValue(4,"HDAC_V0 (0.016%)","Usage");
	chart.plotValue(4,"TREK1 (0.016%)","Usage");
	chart.plotValue(4,"EL (0.016%)","Usage");
	chart.plotValue(4,"GRK4 (0.016%)","Usage");
	chart.plotValue(4,"KHK_INHIBITOR (0.016%)","Usage");
	chart.plotValue(3,"VR1 (0.012%)","Usage");
	chart.plotValue(3,"MDL6_BROWSE_ECDSCMP60 (0.012%)","Usage");
	chart.plotValue(3,"CCR2 (0.012%)","Usage");
	chart.plotValue(3,"ARG2 (0.012%)","Usage");
	chart.plotValue(3,"MDL6_BROWSE_ECDSPIC60 (0.012%)","Usage");
	chart.plotValue(3,"NR2B_BIND (0.012%)","Usage");
	chart.plotValue(3,"SARMS_MONITORING_BROWSER (0.012%)","Usage");
	chart.plotValue(3,"AHCY (0.012%)","Usage");
	chart.plotValue(3,"ACL (0.012%)","Usage");
	chart.plotValue(2,"PCSK9_AC (0.008%)","Usage");
	chart.plotValue(2,"ORL1 (0.008%)","Usage");
	chart.plotValue(2,"TTYPECA (0.008%)","Usage");
	chart.plotValue(2,"CB2 (0.008%)","Usage");
	chart.plotValue(2,"BANYU_NPY1 (0.008%)","Usage");
	chart.plotValue(2,"NMDA (0.008%)","Usage");
	chart.plotValue(2,"KINASE_KDR (0.008%)","Usage");
	chart.plotValue(2,"SGK1_INHIBITOR (0.008%)","Usage");
	chart.plotValue(2,"MGLUR8 (0.008%)","Usage");
	chart.plotValue(2,"NRF2_OXIDATIVE_STRESS (0.008%)","Usage");
	chart.plotValue(2,"SARCOPENIA_(HTS_TITRATION) (0.008%)","Usage");
	chart.plotValue(2,"BANYU_MD_HTS (0.008%)","Usage");
	chart.plotValue(2,"ASIC1 (0.008%)","Usage");
	chart.plotValue(2,"TAU_IMAGING (0.008%)","Usage");
	chart.plotValue(2,"CBS (0.008%)","Usage");
	chart.plotValue(2,"P24_SPREAD (0.008%)","Usage");
	chart.plotValue(1,"PAR2 (0.004%)","Usage");
	chart.plotValue(1,"TRAINING_APPLICATION_IRBM (0.004%)","Usage");
	chart.plotValue(1,"NPY_RY (0.004%)","Usage");
	chart.plotValue(1,"KV21 (0.004%)","Usage");
	chart.plotValue(1,"EZH2 (0.004%)","Usage");
	chart.plotValue(1,"BANYU_PHYSICALCHEMSITRY (0.004%)","Usage");
	chart.plotValue(1,"RY_EMPERIC_SCREEN (0.004%)","Usage");
	chart.plotValue(1,"CCR (0.004%)","Usage");
	chart.plotValue(1,"UBE2C (0.004%)","Usage");
	chart.plotValue(1,"ADAM (0.004%)","Usage");
	chart.plotValue(1,"P24_VIRAL_TITRATION (0.004%)","Usage");
	chart.plotValue(1,"CDK4 (0.004%)","Usage");
	chart.plotValue(1,"ASIC3 (0.004%)","Usage");
	chart.plotValue(1,"MCH (0.004%)","Usage");
	chart.plotValue(1,"NMUR (0.004%)","Usage");
	chart.plotValue(1,"P2Y6 (0.004%)","Usage");
	chart.plotValue(1,"S1PR_ANTAG (0.004%)","Usage");
	chart.plotValue(1,"C5AR (0.004%)","Usage");
	chart.plotValue(1,"NCI_AND_AIDS_-_NATIONAL_CANCER_INSTITUTE_DATABASES (0.004%)","Usage");
	chart.plotValue(1,"GPR81 (0.004%)","Usage");
	chart.plotValue(1,"OXM (0.004%)","Usage");
	chart.plotValue(1,"CHITINASE (0.004%)","Usage");
	chart.plotValue(1,"ME1 (0.004%)","Usage");
	chart.plotValue(1,"MAXI-K_AG (0.004%)","Usage");
	chart.plotValue(1,"GPR61 (0.004%)","Usage");
	chart.plotValue(1,"GPR120_NW_SCREENING (0.004%)","Usage");*/

	chart.setTypeName(Chart.PNG_TYPE_NAME);
	
	
	Color[] colors = { Color.RED};
	
	chart.setSeriesColors(Arrays.asList(colors));
	chart.setBackgroundColor(Color.WHITE);
	chart.setValueLabel("GB(s)");
	
	//chart.setGraphType(Chart.PIE_GRAPH_TYPE);
	//chart.setGraphType(Chart.PIE_3D_GRAPH_TYPE);
	//chart.setGraphType(Chart.LINE_GRAPH_TYPE);
	chart.setGraphType(Chart.AREA_GRAPH_TYPE);

	
	IO.writeFile(new File(filePath), chart.getBytes());
		
	Debugger.println("Write to "+filePath);
   }
   private String title = "Memory Usage";
   private boolean legend = true;
   private String filePath = Config.getTempDir()+"test.png";
   private String name = "name";
   private int height = 700;
   private int width =  600;
   private String categoryLabel = "Dates";


}
