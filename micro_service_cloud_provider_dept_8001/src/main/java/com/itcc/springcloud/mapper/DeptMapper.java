package com.itcc.springcloud.mapper;

import com.itcc.springcloud.entity.Dept;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

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

