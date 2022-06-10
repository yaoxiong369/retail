package com.yaoxiong.retail.enums;

import java.util.Arrays;

public enum OrderStatusEnum {
    CREATE(0,"create order"),
    FINISH(1,"finish order"),
    DELETE(2,"delete order")
    ;


    private Integer status;
    private String comment;

    OrderStatusEnum(Integer status, String comment) {
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

    public static String getComment(Integer status){
        OrderStatusEnum arrObj[] = OrderStatusEnum.values();
        for (OrderStatusEnum obj : arrObj) {
            if (status.intValue() == obj.getStatus().intValue()) {
                return obj.getComment();
            }
        }
        return "";
    }
}
