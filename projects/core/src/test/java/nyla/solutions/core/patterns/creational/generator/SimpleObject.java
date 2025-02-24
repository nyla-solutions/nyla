package nyla.solutions.core.patterns.creational.generator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;


public class SimpleObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2702741187335693984L;

	enum SimpleEnum {
		ENUM1,
		ENUM2
	}
	
	
	
	/**
	 * @return the simpleEnum
	 */
	public SimpleEnum getSimpleEnum()
	{
		return simpleEnum;
	}
	/**
	 * @param simpleEnum the simpleEnum to set
	 */
	public void setSimpleEnum(SimpleEnum simpleEnum)
	{
		this.simpleEnum = simpleEnum;
	}
	/**
	 * @return the fieldSqlDate
	 */
	public java.sql.Date getFieldSqlDate()
	{
		if(fieldSqlDate == null)
			return null;
		
		return new java.sql.Date(this.fieldSqlDate.getTime());
	}
	
	/**
	 * @param fieldSqlDate the fieldSqlDate to set
	 */
	public void setFieldSqlDate(java.sql.Date fieldSqlDate)
	{
		if(fieldSqlDate == null)
			this.fieldSqlDate  = null;
		else
			this.fieldSqlDate = new java.sql.Date(fieldSqlDate.getTime());
	}
	
	/**
	 * @return the fieldDate
	 */
	public Date getFieldDate()
	{
		if(fieldDate == null)
			return null;
		
		return new Date(fieldDate.getTime());
	}
	
	/**
	 * @param fieldDate the fieldDate to set
	 */
	public void setFieldDate(Date fieldDate)
	{
		if(fieldDate == null)
			return;
		else
			this.fieldDate = new Date(fieldDate.getTime());
	}//------------------------------------------------
	/**
	 * @return the fieldTime
	 */
	public Time getFieldTime()
	{
		return fieldTime;
	}
	/**
	 * @param fieldTime the fieldTime to set
	 */
	public void setFieldTime(Time fieldTime)
	{
		this.fieldTime = fieldTime;
	}
	/**
	 * @return the fieldTimestamp
	 */
	public Timestamp getFieldTimestamp()
	{
		if(fieldTimestamp == null)
			return null;
		
		return new Timestamp(fieldTimestamp.getTime());
	}
	/**
	 * @param fieldTimestamp the fieldTimestamp to set
	 */
	public void setFieldTimestamp(Timestamp fieldTimestamp)
	{
		if(fieldTimestamp == null)
			this.fieldTimestamp = null;
		else
			this.fieldTimestamp = new Timestamp(fieldTimestamp.getTime());
	}
	
	/**
	 * @return the fieldCalendar
	 */
	public Calendar getFieldCalendar()
	{
		return fieldCalendar;
	}
	/**
	 * @param fieldCalendar the fieldCalendar to set
	 */
	public void setFieldCalendar(Calendar fieldCalendar)
	{
		this.fieldCalendar = fieldCalendar;
	}
	/**
	 * @return the fieldLongObject
	 */
	public Long getFieldLongObject()
	{
		return fieldLongObject;
	}
	/**
	 * @param fieldLongObject the fieldLongObject to set
	 */
	public void setFieldLongObject(Long fieldLongObject)
	{
		this.fieldLongObject = fieldLongObject;
	}
	/**
	 * @return the fieldLong
	 */
	public long getFieldLong()
	{
		return fieldLong;
	}
	/**
	 * @param fieldLong the fieldLong to set
	 */
	public void setFieldLong(long fieldLong)
	{
		this.fieldLong = fieldLong;
	}
	/**
	 * @return the fieldDouble
	 */
	public double getFieldDouble()
	{
		return fieldDouble;
	}
	/**
	 * @param fieldDouble the fieldDouble to set
	 */
	public void setFieldDouble(double fieldDouble)
	{
		this.fieldDouble = fieldDouble;
	}
	/**
	 * @return the fieldDoubleObject
	 */
	public Double getFieldDoubleObject()
	{
		return fieldDoubleObject;
	}
	/**
	 * @param fieldDoubleObject the fieldDoubleObject to set
	 */
	public void setFieldDoubleObject(Double fieldDoubleObject)
	{
		this.fieldDoubleObject = fieldDoubleObject;
	}
	/**
	 * @return the fieldFloatObject
	 */
	public Float getFieldFloatObject()
	{
		return fieldFloatObject;
	}
	/**
	 * @param fieldFloatObject the fieldFloatObject to set
	 */
	public void setFieldFloatObject(Float fieldFloatObject)
	{
		this.fieldFloatObject = fieldFloatObject;
	}
	/**
	 * @return the fieldFloat
	 */
	public float getFieldFloat()
	{
		return fieldFloat;
	}
	/**
	 * @param fieldFloat the fieldFloat to set
	 */
	public void setFieldFloat(float fieldFloat)
	{
		this.fieldFloat = fieldFloat;
	}
	/**
	 * @return the fieldInteger
	 */
	public Integer getFieldInteger()
	{
		return fieldInteger;
	}
	/**
	 * @param fieldInteger the fieldInteger to set
	 */
	public void setFieldInteger(Integer fieldInteger)
	{
		this.fieldInteger = fieldInteger;
	}
	/**
	 * @return the fieldString
	 */
	public String getFieldString()
	{
		return fieldString;
	}
	/**
	 * @param fieldString the fieldString to set
	 */
	public void setFieldString(String fieldString)
	{
		this.fieldString = fieldString;
	}
	/**
	 * @return the fieldInt
	 */
	public int getFieldInt()
	{
		return fieldInt;
	}
	/**
	 * @param fieldInt the fieldInt to set
	 */
	public void setFieldInt(int fieldInt)
	{
		this.fieldInt = fieldInt;
	}
	
	
	/**
	 * @return the bigDecimal
	 */
	public BigDecimal getBigDecimal()
	{
		return bigDecimal;
	}
	/**
	 * @param bigDecimal the bigDecimal to set
	 */
	public void setBigDecimal(BigDecimal bigDecimal)
	{
		this.bigDecimal = bigDecimal;
	}

	

	/**
	 * @return the fieldBooleanObject
	 */
	public Boolean getFieldBooleanObject()
	{
		return fieldBooleanObject;
	}
	/**
	 * @param fieldBooleanObject the fieldBooleanObject to set
	 */
	public void setFieldBooleanObject(Boolean fieldBooleanObject)
	{
		this.fieldBooleanObject = fieldBooleanObject;
	}
	/**
	 * @return the fieldBoolean
	 */
	public boolean isFieldBoolean()
	{
		return fieldBoolean;
	}
	/**
	 * @param fieldBoolean the fieldBoolean to set
	 */
	public void setFieldBoolean(boolean fieldBoolean)
	{
		this.fieldBoolean = fieldBoolean;
	}
	/**
	 * @return the fieldByteObject
	 */
	public Byte getFieldByteObject()
	{
		return fieldByteObject;
	}
	/**
	 * @param fieldByteObject the fieldByteObject to set
	 */
	public void setFieldByteObject(Byte fieldByteObject)
	{
		this.fieldByteObject = fieldByteObject;
	}
	/**
	 * @return the fiedByte
	 */
	public byte getFiedByte()
	{
		return fiedByte;
	}
	/**
	 * @param fiedByte the fiedByte to set
	 */
	public void setFiedByte(byte fiedByte)
	{
		this.fiedByte = fiedByte;
	}
	/**
	 * @return the fieldCharObject
	 */
	public Character getFieldCharObject()
	{
		return fieldCharObject;
	}
	/**
	 * @param fieldCharObject the fieldCharObject to set
	 */
	public void setFieldCharObject(Character fieldCharObject)
	{
		this.fieldCharObject = fieldCharObject;
	}
	/**
	 * @return the fieldChar
	 */
	public char getFieldChar()
	{
		return fieldChar;
	}
	/**
	 * @param fieldChar the fieldChar to set
	 */
	public void setFieldChar(char fieldChar)
	{
		this.fieldChar = fieldChar;
	}
	/**
	 * @return the fieldClass
	 */
	public java.lang.Class<?> getFieldClass()
	{
		return fieldClass;
	}
	/**
	 * @param fieldClass the fieldClass to set
	 */
	public void setFieldClass(java.lang.Class<?> fieldClass)
	{
		this.fieldClass = fieldClass;
	}
	/**
	 * @return the error
	 */
	public Error getError()
	{
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(Error error)
	{
		this.error = error;
	}
	/**
	 * @return the exception
	 */
	public Exception getException()
	{
		return exception;
	}
	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception)
	{
		this.exception = exception;
	}
	/**
	 * @return the fieldShortObject
	 */
	public Short getFieldShortObject()
	{
		return fieldShortObject;
	}
	/**
	 * @param fieldShortObject the fieldShortObject to set
	 */
	public void setFieldShortObject(Short fieldShortObject)
	{
		this.fieldShortObject = fieldShortObject;
	}
	/**
	 * @return the fieldShort
	 */
	public short getFieldShort()
	{
		return fieldShort;
	}
	/**
	 * @param fieldShort the fieldShort to set
	 */
	public void setFieldShort(short fieldShort)
	{
		this.fieldShort = fieldShort;
	}



	/**
	 * @return the getWithNoSet
	 */
	public byte getGetWithNoSet()
	{
		return getWithNoSet;
	}
	/**
	 * @param setWithNoGet the setWithNoGet to set
	 */
	public void setSetWithNoGet(boolean setWithNoGet)
	{
		this.setWithNoGet = setWithNoGet;
	}



	/**
	 * @return the overloadedRestriction
	 */
	public String getOverloadedRestriction()
	{
		return overloadedRestriction;
	}
	/**
	 * @param overloadedRestriction the overloadedRestriction to set
	 */
	public void setOverloadedRestriction(String overloadedRestriction)
	{
		this.overloadedRestriction = overloadedRestriction;
	}
	

	/*
	 * Overloaded properties are not supported
	 * 
	 * Ex: Exception 
	 * 
	 * [error 2015/01/14 10:08:35.979 EST server1 <Function Execution Processor1> tid=0x3d] org.codehaus.jackson.map.JsonMappingException: Conflicting setter definitions for property "overloadedRestriction": solutions.gedi.demo.data.SimpleObject#setOverloadedRestriction(1 params) vs solutions.gedi.demo.data.SimpleObject#setOverloadedRestriction(1 params)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider._createAndCache2(StdDeserializerProvider.java:315)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider._createAndCacheValueDeserializer(StdDeserializerProvider.java:290)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider.findValueDeserializer(StdDeserializerProvider.java:159)
  	at org.codehaus.jackson.map.deser.std.StdDeserializer.findDeserializer(StdDeserializer.java:620)
  	at org.codehaus.jackson.map.deser.BeanDeserializer.resolve(BeanDeserializer.java:379)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider._resolveDeserializer(StdDeserializerProvider.java:407)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider._createAndCache2(StdDeserializerProvider.java:352)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider._createAndCacheValueDeserializer(StdDeserializerProvider.java:290)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider.findValueDeserializer(StdDeserializerProvider.java:159)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider.findTypedValueDeserializer(StdDeserializerProvider.java:180)
  	at org.codehaus.jackson.map.ObjectMapper._findRootDeserializer(ObjectMapper.java:2829)
  	at org.codehaus.jackson.map.ObjectMapper._readValue(ObjectMapper.java:2699)
  	at org.codehaus.jackson.map.ObjectMapper.readValue(ObjectMapper.java:1286)
  	at solutions.gedi.gemfire.operations.functions.ImportJsonFunction.importRegion(ImportJsonFunction.java:201)
  	at solutions.gedi.gemfire.operations.functions.ImportJsonFunction.importOnRegion(ImportJsonFunction.java:125)
  	at solutions.gedi.gemfire.operations.functions.ImportJsonFunction.execute(ImportJsonFunction.java:76)
  	at com.gemstone.gemfire.internal.cache.execute.AbstractExecution.executeFunctionLocally(AbstractExecution.java:356)
  	at com.gemstone.gemfire.internal.cache.execute.AbstractExecution$2.run(AbstractExecution.java:320)
  	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
  	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
  	at com.gemstone.gemfire.distributed.internal.DistributionManager.runUntilShutdown(DistributionManager.java:726)
  	at com.gemstone.gemfire.distributed.internal.DistributionManager$9$1.run(DistributionManager.java:1198)
  	at java.lang.Thread.run(Thread.java:745)
  Caused by: java.lang.IllegalArgumentException: Conflicting setter definitions for property "overloadedRestriction": solutions.gedi.demo.data.SimpleObject#setOverloadedRestriction(1 params) vs solutions.gedi.demo.data.SimpleObject#setOverloadedRestriction(1 params)
  	at org.codehaus.jackson.map.introspect.POJOPropertyBuilder.getSetter(POJOPropertyBuilder.java:199)
  	at org.codehaus.jackson.map.deser.BeanDeserializerFactory.addBeanProps(BeanDeserializerFactory.java:1161)
  	at org.codehaus.jackson.map.deser.BeanDeserializerFactory.buildBeanDeserializer(BeanDeserializerFactory.java:707)
  	at org.codehaus.jackson.map.deser.BeanDeserializerFactory.createBeanDeserializer(BeanDeserializerFactory.java:636)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider._createDeserializer(StdDeserializerProvider.java:401)
  	at org.codehaus.jackson.map.deser.StdDeserializerProvider._createAndCache2(StdDeserializerProvider.java:310)
  	... 22 more
	 * public void setOverloadedRestriction(Integer overloadedRestriction)
	{
		this.overloadedRestriction = String.valueOf(overloadedRestriction);
	}*/


	/**
	 * @return the setWithNoGet
	 */
	public boolean isSetWithNoGet()
	{
		return setWithNoGet;
	}
	/**
	 * @param getWithNoSet the getWithNoSet to set
	 */
	public void setGetWithNoSet(byte getWithNoSet)
	{
		this.getWithNoSet = getWithNoSet;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bigDecimal == null) ? 0 : bigDecimal.hashCode());
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		result = prime * result + ((exception == null) ? 0 : exception.hashCode());
		result = prime * result + fiedByte;
		result = prime * result + (fieldBoolean ? 1231 : 1237);
		result = prime * result + ((fieldBooleanObject == null) ? 0 : fieldBooleanObject.hashCode());
		result = prime * result + ((fieldByteObject == null) ? 0 : fieldByteObject.hashCode());
		result = prime * result + ((fieldCalendar == null) ? 0 : fieldCalendar.hashCode());
		result = prime * result + fieldChar;
		result = prime * result + ((fieldCharObject == null) ? 0 : fieldCharObject.hashCode());
		result = prime * result + ((fieldClass == null) ? 0 : fieldClass.hashCode());
		result = prime * result + ((fieldDate == null) ? 0 : fieldDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(fieldDouble);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((fieldDoubleObject == null) ? 0 : fieldDoubleObject.hashCode());
		result = prime * result + Float.floatToIntBits(fieldFloat);
		result = prime * result + ((fieldFloatObject == null) ? 0 : fieldFloatObject.hashCode());
		result = prime * result + fieldInt;
		result = prime * result + ((fieldInteger == null) ? 0 : fieldInteger.hashCode());
		result = prime * result + (int) (fieldLong ^ (fieldLong >>> 32));
		result = prime * result + ((fieldLongObject == null) ? 0 : fieldLongObject.hashCode());
		result = prime * result + fieldShort;
		result = prime * result + ((fieldShortObject == null) ? 0 : fieldShortObject.hashCode());
		result = prime * result + ((fieldSqlDate == null) ? 0 : fieldSqlDate.hashCode());
		result = prime * result + ((fieldString == null) ? 0 : fieldString.hashCode());
		result = prime * result + ((fieldTime == null) ? 0 : fieldTime.hashCode());
		result = prime * result + ((fieldTimestamp == null) ? 0 : fieldTimestamp.hashCode());
		result = prime * result + getWithNoSet;
		result = prime * result + ((localDate == null) ? 0 : localDate.hashCode());
		result = prime * result + ((localTime == null) ? 0 : localTime.hashCode());
		result = prime * result + ((overloadedRestriction == null) ? 0 : overloadedRestriction.hashCode());
		result = prime * result + (setWithNoGet ? 1231 : 1237);
		result = prime * result + ((simpleEnum == null) ? 0 : simpleEnum.hashCode());
		return result;
	}
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
		if (getClass() != obj.getClass())
			return false;
		SimpleObject other = (SimpleObject) obj;
		if (bigDecimal == null)
		{
			if (other.bigDecimal != null)
				return false;
		}
		else if (!bigDecimal.equals(other.bigDecimal))
			return false;
		if (error == null)
		{
			if (other.error != null)
				return false;
		}
		else if (!error.equals(other.error))
			return false;
		if (exception == null)
		{
			if (other.exception != null)
				return false;
		}
		else if (!exception.equals(other.exception))
			return false;
		if (fiedByte != other.fiedByte)
			return false;
		if (fieldBoolean != other.fieldBoolean)
			return false;
		if (fieldBooleanObject == null)
		{
			if (other.fieldBooleanObject != null)
				return false;
		}
		else if (!fieldBooleanObject.equals(other.fieldBooleanObject))
			return false;
		if (fieldByteObject == null)
		{
			if (other.fieldByteObject != null)
				return false;
		}
		else if (!fieldByteObject.equals(other.fieldByteObject))
			return false;
		if (fieldCalendar == null)
		{
			if (other.fieldCalendar != null)
				return false;
		}
		else if (!fieldCalendar.equals(other.fieldCalendar))
			return false;
		if (fieldChar != other.fieldChar)
			return false;
		if (fieldCharObject == null)
		{
			if (other.fieldCharObject != null)
				return false;
		}
		else if (!fieldCharObject.equals(other.fieldCharObject))
			return false;
		if (fieldClass == null)
		{
			if (other.fieldClass != null)
				return false;
		}
		else if (!fieldClass.equals(other.fieldClass))
			return false;
		if (fieldDate == null)
		{
			if (other.fieldDate != null)
				return false;
		}
		else if (!fieldDate.equals(other.fieldDate))
			return false;
		if (Double.doubleToLongBits(fieldDouble) != Double.doubleToLongBits(other.fieldDouble))
			return false;
		if (fieldDoubleObject == null)
		{
			if (other.fieldDoubleObject != null)
				return false;
		}
		else if (!fieldDoubleObject.equals(other.fieldDoubleObject))
			return false;
		if (Float.floatToIntBits(fieldFloat) != Float.floatToIntBits(other.fieldFloat))
			return false;
		if (fieldFloatObject == null)
		{
			if (other.fieldFloatObject != null)
				return false;
		}
		else if (!fieldFloatObject.equals(other.fieldFloatObject))
			return false;
		if (fieldInt != other.fieldInt)
			return false;
		if (fieldInteger == null)
		{
			if (other.fieldInteger != null)
				return false;
		}
		else if (!fieldInteger.equals(other.fieldInteger))
			return false;
		if (fieldLong != other.fieldLong)
			return false;
		if (fieldLongObject == null)
		{
			if (other.fieldLongObject != null)
				return false;
		}
		else if (!fieldLongObject.equals(other.fieldLongObject))
			return false;
		if (fieldShort != other.fieldShort)
			return false;
		if (fieldShortObject == null)
		{
			if (other.fieldShortObject != null)
				return false;
		}
		else if (!fieldShortObject.equals(other.fieldShortObject))
			return false;
		if (fieldSqlDate == null)
		{
			if (other.fieldSqlDate != null)
				return false;
		}
		else if (!fieldSqlDate.equals(other.fieldSqlDate))
			return false;
		if (fieldString == null)
		{
			if (other.fieldString != null)
				return false;
		}
		else if (!fieldString.equals(other.fieldString))
			return false;
		if (fieldTime == null)
		{
			if (other.fieldTime != null)
				return false;
		}
		else if (!fieldTime.equals(other.fieldTime))
			return false;
		if (fieldTimestamp == null)
		{
			if (other.fieldTimestamp != null)
				return false;
		}
		else if (!fieldTimestamp.equals(other.fieldTimestamp))
			return false;
		if (getWithNoSet != other.getWithNoSet)
			return false;
		if (localDate == null)
		{
			if (other.localDate != null)
				return false;
		}
		else if (!localDate.equals(other.localDate))
			return false;
		if (localTime == null)
		{
			if (other.localTime != null)
				return false;
		}
		else if (!localTime.equals(other.localTime))
			return false;
		if (overloadedRestriction == null)
		{
			if (other.overloadedRestriction != null)
				return false;
		}
		else if (!overloadedRestriction.equals(other.overloadedRestriction))
			return false;
		if (setWithNoGet != other.setWithNoGet)
			return false;
		if (simpleEnum != other.simpleEnum)
			return false;
		return true;
	}
	/**
	 * @return the localTime
	 */
	public LocalTime getLocalTime()
	{
		return localTime;
	}
	/**
	 * @return the localDate
	 */
	public LocalDate getLocalDate()
	{
		return localDate;
	}
	/**
	 * @param localTime the localTime to set
	 */
	public void setLocalTime(LocalTime localTime)
	{
		this.localTime = localTime;
	}
	/**
	 * @param localDate the localDate to set
	 */
	public void setLocalDate(LocalDate localDate)
	{
		this.localDate = localDate;
	}
	
	
	
	/**
	 * @return the localDateTime
	 */
	public LocalDateTime getLocalDateTime()
	{
		return localDateTime;
	}
	/**
	 * @param localDateTime the localDateTime to set
	 */
	public void setLocalDateTime(LocalDateTime localDateTime)
	{
		this.localDateTime = localDateTime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SimpleObject [simpleEnum=").append(simpleEnum).append(", fieldSqlDate=").append(fieldSqlDate)
		.append(", fieldDate=").append(fieldDate).append(", fieldTime=").append(fieldTime).append(", fieldTimestamp=")
		.append(fieldTimestamp).append(", fieldCalendar=").append(fieldCalendar).append(", fieldLongObject=")
		.append(fieldLongObject).append(", fieldLong=").append(fieldLong).append(", fieldDouble=").append(fieldDouble)
		.append(", fieldDoubleObject=").append(fieldDoubleObject).append(", fieldFloatObject=").append(fieldFloatObject)
		.append(", fieldFloat=").append(fieldFloat).append(", fieldInteger=").append(fieldInteger)
		.append(", fieldString=").append(fieldString).append(", fieldInt=").append(fieldInt).append(", bigDecimal=")
		.append(bigDecimal).append(", fieldBooleanObject=").append(fieldBooleanObject).append(", fieldBoolean=")
		.append(fieldBoolean).append(", fieldByteObject=").append(fieldByteObject).append(", fiedByte=")
		.append(fiedByte).append(", fieldCharObject=").append(fieldCharObject).append(", fieldChar=").append(fieldChar)
		.append(", fieldClass=").append(fieldClass).append(", error=").append(error).append(", exception=")
		.append(exception).append(", fieldShortObject=").append(fieldShortObject).append(", fieldShort=")
		.append(fieldShort).append(", setWithNoGet=").append(setWithNoGet).append(", getWithNoSet=")
		.append(getWithNoSet).append(", overloadedRestriction=").append(overloadedRestriction).append(", localTime=")
		.append(localTime).append(", localDate=").append(localDate).append("]");
		return builder.toString();
	}


	private SimpleEnum simpleEnum; 
	
	private java.sql.Date fieldSqlDate;
	private Date fieldDate;
	private Time fieldTime;
	private Timestamp fieldTimestamp;
	private Calendar fieldCalendar;
	private Long fieldLongObject;
	private long fieldLong;
	private double fieldDouble;
	private Double fieldDoubleObject;
	private Float fieldFloatObject;
	private float fieldFloat;
	private Integer fieldInteger;
	private String fieldString;
	private int fieldInt;
	private BigDecimal bigDecimal;
	
	private Boolean fieldBooleanObject;
	private boolean fieldBoolean;
	private Byte fieldByteObject;
	private byte fiedByte;
	private Character fieldCharObject;
	private char fieldChar;
	private java.lang.Class<?> fieldClass;
	private Error error;
	private Exception exception;
	private Short fieldShortObject;
	private short fieldShort;
	
	private boolean setWithNoGet;
	
	private byte getWithNoSet = 23;
	
	private String overloadedRestriction;
	private LocalTime localTime;
	private LocalDate localDate;
	private LocalDateTime localDateTime;

	
}
