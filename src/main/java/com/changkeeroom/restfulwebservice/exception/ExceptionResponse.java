package com.changkeeroom.restfulwebservice.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 출력되는 Exception err body를 우리가 보여주고 싶은 내용만 보여줄 수 있도록 POJO 클래스 생성.
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;
}
