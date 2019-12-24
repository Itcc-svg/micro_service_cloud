package com.itcc.RibbonRules;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;

public class MySelfRule {

    @Bean
    public IRule myRule()
    {
//        return new RandomRule();// Ribbon默认是轮询，我自定义为随机
        //return new RoundRobinRule();// Ribbon默认是轮询，我自定义为随机

        return new MyRibbonRule();// 我自定义为每个服务提供商提供服务5次
    }
}
