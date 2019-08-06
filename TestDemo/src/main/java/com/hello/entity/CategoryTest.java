package com.hello.entity;

public class CategoryTest {

    private String id;
    private String name;
    private String iconId;
    private String iconUrl;
    private String remark;

    public CategoryTest() {
    }

    public CategoryTest(String id, String name, String iconId, String iconUrl, String remark) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
        this.iconUrl = iconUrl;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "CategoryTest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", iconId='" + iconId + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
