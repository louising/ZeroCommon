package com.zero.common.demo.domain;

import java.util.Date;

import com.zero.core.domain.AbstractEntity;
import com.zero.core.util.BaseUtils;

public class Person extends AbstractEntity implements Cloneable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
    private String sex;
    private Date birthday;
    private Double salary;
    private Boolean state = true;

    public Person() {
    }

    public Person(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Person(Integer id, String name, Date date, Double value) {
        super();
        this.id = id;
        this.name = name;
        this.birthday = date;
        this.salary = value;
    }

    public Person(Integer id, String name, String sex, Date date, Double value) {
        super();
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = date;
        this.salary = value;
    }

    @Override
    public Person clone() {
        try {
            return (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date date) {
        this.birthday = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double value) {
        this.salary = value;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Boolean getState() {
        if (state == null)
            if (id != null && id != 0) {
                state = id % 3 == 0;
            }
        return state;
    }

    public void setState(Boolean flag) {
        this.state = flag;
    }

    protected void appendFields(StringBuilder sb) {
        appendField(sb, "id", id);
        appendField(sb, "name", name);
        appendField(sb, "sex", sex);
        appendField(sb, "birthday", birthday);
        appendField(sb, "salary", salary);
        appendField(sb, "state", state);
    }
    
    @Override
    public int hashCode() {
        return BaseUtils.hashCode(id, name, sex, birthday, salary, state);        
    }
}
