package com.example.simpleDropbox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simpleDropbox.model.MObject;


public interface MObjectRepository extends JpaRepository<MObject,Long> {
    
    public Optional<MObject> findFirstByName(String name);
}
