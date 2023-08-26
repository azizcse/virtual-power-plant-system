package com.tech.virtualpower.repository;

import com.tech.virtualpower.model.Battery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {
    List<Battery> findByPostcodeBetweenOrderByNameAsc(String startPostcode, String endPostcode);
    Page<Battery> findByCapacityLessThan(int minCapacity, Pageable pageable);
}
