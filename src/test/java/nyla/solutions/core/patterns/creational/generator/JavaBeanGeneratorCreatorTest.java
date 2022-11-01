package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.data.Copier;
import nyla.solutions.core.data.NumberedProperty;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Junit test for JavaBeanGeneratorCreator
 * @author gregory green
 */
public class JavaBeanGeneratorCreatorTest
{

	@Test
	void of()
	{
		assertNotNull(JavaBeanGeneratorCreator.of(SimpleObject.class));
	}

	@Test
	void createCollection()
	{
		JavaBeanGeneratorCreator<SimpleObject> subject =
				new JavaBeanGeneratorCreator<SimpleObject>(SimpleObject.class).randomizeAll();

		int expected = 2;
		Collection<SimpleObject> actual = subject.createCollection(expected);
		assertNotNull(actual);
		assertThat(actual).size().isEqualTo(expected);
	}

	@Test
	void createCollection_whenCountLess1_ThenReturnEmpty()
	{
		JavaBeanGeneratorCreator<SimpleObject> subject =
				new JavaBeanGeneratorCreator<SimpleObject>(SimpleObject.class).randomizeAll();
		assertThat(subject.createCollection(0)).isEmpty();
		assertThat(subject.createCollection(-1)).isEmpty();;
	}

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
		assertNotNull(bean.getName());

		
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
	void given_record_When_create_Then_populate_fields() {
		var actual = new JavaBeanGeneratorCreator<>(TestRecord.class).create();

		System.out.println(actual);
		assertThat(actual).isNotNull();
		assertThat(actual.firstName()).isNotEmpty();
		assertThat(actual.LastName()).isNotEmpty();
	}

	@Test
	void given_record_When_createWithGenerateAll_Then_populate_fields() {
		var actual = new JavaBeanGeneratorCreator<>(TestRecord.class).generateNestedAll().create();

		System.out.println(actual);
		assertThat(actual.nestedRecord()).isNotNull();
		assertThat(actual.nestedRecord().email()).isNotEmpty();
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

	@Nested
	public class Given_ObjectWithStringProperty
	{
		ObjectWithStringProperty objectWithStringProperty;
		JavaBeanGeneratorCreator<ObjectWithStringProperty> subject;

		@BeforeEach
		public void setUp()
		{
			subject = new JavaBeanGeneratorCreator<>(ObjectWithStringProperty.class)
					.setTextDateFormat(DateTimeFormatter.ISO_DATE)
					.randomizeAll();

			objectWithStringProperty = subject.create();
		}

		 public class ObjectWithStringProperty
		{
			private String billId;
			private String id;
			private String acctid;
			private String todayDate;

			public String getTodayDate()
			{
				return todayDate;
			}

			public void setTodayDate(String todayDate)
			{
				this.todayDate = todayDate;
			}

			public String getBillId()
			{
				return billId;
			}


			public void setBillId(String billId)
			{
				this.billId = billId;
			}

			public String getId()
			{
				return id;
			}

			public void setId(String id)
			{
				this.id = id;
			}

			public String getAcctid()
			{
				return acctid;
			}

			public void setAcctid(String acctid)
			{
				this.acctid = acctid;
			}

			@Override
			public String toString()
			{
				final StringBuilder sb = new StringBuilder("ObjectWithStringProperty{");
				sb.append("billId='").append(billId).append('\'');
				sb.append(", id='").append(id).append('\'');
				sb.append(", acctid='").append(acctid).append('\'');
				sb.append('}');
				return sb.toString();
			}
		}

		@Nested
		public class When_ObjectProperty_Contains_Date
		{
			@Test
			public void then_id_contains_date()
			{
				String date = objectWithStringProperty.getTodayDate();
				assertNotNull(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
			}
		}
		
		@Nested
		public class When_Property_Contains_Id
		{

			@BeforeEach
			public void setUp()
			{
				subject = new JavaBeanGeneratorCreator<>(ObjectWithStringProperty.class);
				subject.randomizeAll();
			}
			@Test
			public void test_determineId_is_IdCreator()
			{
				String propertyName = "billid";
				PropertyDescriptor propertyDescriptor = mock(PropertyDescriptor.class);
				when(propertyDescriptor.getName()).thenReturn(propertyName);
				Creator<?> creator = subject.determineCreator(String.class,propertyDescriptor);

				assertThat(creator).isInstanceOf(IdCreator.class);

				assertNotNull(subject.getCreatorForClassMap().get(String.class.getName()+".billid"));
			}

			@Test
			public void test_determineId_fax_is_PhoneNumberCreator()
			{
				validateCreator("fax",PhoneNumberCreator.class);
			}
			@Test
			public void test_determineId_phone_is_PhoneNumberCreator()
			{
				validateCreator( "phone",PhoneNumberCreator.class);
				validateCreator( "mobile",PhoneNumberCreator.class);
				validateCreator( "cellPhone",PhoneNumberCreator.class);
				validateCreator( "Telephone",PhoneNumberCreator.class);
				validateCreator( "primaryPhone",PhoneNumberCreator.class);
				validateCreator( "workPhone",PhoneNumberCreator.class);
			}

			@Test
			public void test_determineId_email_is_EmailCreatorCreator()
			{
				validateCreator( "email",EmailCreator.class);
				validateCreator( "eMail",EmailCreator.class);
			}

			@Test
			public void test_name_email_is_FullNameCreator()
			{
				validateCreator( "name",FullNameCreator.class);
				validateCreator( "customer",FullNameCreator.class);
				validateCreator( "fullName",FullNameCreator.class);
			}

			private void validateCreator(String propertyName,Class<?> clz)
			{
				Creator<?> creator;
				PropertyDescriptor cellPropertyDescriptor = mock(PropertyDescriptor.class);
				when(cellPropertyDescriptor.getName()).thenReturn(propertyName);
				creator = subject.determineCreator(String.class,cellPropertyDescriptor);
				assertThat(creator).isInstanceOf(clz);
			}

			@Test
			public void test_determineId_is_FirstNameCreator()
			{
				String propertyName = "myfirstName";
				PropertyDescriptor propertyDescriptor = mock(PropertyDescriptor.class);
				when(propertyDescriptor.getName()).thenReturn(propertyName);
				Creator<?> creator = subject.determineCreator(String.class,propertyDescriptor);

				assertThat(creator).isInstanceOf(FirstNameCreator.class);

				assertNotNull(subject.getCreatorForClassMap().get(String.class.getName()+"."+propertyName.toLowerCase()));
			}

			@Test
			public void test_UserProfile()
			{
				UserProfile userProfile  = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class)
						.randomizeAll().create();
				System.out.println(userProfile);

			}
			@Test
			public void test_determineId_is_LastNameCreator()
			{
				String propertyName = "mylastName";
				PropertyDescriptor propertyDescriptor = mock(PropertyDescriptor.class);
				when(propertyDescriptor.getName()).thenReturn(propertyName);
				Creator<?> creator = subject.determineCreator(String.class,propertyDescriptor);

				assertThat(creator).isInstanceOf(LastNameCreator.class);

				assertNotNull(subject.getCreatorForClassMap().get(String.class.getName()+"."+propertyName.toLowerCase()));
			}

			@Test
			public void then_GenerateIdId_IsInteger()
			{

				objectWithStringProperty = subject.create();
				System.out.println(objectWithStringProperty);
				assertTrue(Text.isInteger(objectWithStringProperty.getAcctid()));
				assertTrue(Text.isInteger(objectWithStringProperty.getBillId()));
				assertTrue(Text.isInteger(objectWithStringProperty.getId()));
			}

		}

		@Test
		public void test_determine_Dates()
		{
			PropertyDescriptor propertyDescriptor = mock(PropertyDescriptor.class);
			when(propertyDescriptor.getName()).thenReturn("todayDate");
			assertNotNull(subject.determineCreator(String.class,propertyDescriptor));
			assertNotNull(subject.determineCreator(Date.class,propertyDescriptor));
			assertNotNull(subject.determineCreator(java.sql.Date.class,propertyDescriptor));
			assertNotNull(subject.determineCreator(LocalDate.class,propertyDescriptor));
			assertNotNull(subject.determineCreator(LocalDateTime.class,propertyDescriptor));
			assertNotNull(subject.determineCreator(Timestamp.class,propertyDescriptor));
		}

	}

}
