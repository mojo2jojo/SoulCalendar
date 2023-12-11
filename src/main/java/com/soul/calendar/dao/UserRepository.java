package com.soul.calendar.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soul.calendar.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

}
