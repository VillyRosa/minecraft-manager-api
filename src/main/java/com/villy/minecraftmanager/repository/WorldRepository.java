package com.villy.minecraftmanager.repository;

import com.villy.minecraftmanager.entity.World;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorldRepository extends JpaRepository<World, UUID> {
}
