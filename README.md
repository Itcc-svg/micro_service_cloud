1. 项目组成部分
    ```xml-dtd
    1. micro_service_cloud7001
        eureka 服务器
    2. micro_service_cloud_provider_dept_8001
        服务提供者，并将服务注册进eureka服务器中，可通过7001端口查看
    3. microservicecloud-consumer-dept-80
        服务消费者
    4. micro_service_cloud_api
        通用模块定义 ----  Dept实体类
    ```

2. 相关知识点
    ```xml-dtd
    1. eureka自我保护 ---- 一段时间不操作爆红
        EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
        一句话：某时刻某一个微服务不可用了，eureka不会立刻清理，依旧会对该微服务的信息进行保存
    2. spring cloud yml配置$project.version$获取不到值问题处理
        a. application.properties配置文件，会用${parameter}去读取pom文件的变量
        b. application.yml，${parameter}则是读取文件内部的变量值若想读取pom.xml文件的变量应该使用"@project.version@"
    3. 
    ```
    
    **eureka自我保护机制**
    ```xml-dtd
       什么是自我保护模式？
           默认情况下，如果EurekaServer在一定时间内没有接收到某个微服务实例的心跳，EurekaServer将会注销该实例（默认90秒）。但是当网络分区故障发生时，微服务与EurekaServer之间无法正常通信，以上行为可能变得非常危险了——因为微服务本身其实是健康的，此时本不应该注销这个微服务。Eureka通过“自我保护模式”来解决这个问题——当EurekaServer节点在短时间内丢失过多客户端时（可能发生了网络分区故障），那么这个节点就会进入自我保护模式。一旦进入该模式，EurekaServer就会保护服务注册表中的信息，不再删除服务注册表中的数据（也就是不会注销任何微服务）。当网络故障恢复后，该Eureka Server节点会自动退出自我保护模式。
       在自我保护模式中，Eureka Server会保护服务注册表中的信息，不再注销任何服务实例。当它收到的心跳数重新恢复到阈值以上时，该Eureka Server节点就会自动退出自我保护模式。它的设计哲学就是宁可保留错误的服务注册信息，也不盲目注销任何可能健康的服务实例。一句话讲解：好死不如赖活着
       综上，自我保护模式是一种应对网络异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务（健康的微服务和不健康的微服务都会保留），也不盲目注销任何健康的微服务。使用自我保护模式，可以让Eureka集群更加的健壮、稳定。
       在Spring Cloud中，可以使用eureka.server.enable-self-preservation = false 禁用自我保护模式。
    ```
    **eureka集群配置**
    ```xml-dtd
        1. 修改 /etc/hosts文件，使127.0.0.1同时映射eureka7001/7002/7003三个地址
        2. 增加7002 7003服务器，并修改三个服务器yml文件，形成eureka小集群
        3. 同时启动，进入7001端口查看，可以发现牵着7002 7003，另外两个也是一样
    ```
    
    **Eureka与Zookeeper**
    ```xml-dtd
        1. Eureka 遵守AP原则， Zookeeper遵守CP原则
    ```
    