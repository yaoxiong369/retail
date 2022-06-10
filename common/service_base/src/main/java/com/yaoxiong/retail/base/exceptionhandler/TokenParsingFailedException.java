package com.yaoxiong.retail.base.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenParsingFailedException extends RuntimeException{
    private Integer code;
    private String msg;
}
