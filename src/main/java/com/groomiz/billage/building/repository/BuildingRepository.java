package com.groomiz.billage.building.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groomiz.billage.building.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {

}
