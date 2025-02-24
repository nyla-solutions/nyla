#NYLA Solutions - DAO Spring Batch

This project provides password encryption, emailer Error Listener and other utilities to the spring batch framework.

The following is an example of defining a data source that supports password encryption using *nyla.solutions.global.util.Config*.

	
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:aop="http://www.springframework.org/schema/aop" 
		xmlns:p="http://www.springframework.org/schema/p"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd
			http://www.springframework.org/schema/batch http://www.springframework.org/schema/batch/spring-batch-2.1.xsd
			http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	
	 <bean id="dataSource" class="nyla.solutions.dao.spring.batch.ConfigDriverMgrDataSource">
			<property name="driverClassName" value="${jdbc.driver}" />
			<property name="url" value="${jdbc.connection.url}" />
			<property name="username" value="${jdbc.user}" />
			<property name="password" value="${jdbc.password}" />
		</bean>
	
	</beans>

The following is an example configuration can set the error listener that sends emails when
exceptions occur.



	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:aop="http://www.springframework.org/schema/aop" xmlns:p="http://www.springframework.org/schema/p"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd
			http://www.springframework.org/schema/batch http://www.springframework.org/schema/batch/spring-batch-2.1.xsd
			http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
		
			
		<bean id="ON_READ_ERROR_LISTENER" class="nyla.solutions.dao.spring.batch.SpringBatchErrorListener">
		    <property name="readErrorCmd">
		    	<bean class="solutions.global.patterns.command.net.EmailCommand">
		    		<property name="subject" value="Spring Batch Error Notification"/>
		    		<property name="to" value="gregory.green@emc.com"/>
		    		<property name="textDecorator">
		    			<bean class="solutions.global.exception.fault.FaultFormatTextDecorator">
		    				<property name="template">
		    				 	<value><![CDATA[<div><b>MODULE</b> :${module} </div>
					         <div><b>OPERATION</b> :${operation} </div>
					         <div><b>MESSAGE</b> :${message} </div>
					         <div><b>CATEGORY</b> :${category} </div>
					         <div><b>CODE</b> :${code} </div>
					         <div><b>argument</b> :${argument} </div>
					         <div><b>notes</b> :${notes} </div>]]>
		    				 	</value>
		    				</property>
		    			</bean>
		    		</property>
		    	</bean>
		    </property>
			<property name="faultService">
				<bean class="solutions.global.exception.fault.FaultMgr">
					<property name="faultErrorMap">
						<map>
							<entry>
								<key>
									<value>org.springframework.batch.core.JobExecutionException</value>
								</key>
									<bean class="solutions.global.exception.fault.FaultError">
										<constructor-arg type="String" value="0001"/>
									    <constructor-arg type="String" value="BATCH"/>
									</bean>
							</entry>
							<entry>
								<key>
									<value>org.springframework.batch.core.step.FatalStepExecutionException</value>
								</key>
								<bean class="solutions.global.exception.fault.FaultError">
										<constructor-arg type="String" value="0002"/>
									    <constructor-arg type="String" value="BATCH"/>
									</bean>
							</entry>
						</map>
					</property>
				</bean>
			</property>
		</bean>
	</beans>
	
The following error can read from an excel file and write into a JDBC relational database.
	
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:aop="http://www.springframework.org/schema/aop" 
		xmlns:p="http://www.springframework.org/schema/p"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd
			http://www.springframework.org/schema/batch http://www.springframework.org/schema/batch/spring-batch-2.1.xsd
			http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	
		<job id="LOCAL_CC" xmlns="http://www.springframework.org/schema/batch">
			<step id="LOCAL_CC_IMPORT" next="">
				<tasklet>
					<chunk reader="inventoryCCReader" writer="loalCCWriter" processor="localCCProcessor"
						commit-interval="1000" />
				</tasklet>
			</step>
		</job>
		
		<bean id="inventoryCCReader" class="solutions.dao.spring.batch.ExcelItemReader">
			<property name="sheetNumber" value="1"/>
			<property name="filePathProperty" value="CC.filePath"/>
			
		</bean>
	   <bean id="localCCProcessor" class="mycompany.integration.batch.InventoryProcessor">
			<property name="batchNumberPos" value="1"/>
			<property name="structureKeyPos" value="2"/>
			<property name="locationTypePos" value="6"/>
			<property name="containerLabelPos" value="9"/>
			<property name="barcodePos" value="10"/>
			<property name="vendorNamePos" value="11"/>
			<property name="casNumberPos" value="16"/>
			<property name="inventorySourcePos" value="17"/>
			<property name="structureSourceCodePos" value="18"/>
			<property name="molKeyPos" value="19"/>
			<property name="dataRowSize" value="20"/>
	
	        <property name="acdSourceCode" value="ACD"/>
	        <property name="mcidbSourceCode" value="MCIDB"/>
	         <property name="merckAcdSourceCode" value="MERCKACD"/>
	         
			<property name="defaultLnumberVendor" value="MERCK"/>
			<property name="defaultMfcdVendor" value="Sigma-Aldrich"/>
			<property name="skipFilePath" value="${report.dir}/LOCAL_CC_SKIPS.csv"/>
			<property name="skipFileHeader" value="BATCHNUMBER,STRUCTUREKEYS,SITE,BUILDING,FLOOR,LOCATIONTYPE,ROOM,SUBLOCATION,CONTAINERLABEL,BARCODE,VENDOR,ORIGINALAMOUNT,ORIGINALAMOUNTUNITS,STATUS,INVENTORY_ID,CASNUMBER,INV_SOURCE_CD,STRUCT_SOURCE_CD"/>
			
		</bean>
	<!-- loalCCWriter -->
		<bean id="loalCCWriter" class="org.springframework.batch.item.database.JdbcBatchItemWriter">
			<property name="sql">
				<value>
				INSERT
				  INTO INVENTORY
				    (
				       BATCHNUMBER,
				       STRUCTUREKEY,
				       SITE,
				       BUILDING,
				       FLOOR,
				       LOC_TYPE_CD,
				       ROOM,
				       SUBLOCATION,
				       CONTAINERLABEL,
				       BARCODE,
				       VENDORNAME,
				       ORIGINALAMOUNT,
				       ORIGINALAMOUNTUNITS,
				       STATUS_CD,
				       INVENTORY_ID,
				       CASNUMBER, 
				       INV_SOURCE_CD, -- 17
				       STRUCT_SOURCE_CD, --18
				       LAST_MOD_DT, -- 19
				       MOLKEY  -- 20
				      
				    )
				    VALUES
				    (
				      ? , 
				      ? ,
				      ? ,
				      ? ,
				      ? , -- 5
				      ? ,
				      ? ,
				      ? ,
				      ? ,
				      ? ,
				      ? , -- 10
				      ? ,
				      ? ,
				      NVL(?,'IN USE'), -- status
				      ? ,
				      ?,
				      ?,
				      ?,
				      sysdate,  --19
				      ? -- molkey (19, 20)
				      
				    )
	      </value>
			</property>
					<property name="dataSource">
				<ref bean="dataSource"/>
			</property>
			<property name="itemPreparedStatementSetter">
				<ref bean="LocalCCDataRowMapper"/>
			</property>
		</bean>
		
	    <bean id="LocalCCDataRowMapper" class="solutions.dao.spring.batch.DataRowMapperSetter" scope="prototype">
			<property name="maxParameters" value="19"/>	
		</bean>
	</beans>