# MicroServiceCloud

## 项目基础依赖

工程基础依赖中并未引入数据库相关依赖，是因为引入之后必须要配置相关数据库信息，而类似服务中心并不需要

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-dependencies</artifactId>
    <version>Finchley.SR2</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.2.1.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.12</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.2.3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework/springloaded -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>springloaded</artifactId>
    <version>1.2.0.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
```



## Eureka服务注册中心

#### 1. Module Introduction

```xml-dtd
工程中包含三个服务中心，分别使用7001、7002、7003端口，搭建成一个简单的集群信息
```

#### 2. pom.xml

```xml
<!--eureka-server服务端 -->
<!--springboot2.x 之后要使用spring-cloud-starter-netflix-eureka-server-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    <version>2.2.0.RELEASE</version>
</dependency>
```

#### 3. application.yml

###### 3.1 配置信息

```yml
server:
  port: 7001

eureka:
  instance:
    hostname: eureka7001.com #eureka服务端的实例名称
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      #单机 设置与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址（单机）。
#      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

      # 构成集群环境 将与之相连的服务地址以逗号分隔
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/

```



###### 3.2 配置localhost映射信息

修改/etc/hosts文件，使本机地址可以分别映射到三个不同端口服务中心的地址

```
127.0.0.1   eureka7001.com
127.0.0.1   eureka7002.com
127.0.0.1   eureka7003.com
```



#### 4. 主函数

```java
// 主启动类中加入@EnableEurekaServer注解，指明是Eureka服务中心 
// Eureka 通过localhost:7001检测
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication7001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication7001.class, args);
    }
}
```



## micro_service_cloud---公共模块api

#### 1. Module Introduction

```
将各个微服务模块都会使用的部分集成到此部分，供各个微服务调用，不会产生大量冗余代码
```

#### 2. pom.xml

lombok是用一个插件，可以用来自动生成实体类中的Get Set等一些可以自动生成的部分代码

```xml
<!--lombok此处需要指定版本号-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.10</version>
</dependency>
<!--Spring Boot 2.x 中Feign支持-->
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-openfeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
```

#### 3. lombok使用

```java
/**
 * 1、@Data：在JavaBean中使用，注解包含包含getter、setter、NoArgsConstructor注解
 *
 * 　　@Value注解和@Data类似，区别在于它会把所有成员变量默认定义为private final修饰，并且不会生成set方法
 *
 * 2、@getter：在JavaBean中使用，注解会生成对应的getter方法
 *
 * 3、@setter：在JavaBean中使用，注解会生成对应的setter方法
 *
 * 4、@NoArgsConstructor：在JavaBean中使用，注解会生成对应的无参构造方法
 *
 * 5、@AllArgsConstructor：在JavaBean中使用，注解会生成对应的有参构造方法
 *
 * 　　@RequiredArgsConstructor ：生成private构造方法，使用staticName选项生成指定名称的static方法。
 *
 * 6、@ToString：在JavaBean中使用，注解会自动重写对应的toStirng方法
 *
 * 　　@ToString(exclude={"column1","column2"})：排除多个column列所对应的元素
 *
 * 　　@ToString(of={"column1","column2"})：只生成包含多个column列所对应的元素
 *
 * 7、@EqualsAndHashCode：在JavaBean中使用，注解会自动重写对应的equals方法和hashCode方法
 *
 * 8、@Slf4j：在需要打印日志的类中使用，项目中使用slf4j日志框架
 *
 * 9、@Log4j：在需要打印日志的类中使用，项目中使用log4j日志框架
 *
 * 10、@NonNull：注解快速判断是否为空,为空抛出java.lang.NullPointerException
 *
 * 11、@Synchronized：注解自动添加到同步机制，生成的代码并不是直接锁方法,而是锁代码块， 作用范围是方法上
 * 12、@Cleanup：注解用于确保已分配的资源被释放（IO的连接关闭）
 *
 * ************************************************重点线***************************************************
 *
 * 13、@Accessors(chain = true)：链式风格，在调用set方法时，返回这个类的实例对象
 *          example: dept.setxxx().setxxx().setxxx()
 *
 * -------------------------------------
 * @SuppressWarnings 抑制警告
 *          可以传入的关键字查看https://blog.csdn.net/qq_41097820/article/details/88840960
 */
```



#### 4. 核心公共模块

###### 4.1 公共实体类Dept

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@SuppressWarnings("serial")
@NoArgsConstructor
@Accessors(chain = true)
public class Dept implements Serializable { // 序列化接口必须实现
    private Long deptno; // 主键
    private String dname; // 部门名称
    private String db_source;// 来自那个数据库，因为微服务架构可以一个服务对应一个数据库，同一个信息被存储到不同数据库

    // 在这里不要使用get set方法等等，因为每次增加字段都需要修改很多内容

    public Dept(String dname) {
        super();
        this.dname = dname;
    }

}
```

###### 4.2 FeignClient模块

使用Feign包装接口用来代替Ribbon+RestTemplate的负载均衡策略

```java
// MICRO-SERVICE-CLOUD-DEPT 为指定的服务名，此服务名在服务端定义
// fallbackFactory 指定某个服务端出问题之后定义的服务降级策略类
@FeignClient(value = "MICRO-SERVICE-CLOUD-DEPT", fallbackFactory = DeptClientServiceFallbackFactory.class)
public interface DeptClientService {
    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") long id);

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list();

    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    public Dept add(Dept dept);
}
```

服务降级策略类

```java
@Component // 不要忘记添加，不要忘记添加
public class DeptClientServiceFallbackFactory implements FallbackFactory<DeptClientService> {
    @Override
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService() {
            @Override
            public Dept get(long id) {
                System.out.println("获取单条数据时出错了.....");
                return new Dept().setDeptno(id).setDname("该ID：" + id + "没有没有对应的信息,Consumer客户端提供的降级信息,此刻服务Provider已经关闭")
                        .setDb_source("no this database in MySQL");
            }

            @Override
            public List<Dept> list() {
                System.out.println("获取全部数据时出错了.....");
                return new ArrayList<Dept>();
            }

            @Override
            public Dept add(Dept dept) {
                return dept;
            }
        };
    }
}
```



###### 5. maven打包

```
将公共模块完成之后，为了方便以后其他的微服务模块可以直接调用，需要一下两步
1. maven clean
2. maven install
```



## micro_service_cloud---服务端

#### 1. Module Introduction

```
定义了两个服务提供商作为服务端，端口分别为8001、8002
```

#### 2. pom.xml

```xml
<!-- 引入自己定义的api通用包，可以使用Dept部门Entity -->
<dependency>
    <groupId>com.itcc.micro_service_cloud</groupId>
    <artifactId>micro_service_cloud_api</artifactId>
    <version>${project.version}</version>
</dependency>
<!--eureka-server服务端 -->
<!--springboot2.x 之后要使用spring-cloud-starter-netflix-eureka-client-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>2.2.0.RELEASE</version>
</dependency>

<!-- actuator监控信息完善 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-config -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.1</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.12</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.8</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

**小注意点**

```xml-dtd
1. 使用 ${project.version} 定义引入的微服务模块，方便以后的版本升级
2. 对于 pom文件中可能出现的依赖冲突使用<exclusion></exclusion>去除
3. mysql版本要与本机 mysql版本一致
4. actuator依赖
```



#### 3. mybatis配置

###### 3.1 配置目录

```
--resources
    --mybatis
    --mapper
        --DeptMapper.xml
    --config-mybatis.xml
```

###### 3.2 DeptMapper配置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.itcc.springcloud.mapper.DeptMapper">

    <!--resultType中的Dept此时要写全类名-->

    <select id="findById" 
            resultType="com.itcc.springcloud.entity.Dept" parameterType="Long">
      	select * from dept where deptno=#{deptno};
    </select>
    <select id="findAll" resultType="com.itcc.springcloud.entity.Dept">
      	select * from dept;
    </select>
    <insert id="add" useGeneratedKeys="true" keyProperty="deptno" parameterType="com.itcc.springcloud.entity.Dept">
      	INSERT INTO dept(dname,db_source) VALUES(#{dname},DATABASE());
    </insert>
</mapper>
```

###### 3.3 config-mybatis配置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
      	<!-- 二级缓存开启 -->
        <setting name="cacheEnabled" value="true"/>
    </settings>
</configuration>
```



#### 4. 服务端配置

```yml
server:
  port: 8001

spring:
  application:
    # 名称不能使用下划线
    name: micro-service-cloud-dept
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource            # 当前数据源操作类型
    driver-class-name: com.mysql.cj.jdbc.Driver              # mysql驱动包
    url: jdbc:mysql://localhost:3306/cloudDB01              # 数据库名称
    username: root
    password: caimenglei.
    dbcp2:
      min-idle: 5                                           # 数据库连接池的最小维持连接数
      initial-size: 5                                       # 初始化连接数
      max-total: 5                                          # 最大连接数
      max-wait-millis: 200                                  # 等待连接获取的最大超时时间

mybatis:
  # 指定全局配置文件位置
  config-location: classpath:mybatis/config-mybatis.xml
  # 指定sql映射文件位置
  mapper-locations: classpath:mybatis/mapper/*.xml

eureka:
  client: #客户端注册进eureka服务列表内
    service-url:
#      defaultZone: http://localhost:7001/eureka

      # 将服务注册进集群
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/,http://eureka7001.com:7001/eureka/
  instance:
    instance-id: itcc-microservicecloud-dept8001  # 实例名称
    prefer-ip-address: true     # 访问路径可以显示IP地址

# 
info:
  app.name: itcc-microservicecloud
  company.name: www.itcc.com
  build.artifactId: "@project.artifactId@"
  build.version: "@project.version@"
```



#### 5. Controller

```java
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") Long id) {

        return deptService.get(id);
    }

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list() {
        return deptService.list();
    }

    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    public Dept add(@RequestBody Dept dept) {
        // 此处的RequestBody注解必须要加，否则restTemplate服务调用无法接受数据
        System.out.println(dept);
        return deptService.add(dept);
    }

    @RequestMapping(value = "/dept/discovery", method = RequestMethod.GET)
    public Object discovery() {
        // 获取到eureka中所有的服务
        List<String> list = client.getServices();
        System.out.println("**********" + list);

        List<ServiceInstance> srvList = client.getInstances("MICRO_SERVICE_CLOUD_DEPT");
        for (ServiceInstance element : srvList) {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                    + element.getUri());
        }
        return this.client;
    }
}
```



#### 6. mapper

可以使用注解的方式或者配置文件的方式

```java
@Mapper
public interface DeptMapper {

//    @Select("select * from dept where deptno=#{id}")
    public Dept findById(Long id);

//    @Select("select * from dept")
    public List<Dept> findAll();

//    @Options(useGeneratedKeys = true, keyProperty = "deptno")
//    @Insert("insert into dept(dname,db_source) values(#{dname}, DATABASE())")
    public int add(Dept dept);
}
```

#### 7. service

###### 7.1 Interface

```java
public interface DeptService {

    public Dept get(Long id);

    public List<Dept> list();

    public Dept add(Dept dept);
}
```



###### 7.2 InterfaceImpl

```java
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public Dept get(Long id) {
        return deptMapper.findById(id);
    }

    @Override
    public List<Dept> list() {
        return deptMapper.findAll();
    }

    @Override
    public Dept add(Dept dept) {
        deptMapper.add(dept);
        return dept;
    }
}
```

#### 8. 主启动类

```java
@MapperScan(value = "com.itcc.springcloud.mapper")
@SpringBootApplication
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
@EnableDiscoveryClient //服务发现
public class DeptProvider8001 {

    public static void main(String[] args) {
        SpringApplication.run(DeptProvider8001.class, args);
    }
}
```



#### 9. 注意事项

```
1. 服务名称（spring.application.name）不能包含下划线
```



## micro_service_cloud---客户端

#### 1. Module Introduction

```
客户端 ---- 端口80
```

#### 2. pom.xml

```xml
<dependency>
    <groupId>com.itcc.micro_service_cloud</groupId>
    <artifactId>micro_service_cloud_api</artifactId>
    <version>${project.version}</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-commons</artifactId>
    <version>RELEASE</version>
    <scope>compile</scope>
</dependency>
<!-- Ribbon相关 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
```

#### 3. Eureka配置

```yml
server:
  port: 80

eureka:
  client:
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/,http://eureka7001.com:7001/eureka/
```

#### 4. Rest Config文件

```java
@Configuration
public class ConfigBean //boot -->spring   applicationContext.xml --- @Configuration配置   ConfigBean = applicationContext.xml
{
    /**
     * RestTemplate
     * RestTemplate提供了多种便捷访问远程Http服务的方法，
     * 是一种简单便捷的访问restful服务模板类，是Spring提供的用于访问Rest服务的客户端模板工具集
     *
     * @return
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
```

#### 5. Controller

使用RestTemplate调用8001端口的服务

```java
@RestController
public class DeptControllerConsumer {

    private static final String REST_URL_PREFIX = "http://localhost:8001";

    /**
     * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
     * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
     */
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/dept/add")
    public Dept add(Dept dept) {
        return restTemplate.postForObject(REST_URL_PREFIX + "/dept/add", dept, Dept.class);
    }

    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/get/" + id, Dept.class);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/list", List.class);
    }

    // 消费者使用服务发现
    @RequestMapping(value = "/consumer/dept/discovery")
    public Object discovery() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/discovery", Object.class);
    }
}
```

#### 6. 主启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class DeptConsumer80 {

    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer80.class, args);
    }
}
```



## Ribbon+RestTemplate实现负载均衡

#### 1. Module Introduction

```
		建立一个新的微服务模块，在RestTemplate请求的基础上，加入Ribbon实现负载均衡，分为官方定义的负载均衡策略以及自定义负载均衡
```

#### 2. pom.xml

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
```

#### 3. Ribbon负载均衡策略

###### 3.1 官方定义的其中策略

```xml-dtd
1. RoundRobinRule 轮询
2. RandomRule 随机
3. AvailabilityFilteringRule
		会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务，然后对剩余的服务列表按照RoundRobinRule策略进行访问
4. WeightedResponseTimeRule
		根据平均响应时间计算所有服务的权重，响应时间越快服务权重越大被选中的概率越高。刚启动时如果统计信息不足，则使用RoundRobinRule策略，等统计信息足够，会切换到WeightedResponseTimeRule
5. RetryRule
		先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试，获取可用的服务
6. BestAvailableRule
		会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
7. ZoneAvoidanceRule
		默认规则,复合判断server所在区域的性能和server的可用性选择服务器
```

###### 3.2 Config中开启Ribbon负载均衡

默认加上@LoadBalanced注解是代表轮询算法，可以修改成其他算法，以随机服务选择算法

```java
//boot -->spring   
// applicationContext.xml --- @Configuration配置   
// ConfigBean = applicationContext.xml
@Configuration
public class ConfigBean 
{
    /**
     * RestTemplate
     * RestTemplate提供了多种便捷访问远程Http服务的方法，
     * 是一种简单便捷的访问restful服务模板类，是Spring提供的用于访问Rest服务的客户端模板工具集
     *
     * @return
     */
    @Bean
    @LoadBalanced  //Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端负载均衡的工具。
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public IRule mySelfRule() {
        return new RandomRule(); // 随机服务选择算法
    }
}
```



#### 4 自定义Ribbon负载均衡

###### 4.1 定义方法

Config类

```java
public class MySelfRule {

    @Bean
    public IRule myRule()
    {
//        return new RandomRule();// Ribbon默认是轮询，我自定义为随机
        //return new RoundRobinRule();// Ribbon默认是轮询，我自定义为随机

        return new MyRibbonRule();// 我自定义为每个服务提供商提供服务5次
    }
}
```

自定义策略类

```java
public class MyRibbonRule extends AbstractLoadBalancerRule {

    private static int total = 0;  // 定义每个服务提供五次轮到下一个
    private static int currentServerIndex = 0;  //当前服务序号

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers(); // 获取到所有能连接到的server
            List<Server> allList = lb.getAllServers(); // 获取到所有的server

            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }

            // 根据自定义规则获取server
            total++;
            if (total >= 5) {
                currentServerIndex = (currentServerIndex + 1) % serverCount;
                total = 0;
            }
            server = upList.get(currentServerIndex);

            if (server == null) {
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            server = null;
            Thread.yield();
        }

        return server;

    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }
}
```



###### 4.2 注意事项

```xml-dtd
自定义的规则不能处于默认的扫描包下，否则就会匹配到所有，不能达到自定义的功能
工程目录：
--com
	--itcc
		--springcloud
			--config
			--controller
			--DeptConsumer80 -- 主启动类
		--RibbonRules
			--MySelfRule
			--MyRibbonRule -- 自定义负载均衡算法，实现AbstractLoadBalancerRule接口，按照github源码修改
```



## Feign+Ribbon实现负载均衡

#### 1. Module Introduction

```xml-dtd
		前面在使用 Ribbon+RestTemplate时，利用 RestTemplate对http请求的封装处理，形成了一套模版化的调用方法。但是在实际开发中，由于对服务依赖的调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用
```

#### 2. pom.xml

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
<!--Spring Boot 2.x 中Feign支持-->
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-openfeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
```

#### 3. Feign接口配置

在api模块中增加@FeignClient模块，详情可见api模块Feign模块

#### 4. 主启动类配置

在springboot2.x之后，主启动类配置发生变化

```java
// basePackages写入主启动包以及api模块所在的包路径名
@EnableFeignClients(basePackages = {"com.itcc.springcloud.service", "com.itcc.springcloud"})
public class DeptConsumer80_Feign_App {

    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer80_Feign_App.class, args);
    }
}
```

#### 5. Controller

配置Feign包装的接口之后不需要再使用RestTemplate服务，controller类如下

```java
@RestController
public class DeptController_Feign {
    @Autowired
    private DeptClientService service;

    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        return this.service.get(id);
    }

    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list() {
        return this.service.list();
    }

    @RequestMapping(value = "/consumer/dept/add")
    public Object add(Dept dept) {
        return this.service.add(dept);
    }
}
```



## Ribbon+Hystrix实现熔断机制

#### 1. Module Introduction

```xml-dtd
		熔断机制是应对雪崩效应的一种微服务链路保护机制。
		当扇出链路的某个微服务不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回"错误"的响应信息。当检测到该节点微服务调用响应正常后恢复调用链路。在SpringCloud框架里熔断机制通过Hystrix实现。Hystrix会监控微服务间调用的状况，当失败的调用到一定阈值，缺省是5秒内20次调用失败就会启动熔断机制。熔断机制的注解是@HystrixCommand。
```

#### 2. pom.xml

要将配置依赖冲突的包去除掉

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>
                spring-cloud-starter-netflix-hystrix
            </artifactId>
            <version>2.1.1.RELEASE</version>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```

#### 3. application.yml配置

同8001端口服务端配置

#### 4. Controller

controller类中体现熔断机制，当请求出现数据错误、服务错误等问题时，就会触发熔断函数，返回一个可处理的备选响应。

```java
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private DiscoveryClient client;

    @HystrixCommand(fallbackMethod = "processHystrix_Get")
    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") Long id) {

        Dept dept = deptService.get(id);

        if (dept == null) {
            throw new RuntimeException("该ID：" + id + "没有没有对应的信息");
        }
        return dept;
    }

    public Dept processHystrix_Get(@PathVariable("id") Long id) {
        return new Dept().setDeptno(id)
                .setDname("该ID：" + id + "没有没有对应的信息,null--@HystrixCommand")
                .setDb_source("no this database in MySQL");
    }
}
```

#### 5. 主启动类

增加@EnableCircuitBreaker注解开启熔断服务，其他代码配置与8001服务端代码一致



## Feign+Hystrix实现服务降级

#### 1. Module Introduction

```
		Hystrix服务降级，其实就是线程池中单个线程障处理，防止单个线程请求时间太长，导致资源长期被占有而得不到释放，从而导致线程池被快速占用完，导致服务崩溃。
```

服务实现见api模块中的服务降级模块

#### 2. 配置信息

###### 2.1 开启Hystrix

```yml
feign:
  hystrix:
    enabled: true
```

###### 2.2 修改默认熔断延时时间

```yml
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 60000 #缺省为1000
```

###### 2.3 允许overriding

```yml
# 2.x之后使用的openfeign包可能需要开启
spring:
  main:
    allow-bean-definition-overriding: true
```



## dashboard服务监控

#### 1. Module Introduction

```xml-dtd
		除了隔离依赖服务的调用以外，Hystrix还提供了准实时的调用监控（Hystrix Dashboard），Hystrix会持续地记录所有通过 Hystrix发起的请求的执行信息，并以统计报表和图形的形式展示给用户，包括每秒执行多少请求多少成功，多少失败等。Netflix通过hystrix-metrics-event-stream项目实现了对以上指标的监控。Spring Cloud也提供了 Hystrix Dashboard的整合，对监控内容转化成可视化界面。
```

#### 2. 使用

```xml-dtd
1. 使用@EnableHystrixDashboard在主启动类开启服务监控
2. 配置文件定义端口号以及 Eureka不向自己注册等信息
3. 被监控的服务端 pom依赖中要加入 actuator依赖
4. http://localhost:9001/hystrix 查看监控主页面
5. 启动一个被监控的服务，不断的使用服务调用，查看 http://localhost:8001/hystrix.stream，ping对应的数据返回情况
```

#### 3. pom.xml

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
            <version>2.2.1.RELEASE</version>
        </dependency>
```

#### 4. application.yml

增加基本配置

```yml
server:
  port: 9001
eureka:
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
```

#### 5. 主启动类开始服务监控模块

```java
@SpringBootApplication
@EnableHystrixDashboard
public class SpringCloudDashboardApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudDashboardApp.class, args);
    }
}
```

#### 6. 被监控模块配置

###### 6.1 pom.xml配置

配置依赖自动开启被监控

```xml
        <!-- actuator监控信息完善 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

###### 6.2 开启后缀识别

可能出现无法识别hystrix.stream后缀的情况，增加一个配置类

```java
@Configuration
public class DashboardConfiguration {

    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
```

## Spring Cloud Config

本节内容查看SpringCloud笔记