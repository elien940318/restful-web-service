package com.changkeeroom.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// HTTP Status code
// 2XX -> 0k
// 4XX -> Client err
// 5XX -> Server err

// 존재하지 않는 User를 Client가 요청할 경우 발생시키는 Exception.
// Response상태를 NOT_FOUND로 두어 Client err임을 명시한다.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
