package com.ways.framework.utils;


/**
 * 
 * @author Administrator
 *
 */
public class Pager {
    private int totalPages = 0;
    private int totalObjects = 0;
    private int pageNumber = 1;
    private int pageSize = 20;
    private boolean pageAble = true;

    private int firstResult;


    public void calc(){
        totalPages =  totalObjects % pageSize == 0 ? totalObjects
                / pageSize : totalObjects / pageSize + 1;
        if(totalPages>0){
        	if(pageNumber>totalPages) pageNumber=totalPages;
        }else{
        	pageNumber=1;
        }
        firstResult = (pageNumber - 1) * pageSize;
    }

    public boolean isPageAble() {
        return pageAble;
    }

    public void setPageAble(boolean pageAble) {
        this.pageAble = pageAble;
    }

    public int getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(int param) {
        this.totalObjects = param;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int param) {
        this.totalPages = param;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String toString(){
        return("\ntotalPages:" + totalPages +
                "\ntotalObjects:" + totalObjects +
                "\npageNumber:" + pageNumber +
                "\npageSize:" + pageSize +
                "\npageAble:" + pageAble +
                "\nfirstResult:" + firstResult);
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }
}
