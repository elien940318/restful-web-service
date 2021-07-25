package com.changkeeroom.restfulwebservice.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {    
    @Autowired
    private UserDaoService service;

    @GetMapping(path = "/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    // @GetMapping(path = "/users/{id}")
    // public User retrieveUser(@PathVariable int id) {
    //     User user = service.findOne(id);

    //     // 존재하지 않는 유저를 요청하는 경우 우리가 만든 UserNotFoundException 으로 Exception 호출하도록 설정.
    //     if (user == null)
    //         throw new UserNotFoundException(String.format("ID[%s] not found", id));

    //     //EntityModel<User> model = new EntityModel<>(user);  // deprecated
        
    //     return user;
    // }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<EntityModel<User>> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);

        // 존재하지 않는 유저를 요청하는 경우 우리가 만든 UserNotFoundException 으로 Exception 호출하도록 설정.
        if (user == null)
            throw new UserNotFoundException(String.format("ID[%s] not found", id));

        // EntityModel<> model 에 보내줄 DVO 클래스를 static method인 EntityModel.of() 로 넣어준다.
        // 함께 보내줄 _link를 add method를 사용하여 WebMvcLinkBuilder로 대상 method를 wrapping하여 추가한다.
        EntityModel<User> model = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());        
        model.add(linkTo.withRel("all-users"));
        
        return ResponseEntity.ok(model);
    }
        

    // JSON, XML 등의 형식으로 받기 위해서는 RequestBody로 받아야함...
    // 새로운 User를 추가합니다.
    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();
        
        return ResponseEntity.created(location).build();
    }    

    @PutMapping(path = "/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User updateuser = service.modifyOne(id, user);
        if (updateuser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return updateuser;
    }

    @DeleteMapping(path = "/users/{id}")
    public User deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);
        
        if(user == null)
            throw new UserNotFoundException(String.format("ID[%s] not found", id));

        return user;
    }
}
