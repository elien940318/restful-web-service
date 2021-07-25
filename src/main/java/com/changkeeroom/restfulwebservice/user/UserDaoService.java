package com.changkeeroom.restfulwebservice.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;


/**
 * UserDaoService
 */
@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int userCount = 3;

    static {
        users.add(new User(1, "Chang-gi", new Date(), "1234", "940318-1234567"));
        users.add(new User(2, "Seong-eun", new Date(), "5678", "950318-1234567"));
        users.add(new User(3, "stella", new Date(), "0000", "960318-1234567"));
    }    

    public List<User> findAll() {
        return users;
    }

    public User findOne(int id) {
        for (User user : users) {
            if(user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User save(User user) {
        if(user.getId() == null)
            user.setId(++userCount);
        
        users.add(user);
        return user;
    }

    public User deleteById(int id) {
        // Iterator를 활용하여 List 접근.
        Iterator<User> iter = users.iterator();

        while (iter.hasNext()) {
            User user = iter.next();
            if(user.getId() == id) {
                iter.remove();
                return user;
            }
        }
        return null;
    }

    public User modifyOne(int id, User newusr) {
        //Iterator<User> iter = users.iterator();
        for (User oldusr : users) {
            if(oldusr.getId() == id) {
                int oldusridx = users.indexOf(oldusr);
                users.set(oldusridx, newusr);
                
                return newusr;
            }
        }

        // while (iter.hasNext()) {
        //     //User updateuser = iter.next();
            
        //     if (updateuser.getId() == id) {
        //         // updateuser.setName(user.getName());
        //         // updateuser.setJoinDate(user.getJoinDate());
        //         // updateuser.setPassword(user.getPassword());
        //         // updateuser.setSsn(user.getSsn());
        //         return updateuser;
        //     }
        // }
        return null;
    }
}