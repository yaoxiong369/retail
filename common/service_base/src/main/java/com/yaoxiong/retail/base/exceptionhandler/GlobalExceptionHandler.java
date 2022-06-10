package com.yaoxiong.retail.base.exceptionhandler;


import com.yaoxiong.retail.base.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //Specify what exception occurs to execute this method
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error().message("Global exception handling performed..");
    }


    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.error().message("Arithmeticexception exception handling performed..");
    }

    //Custom exception
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public Result error(GuliException e) {
        log.error(e.getMessage());
        e.printStackTrace();

        return Result.error().code(e.getCode()).message(e.getMsg());
    }

    //Custom exception
    @ExceptionHandler(TokenParsingFailedException.class)
    @ResponseBody
    public Result error(TokenParsingFailedException e) {
        log.error(e.getMessage());
        e.printStackTrace();

        return Result.error().code(e.getCode()).message(e.getMsg());
    }

    //Validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result error(MethodArgumentNotValidException e) {
        BindingResult re = e.getBindingResult();
        if(re.hasErrors()){
            List<ObjectError> errors = re.getAllErrors();
            if(!errors.isEmpty()){
                return Result.error().code(20003).message(errors.get(0).getDefaultMessage());
            }
        }
        return Result.error().code(20003);
    }
}
