package com.villy.minecraftmanager.repository;

import com.villy.minecraftmanager.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
