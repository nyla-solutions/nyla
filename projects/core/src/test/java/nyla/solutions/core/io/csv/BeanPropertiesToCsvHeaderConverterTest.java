package nyla.solutions.core.io.csv;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.creational.generator.SimpleObject;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanPropertiesToCsvHeaderConverterTest
{

	@Test
	public void testConvert()
	{
		SimpleObject so = new JavaBeanGeneratorCreator<SimpleObject>(SimpleObject.class)
		.randomizeAll().create();

		BeanPropertiesToCsvHeaderConverter<SimpleObject> header= new BeanPropertiesToCsvHeaderConverter<SimpleObject>();
		
		String out = header.convert(so);
		System.out.println("out:"+out);
	
		assertTrue(!out.contains("class"));
		
		
		
	}
	@Test
	public void testNumber() throws Exception
	{

		assertEquals("Integer\n",new BeanPropertiesToCsvHeaderConverter<Integer>().convert(1));
		assertEquals("Float\n",new BeanPropertiesToCsvHeaderConverter<Float>().convert((float)0.3));
		assertEquals("Double\n",new BeanPropertiesToCsvHeaderConverter<Double>().convert(0.3));
		assertEquals("Long\n",new BeanPropertiesToCsvHeaderConverter<Long>().convert(Long.valueOf(1)));
	}
	@Test
	public void testTimes() throws Exception
	{
		assertEquals("Date\n",new BeanPropertiesToCsvHeaderConverter<Date>().convert(Calendar.getInstance().getTime()));
	}
	@Test
	public void testString() throws Exception
	{

		assertEquals("String\n",new BeanPropertiesToCsvHeaderConverter<String>().convert(""));
	}

}
