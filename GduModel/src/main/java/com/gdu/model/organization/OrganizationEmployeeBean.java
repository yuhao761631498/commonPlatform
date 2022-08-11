package com.gdu.model.organization;

import java.io.Serializable;
import java.util.List;

/**
 * 组织机构员工信息
 */
public class OrganizationEmployeeBean implements Serializable {

    private List<RecordsDTO> records;
    private int total;
    private int size;
    private int current;
    private boolean searchCount;
    private int pages;

    public List<RecordsDTO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsDTO> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public static class RecordsDTO implements Serializable{
        private int id;
        private String employeeName;
        private String employeeIdentity;
        private String employeeTel;
        private String deptCode;
        private String deptName;
        private int employeeSort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getEmployeeIdentity() {
            return employeeIdentity;
        }

        public void setEmployeeIdentity(String employeeIdentity) {
            this.employeeIdentity = employeeIdentity;
        }

        public String getEmployeeTel() {
            return employeeTel;
        }

        public void setEmployeeTel(String employeeTel) {
            this.employeeTel = employeeTel;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getEmployeeSort() {
            return employeeSort;
        }

        public void setEmployeeSort(int employeeSort) {
            this.employeeSort = employeeSort;
        }
    }
}
