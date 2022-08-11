package com.gdu.command.ui.setting;

public class Dept extends Node<Integer>{
    private int id;
    private int parentId;
    private String name;
    private String deptCode;

    public Dept() {

    }

    public Dept(int id, int parentId, String name, String dept_code) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.deptCode = dept_code;
    }

    @Override
    public Integer get_id() {
        return id;
    }

    @Override
    public Integer get_parentId() {
        return parentId;
    }

    @Override
    public String get_label() {
        return name;
    }

    @Override
    public boolean parent(Node dest) {
        if (id == ((Integer) dest.get_parentId()).intValue()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean child(Node dest) {
        if (parentId == ((Integer) dest.get_id()).intValue()) {
            return true;
        }
        return false;
    }

    @Override
    public String deptCode() {
        return deptCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
}
