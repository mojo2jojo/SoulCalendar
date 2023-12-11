package com.soul.calendar.service;

import java.util.List;

import com.soul.calendar.entity.Users;

public interface UserResourceService {

    public Users registerUser(Users user);

    public List<Users> listAll();

    public Users update(Users user);

    public Users findById(int id);

}
