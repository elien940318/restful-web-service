package com.changkeeroom.restfulwebservice.user;

import java.util.List;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// alt + shift + O : 필요없는 import 삭제
@RestController
@RequestMapping("/admin")
public class AdminUserController {    
    @Autowired
    private UserDaoService service;

    @GetMapping(path = "/users")
    public MappingJacksonValue retrieveAllUsers() {

        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    /**
     * 
     * <<< Version 관리 방법 >>> 1,2 : Browser 실행가능, 3,4 : Browser 실행불가
     * 1. path에 버전 직접 기입한다. URL Versioning                      : /v1/users/1            @GetMapping(value="/v1/users/{id}")
     * 2. params로 요청한다. Request Parameter Versioning               : /users/1/?version=1    @GetMapping(value="/users/{id}", params="version=1")
     * 3. header로 요청한다. (Custom) Headers Versioning(accept header) : /users/1               @GetMapping(value="/users/{id}", headers="X-API-VERSION=1")
     *                                                                                          {"X-API-VERSION" : "2"}
     * 4. MIME로 요청한다. Media type Versioning                        : /users/1               @GetMapping(value="/users/{id}", produces="application/vnd.company.appv1+json")
     * (Multi purpost Internet Mail Extension)                                                  {"Accept" : "application/vnd.company.appv1+json"}
     * 
     */
    
    @GetMapping(value="/users/{id}", produces = "application/vnd.company.appv1+json")
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
        User user = service.findOne(id);
        // 존재하지 않는 유저를 요청하는 경우 우리가 만든 UserNotFoundException 으로 Exception 호출하도록 설정.
        if (user == null)
            throw new UserNotFoundException(String.format("ID[%s] not found", id));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        
        return mapping;
    }

    //@GetMapping(value = "/users/{id}/", headers = "X-API-VERSION=2")
    @GetMapping(value="/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        User user = service.findOne(id);

        // 존재하지 않는 유저를 요청하는 경우 우리가 만든 UserNotFoundException 으로 Exception 호출하도록 설정.
        if (user == null)
            throw new UserNotFoundException(String.format("ID[%s] not found", id));

        // User -> User2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2);
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn", "grade");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);
        
        return mapping;
    }
}
