package com.yaoxiong.retail.enums;

public enum OrderDetailStatusEnum {
    CREATE(0,"create order detail"),
    PLACE(1,"place order detail"),
    DELETE(2,"delete order detail")
    ;


    private Integer status;
    private String comment;

    OrderDetailStatusEnum(Integer status, String comment) {
        this.status = status;
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
