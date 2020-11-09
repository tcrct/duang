package com.duang.project.controller;

import duang.mvc.common.annotation.Bean;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Bean
public class MainEntity extends BaseEntity {

    @NotEmpty
    private String name;

    @NotNull
    @Range(min = 18, max = 60)
    private Integer age;

    public MainEntity() {
    }

    public MainEntity(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
