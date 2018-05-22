package com.example.xueming_wu.ant.rxjava_demo.entity;

/**
 * @author xueming_wu
 * @date 2018/5/22 0022
 * TODO:
 */

public class Data {
    private int id;
    private String name;

    private Data(Builder builder) {
        setId(builder.id);
        setName(builder.name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class Builder {
        private int id;
        private String name;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Data build() {
            return new Data(this);
        }
    }
}
