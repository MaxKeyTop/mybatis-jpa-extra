
# MyBatis JPA Extra
   **MyBatis JPA Extra**对MyBatis进行了JPA扩展，旨在基于JPA 2.1的注释简化对单表CUID操作，根据JPA注释动态生成SQL语句；使用Interceptor实现数据库SELECT分页查询，适配多种数据库；另外提供mybatis-jpa-extra-spring-boot-starter简化SpringBoot集成。
 
相关资源

[MyBatis网站][1]

[MyBatis GitHub源码][2]


## 1、JavaBean注释简单

只支持6个注释
> * @Entity
> * @Table
> * @Column
> * @Id
> * @GeneratedValue
> * @Transient 


@GeneratedValue有3中策略 

 1. **AUTO**
 
    uuid

    uuid.hex

    serial

 2. **SEQUENCE**
 
    generator值为数据库序列名

 3. **IDENTITY**
 
    generator无，根据数据库自动生成方式


```java
package org.apache.mybatis.jpa.test.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.mybatis.jpa.persistence.JpaBaseDomain;

/*
   ID                   varchar(40)                    not null,
   NAME                 varchar(60)                    not null,
   STATUS               char(1)                        null,
   CREATEBY             varchar(40)                    null,
   CREATEDATE           date                           null,
   UPDATEBY             varchar(40)                    null,
   UPDATEDATE           date                           null,
   constraint PK_ROLES primary key clustered (ID)
 */
/**
 * @author Crystal.Sea
 *
 */
@Entity
@Table(name = "STUDENTS")  
public class Students extends JpaBaseDomain implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6928570405840778151L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO,generator="serial")
	//@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_MYBATIS_STUD")
	//@GeneratedValue(strategy=GenerationType.IDENTITY,generator="SEQ_MYBATIS_STUD")
	private String id;
	@Column
	private String stdNo;
	@Column
	private String stdName;
	@Column
	private String stdGender;
	@Column
	private int stdAge;
	@Column
	private String stdMajor;
	@Column
	private String stdClass;
	
	public Students() {
		super();
	}

	public String getStdNo() {
		return stdNo;
	}

	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getStdGender() {
		return stdGender;
	}

	public void setStdGender(String stdGender) {
		this.stdGender = stdGender;
	}

	public int getStdAge() {
		return stdAge;
	}

	public void setStdAge(int stdAge) {
		this.stdAge = stdAge;
	}

	public String getStdMajor() {
		return stdMajor;
	}

	public void setStdMajor(String stdMajor) {
		this.stdMajor = stdMajor;
	}

	public String getStdClass() {
		return stdClass;
	}

	public void setStdClass(String stdClass) {
		this.stdClass = stdClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Students [stdNo=" + stdNo + ", stdName=" + stdName + ", stdgender=" + stdGender + ", stdAge=" + stdAge
				+ ", stdMajor=" + stdMajor + ", stdClass=" + stdClass + "]";
	}
}

```

## 2、单表新增、修改、删除、查询

```java
package org.apache.mybatis.jpa.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.mybatis.jpa.test.dao.service.StudentsService;
import org.apache.mybatis.jpa.test.domain.Students;
import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyBatisTestRunner {
	private static final Logger _logger = LoggerFactory.getLogger(MyBatisTestRunner.class);
	
	public static ApplicationContext context;
	public static StudentsService service;
	
	@Test
	public void insert() throws Exception{
		_logger.info("insert...");
		Students student=new Students();
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		service.insert(student);
		Thread.sleep(1000);
		service.remove(student.getId());
	}
	
	@Test
	public void get() throws Exception{
		_logger.info("get...");
		Students student=service.get("921d3377-937a-4578-b1e2-92fb23b5e512");
		 _logger.info("Students "+student);

	}
	
	@Test
	public void remove() throws Exception{
		_logger.info("remove...");
		Students student=new Students();
		student.setId("921d3377-937a-4578-b1e2-92fb23b5e512");
		service.remove(student.getId());
		
	}
	
	@Test
	public void batchDelete() throws Exception{
		_logger.info("batchDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		service.batchDelete(idList);
	}

	@Test
	public void findAll() throws Exception{
		_logger.info("findAll...");
		_logger.info("findAll "+service.findAll());
	}
	
	@Before
	public void initSpringContext(){
		if(context!=null) return;
		_logger.info("init Spring Context...");
		SimpleDateFormat sdf_ymdhms =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime=sdf_ymdhms.format(new Date());

		try{
			MyBatisTestRunner runner=new MyBatisTestRunner();
			runner.init();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		_logger.info("-- --Init Start at " + startTime+" , End at  "+sdf_ymdhms.format(new Date()));
	}
	
	//Initialization ApplicationContext for Project
	public void init(){
		_logger.info("init ...");
		
		context = new ClassPathXmlApplicationContext(new String[] {"spring/applicationContext.xml"});
		
		WebContext.applicationContext=context;
		service =(StudentsService)WebContext.getBean("studentsService");
	}
	
}
```

## 3、支持分页查询

```java
package org.apache.mybatis.jpa.test;

import java.util.ArrayList;
import java.util.List;
import org.apache.mybatis.jpa.test.dao.service.StudentsService;
import org.apache.mybatis.jpa.test.domain.Students;
import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisJpaApplication.class)
public class MybatisJpaApplicationTest{ 
    private static final Logger _logger = LoggerFactory.getLogger(MybatisJpaApplicationTest.class);
    
    @Autowired
    StudentsService studentsService;
    
    @Autowired
    org.apache.ibatis.session.SqlSessionFactory SqlSessionFactory;

    @Autowired
    private ApplicationContext applicationContext;
 
    @Before
    public  void before() {
    	_logger.info("---------------- before");
    	WebContext.applicationContext=applicationContext;

    }
    
    @Test
	public void insert() throws Exception{
		_logger.info("insert...");
		Students student=new Students();
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		studentsService.insert(student);
		
		Thread.sleep(1000);
		studentsService.remove(student.getId());
		
	}
	
	@Test
	public void get() throws Exception{
		_logger.info("get...");
		Students student=studentsService.get("921d3377-937a-4578-b1e2-92fb23b5e512");
		
		System.out.println("Students "+student);
		 _logger.info("Students "+student);

	}
	
	@Test
	public void remove() throws Exception{
		
		_logger.info("remove...");
		Students student=new Students();
		student.setId("921d3377-937a-4578-b1e2-92fb23b5e512");
		studentsService.remove(student.getId());
		
	}
	
	@Test
	public void batchDelete() throws Exception{
		_logger.info("batchDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		studentsService.batchDelete(idList);
	}

	@Test
	public void queryPageResults() throws Exception{
		
		_logger.info("queryPageResults...");
		 Students student=new Students();
		 //student.setId("af04d610-6092-481e-9558-30bd63ef783c");
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 List<Students> allListStudents = 
				 studentsService.queryPageResults(student).getRows();
		 for (Students s : allListStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	@Test
	public void queryPageResultsByMapperId() throws Exception{

		_logger.info("queryPageResults by mapperId...");
		 Students student=new Students();
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 
		 List<Students> allListStudents = 
				 studentsService.queryPageResults("queryPageResults1",student).getRows();
		 for (Students s : allListStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	
	 @Test
	 public void findAll() {
			_logger.info("---------------- ALL");
			
			List<Students> allListStudents=studentsService.findAll();
			 for (Students s : allListStudents) {
				 _logger.info("Students "+s);
			 }
	 }
}

package org.apache.mybatis.jpa.test;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MybatisJpaConfig {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Bean
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSource dataSource() {
		return DruidDataSourceBuilder.create().build();
	}
}
```


## 4、映射文件配置

```xml
<mapper namespace="org.apache.mybatis.jpa.test.dao.persistence.StudentsMapper" >
	<sql id="sql_condition">
		WHERE	1	=	1
    	<if test="id != null">
			AND	ID	=	'${id}'
		</if>
		<if test="stdName != null  and stdName != '' ">
			AND STDNAME like '%${stdName}%'
		</if>
		<if test="stdGender != null  and stdGender != '' ">
			AND	STDGENDER	=	#{stdGender}
		</if>
		<if test="stdMajor != null">
			<![CDATA[AND	STDMAJOR	= #{stdMajor}]]>
		</if>
	</sql>
	
    <select id="queryPageResults" parameterType="Students" resultType="Students">
    	SELECT 
    		ID		   ,
			STDNO      ,
			STDNAME    ,
			STDGENDER  ,
			STDAGE     ,
			STDMAJOR   ,
			STDCLASS 
    	FROM STUDENTS 
    	<include refid="sql_condition"/>
    </select>
 
     <select id="queryPageResults1" parameterType="Students" resultType="Students">
    	SELECT 
    		ID		   ,
			STDNO      ,
			STDNAME    ,
			STDGENDER  ,
			STDAGE     ,
			STDMAJOR   ,
			STDCLASS 
    	FROM STUDENTS 
    	<include refid="sql_condition"/>
    </select>
       
     <select id="queryBy" parameterType="Students" resultType="Students">
    	SELECT 
    		ID		   ,
			STDNO      ,
			STDNAME    ,
			STDGENDER  ,
			STDAGE     ,
			STDMAJOR   ,
			STDCLASS 
    	FROM ROLES 
    	<include refid="sql_condition"/>
    </select>
  
    <delete id="delete" parameterType="Students" >
    	DELETE FROM STUDENTS WHERE ID=#{id}
    </delete>
    
```


##  5、SpringBoot配置

```ini
 spring.main.web-application-type=NONE
#
spring.datasource.username=root
spring.datasource.password=maxkey
spring.datasource.url=jdbc:mysql://localhost/test?autoReconnect=true&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource


mybatis.type-aliases-package=org.apache.mybatis.jpa.test.domain
mybatis.mapper-locations=classpath*:/org/apache/mybatis/jpa/test/dao/persistence/xml/mysql/*.xml
mybatis.table-column-escape=true
#mybatis.table-column-escape-char=`
```


  [1]: http://www.mybatis.org/mybatis-3/
  [2]: https://github.com/mybatis/mybatis-3/
