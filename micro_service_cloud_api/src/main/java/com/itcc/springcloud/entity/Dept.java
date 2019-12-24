package com.itcc.springcloud.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

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