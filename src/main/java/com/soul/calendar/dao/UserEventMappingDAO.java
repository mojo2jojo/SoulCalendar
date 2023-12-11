package com.soul.calendar.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soul.calendar.entity.UserEventMapping;

public interface UserEventMappingDAO extends JpaRepository<UserEventMapping, Integer> {
    List<UserEventMapping> findAllByUserId(Integer userId);
}