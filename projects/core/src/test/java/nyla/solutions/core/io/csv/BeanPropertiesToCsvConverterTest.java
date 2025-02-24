package nyla.solutions.core.io.csv;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.creational.generator.SimpleObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanPropertiesToCsvConverterTest
{

	@Test
	public void testDoesNotHaveClassInConvert()
	{
		SimpleObject so = new JavaBeanGeneratorCreator<SimpleObject>(SimpleObject.class)
		.randomizeAll().create();
		
		
		BeanPropertiesToCsvConverter<SimpleObject> c= new BeanPropertiesToCsvConverter<SimpleObject>(SimpleObject.class);
		String out = c.convert(so);
		
		System.out.println("out:"+out);
		assertTrue(!out.contains(SimpleObject.class.getName()));
		
	}
	@Test
	public void testHeadMatchConvert()
	{
		SimpleObject so = new JavaBeanGeneratorCreator<SimpleObject>(SimpleObject.class)
		.randomizeAll().create();
		
		so.setFieldString("hello");
		BeanPropertiesToCsvHeaderConverter<SimpleObject> header= new BeanPropertiesToCsvHeaderConverter<SimpleObject>();
		
		BeanPropertiesToCsvConverter<SimpleObject> c= new BeanPropertiesToCsvConverter<SimpleObject>(SimpleObject.class);
		String out = c.convert(so);
		
		System.out.print("out:"+out);
		assertTrue(!out.contains(SimpleObject.class.getName()));
		
		String hOut = header.convert(so);
		System.out.print("hOut:"+hOut);
		
	}
}
