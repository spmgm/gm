package com.spmgm.gm.repository;

import com.spmgm.gm.entity.Activity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActivityRepository extends CrudRepository<Activity, String> {
    Optional<Activity> findByCode(String code);
}
