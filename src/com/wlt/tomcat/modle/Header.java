package com.wlt.tomcat.modle;

/**
 * @author 王立腾
 * @date 2019-09-14 19:01
 */
public class Header {

    private String name;
    private String value;

    public Header() {
    }

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Header setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Header setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "Header{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
