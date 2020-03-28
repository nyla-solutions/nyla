package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.data.Copier;
import nyla.solutions.core.data.NumberedProperty;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Debugger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JavaBeanGeneratorCreatorTest
{
	@Test
	public void testRandomAll()
	{
		JavaBeanGeneratorCreator<SimpleObject> creator = 
		new JavaBeanGeneratorCreator<SimpleObject>(SimpleObject.class).randomizeAll();
		
		assertTrue(creator.getRandomizeProperties().contains("fieldString"));
		
		SimpleObject so = creator.create();
		
		assertTrue(so.getFieldString().length() > 0);
	}
	@Test
	public void testFixedProperties()
	{
		JavaBeanGeneratorCreator<UserProfile> creator = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class);
		creator.fixedProperties("email");
		
		assertTrue(creator.getRandomizeProperties().contains("firstName"));
		
	}
	/**
	 * Test generator
	 */
	@Test
	public void testCreateString()
	{
		
		JavaBeanGeneratorCreator<UserProfile> creator = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class);
		creator.randomizeProperty("email");
		
		UserProfile userProfile = creator.create();
		assertNotNull(userProfile);
		
		assertTrue(userProfile.getEmail() != null && userProfile.getEmail().length() > 0 );
		
	}//------------------------------------------------
	/**
	 * Test generator
	 */
	@Test
	public void testCreateInt()
	{
		
		JavaBeanGeneratorCreator<NumberedProperty> creator = new JavaBeanGeneratorCreator<NumberedProperty>(NumberedProperty.class);
		creator.randomizeProperty("number");
		
		NumberedProperty numberedProperty = creator.create();
		assertNotNull(numberedProperty);
		
		assertTrue(numberedProperty.getValue() != null && numberedProperty.getNumber() > 0);
	}//------------------------------------------------
	
	/**
	 * Test generator
	 */
	@Test
	public void testCreateLong()
	{
		JavaBeanGeneratorCreator<LongObject> creator = new JavaBeanGeneratorCreator<LongObject>(LongObject.class);
		creator.randomizeProperty("id");
		creator.randomizeProperty("longId");
		
		LongObject lo = creator.create();
		
		assertNotNull(lo);
		assertTrue(lo.getId() > 0);
		assertNotNull(lo.getLongId());
		
		LongObject lo2 = creator.create();
		assertNotEquals(lo.getId(), lo2.getId());
		assertNotEquals(lo.getLongId(), lo2.getLongId());
	}
	
	@Test
	public void testCreateProtoype()
	{
		UserProfile protoype = new UserProfile();
		protoype.setFirstName("Imani");
		protoype.setLastName("Green");
		
		JavaBeanGeneratorCreator<UserProfile> creator = new JavaBeanGeneratorCreator<UserProfile>(protoype);
		
		creator.randomizeProperty("email");
		
		UserProfile u1 = creator.create();
		assertEquals(u1.getFirstName(),protoype.getFirstName());
		assertEquals(u1.getLastName(),protoype.getLastName());
		assertNotNull(u1.getEmail());
		assertTrue(u1.getEmail().length() > 0);
	}
	@Test
	public void testQABean()
	{
	
			JavaBeanGeneratorCreator<GeneratorQABean> creator = new JavaBeanGeneratorCreator<GeneratorQABean>(GeneratorQABean.class);
			creator.randomizeAll();
			
			GeneratorQABean bean = creator.create();
			assertNotNull(bean);
			assertNotNull(bean.getS1());

		
	}

	@Test
	public void test_complex_Objects()
	{
		JavaBeanGeneratorCreator<ComplexObject> creator = new JavaBeanGeneratorCreator<ComplexObject>(ComplexObject.class);
		creator.randomizeAll();

		ComplexObject bean = creator.create();
		assertNotNull(bean);
		assertNotNull(bean.getSimpleObject());
		assertNotNull(bean.getUserProfile());

		Debugger.dump(bean);

	}

	@Test
	public void test_nested_function_creation()
	{

		JavaBeanGeneratorCreator<ComplexObject> creator = new
				JavaBeanGeneratorCreator<ComplexObject>(ComplexObject.class)
				.randomizeAll()
				.generateNestedClass(UserProfile.class);

		ComplexObject complexObject = creator.create();

		System.out.println("obj:"+complexObject);

		assertNotNull(complexObject);

		assertNotNull(complexObject.getUserProfile());
		assertTrue(
				complexObject.getUserProfile().getEmail() != null&&
				complexObject.getUserProfile().getEmail().length() > 0);

	}


	@Test
	public void test_object_with_set()
	{
		JavaBeanGeneratorCreator<ClassWithSet> creator = new JavaBeanGeneratorCreator<>(ClassWithSet.class)
				.randomizeAll();

		ClassWithSet cws = creator.create();

		assertTrue(cws.getSet() != null);
	}

	@Test
	public void test_object_with_no_setter()
	{

		JavaBeanGeneratorCreator<ClassWithNoSetter> generatorCreator
				= new JavaBeanGeneratorCreator<>(ClassWithNoSetter.class)
				.randomizeAll();

		ClassWithNoSetter classWithNoSetter = generatorCreator.create();
		Debugger.dump(classWithNoSetter);
		assertNotNull(classWithNoSetter);
	}

	public static class QaObjectNeededWithFactory{
		private ObjectNeededWithFactory objectNeededWithFactory;
		private String test;

		public ObjectNeededWithFactory getObjectNeededWithFactory()
		{
			return objectNeededWithFactory;
		}

		public void setObjectNeededWithFactory(ObjectNeededWithFactory objectNeededWithFactory)
		{
			this.objectNeededWithFactory = objectNeededWithFactory;
		}
	}//-------------------------------------------

	public static class ObjWithEnum
	{
		enum Level {
			LOW,
			MEDIUM,
			HIGH
		};

		private Level myLevel;

		public Level getMyLevel()
		{
			return myLevel;
		}

		public void setMyLevel(Level myLevel)
		{
			this.myLevel = myLevel;
		}
	}

	@Test
	public void test_ObjectWithEnum()
	{
		ObjWithEnum obj = new JavaBeanGeneratorCreator<ObjWithEnum>(ObjWithEnum.class)
				.randomizeAll()
				.create();


		assertEquals(ObjWithEnum.Level.LOW,obj.getMyLevel());

	}

	@Test
	public void test_ObjectNeededWithFactory()
	{
		Creator<ObjectNeededWithFactory> creator = () -> { return null;};

		JavaBeanGeneratorCreator<QaObjectNeededWithFactory> generatorCreator
				= new JavaBeanGeneratorCreator<>(QaObjectNeededWithFactory.class)
				.randomizeAll()
				.generateNestedAll()
				.creatorForClass(ObjectNeededWithFactory.class,creator);


		assertNotNull(generatorCreator.create());
	}

	@Test
	public void test_nested_function_creation_all()
	{

		JavaBeanGeneratorCreator<ComplexObject> creator = new
				JavaBeanGeneratorCreator<ComplexObject>(ComplexObject.class)
				.randomizeAll()
				.generateNestedAll();

		ComplexObject complexObject = creator.create();

		System.out.println("obj:"+complexObject);

		assertNotNull(complexObject);

		assertNotNull(complexObject.getUserProfile());
		assertTrue(
				complexObject.getUserProfile().getEmail() != null&&
						complexObject.getUserProfile().getEmail().length() > 0);

	}

	@Test
	public void test_cloneForClass()
	{
		JavaBeanGeneratorCreator<SimpleObject> creator = new JavaBeanGeneratorCreator<>(SimpleObject.class);
		creator.randomizeAll();

		JavaBeanGeneratorCreator<UserProfile> nestedCreator = creator.cloneForClass(UserProfile.class);

		UserProfile userProfile = (UserProfile)nestedCreator.create();

		assertTrue(userProfile.getEmail() != null && userProfile.getEmail().length() > 0);

	}

	@Test
	public void test_generate_interface()
	{
		assertNull(ClassPath.newInstance(Copier.class));
	}

	public static class LongObject
	{
		
		/**
		 * @return the id
		 */
		public long getId()
		{
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(long id)
		{
			this.id = id;
		}

		private long id;
		
		
		/**
		 * @return the longId
		 */
		public Long getLongId()
		{
			return longId;
		}

		/**
		 * @param longId the longId to set
		 */
		public void setLongId(Long longId)
		{
			this.longId = longId;
		}

		private Long longId;
	}

}
