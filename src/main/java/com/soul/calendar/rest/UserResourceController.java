package com.soul.calendar.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soul.calendar.entity.Users;
import com.soul.calendar.service.UserResourceService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/user")
public class UserResourceController {

    private UserResourceService userService;

    @Autowired
    public UserResourceController(UserResourceService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody Users user) {
        Users newUser = userService.registerUser(user);
        return new ResponseEntity<>(newUser,HttpStatus.CREATED);
    }

    // find All
    @GetMapping("")
    public ResponseEntity<List<Users>>  listAllUsers() {
        List<Users> userList= userService.listAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    // find by id
    @GetMapping("/{userId}")
    public ResponseEntity<Users> findUserById(@PathVariable Integer userId) {
        Users newUser= userService.findById(userId);
        return new ResponseEntity<>(newUser,HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer userId, @RequestBody Users user) {
        Users newUser= userService.update(user);
        return new ResponseEntity<>(newUser,HttpStatus.OK);
    }

}
