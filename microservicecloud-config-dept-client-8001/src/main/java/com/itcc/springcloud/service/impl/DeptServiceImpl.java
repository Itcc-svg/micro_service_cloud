package com.itcc.springcloud.service.impl;

import com.itcc.springcloud.entity.Dept;
import com.itcc.springcloud.mapper.DeptMapper;
import com.itcc.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
