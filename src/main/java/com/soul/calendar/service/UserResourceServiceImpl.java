package com.soul.calendar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soul.calendar.dao.UserRepository;
import com.soul.calendar.entity.Users;
import com.soul.calendar.exception.UserNotFoundException;

@Service
public class UserResourceServiceImpl implements UserResourceService {

    private UserRepository userRepository;

    @Autowired
    public UserResourceServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users registerUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public List<Users> listAll() {
        return userRepository.findAll();
    }

    @Override
    public Users update(Users user) {
        return userRepository.save(user);
    }

    @Override
    public Users findById(int id) {
        Optional<Users> result = userRepository.findById(id);
        Users theUser = null;
        if (result.isPresent()) {
            theUser = result.get();
        } else {
            throw new UserNotFoundException("Did not find employee id - " + id);
        }

        return theUser;
    }

}
