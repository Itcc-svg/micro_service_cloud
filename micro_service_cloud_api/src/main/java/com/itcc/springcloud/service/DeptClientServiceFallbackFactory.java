package com.itcc.springcloud.service;

import java.util.ArrayList;
import java.util.List;

import com.itcc.springcloud.entity.Dept;
import org.springframework.stereotype.Component;


import feign.hystrix.FallbackFactory;
import org.springframework.web.bind.annotation.PathVariable;

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




