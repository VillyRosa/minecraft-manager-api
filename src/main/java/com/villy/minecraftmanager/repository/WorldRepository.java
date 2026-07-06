package com.villy.minecraftmanager.repository;

import com.villy.minecraftmanager.entity.World;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface WorldRepository extends JpaRepository<World, UUID> {

    @Query("SELECT w.port FROM World w ORDER BY w.port")
    List<Integer> findAllPorts();

}
