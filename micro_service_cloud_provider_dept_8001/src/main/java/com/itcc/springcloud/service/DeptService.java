package com.itcc.springcloud.service;

import com.itcc.springcloud.entity.Dept;

import java.util.List;


public interface DeptService {

    public Dept get(Long id);

    public List<Dept> list();

    public Dept add(Dept dept);
}
