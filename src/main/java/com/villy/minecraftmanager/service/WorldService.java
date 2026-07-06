package com.villy.minecraftmanager.service;

import com.villy.minecraftmanager.entity.World;
import com.villy.minecraftmanager.enums.ContainerStatus;
import com.villy.minecraftmanager.repository.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorldService {

    private final WorldRepository worldRepository;

    public List<World> findAll() {
        return worldRepository.findAll();
    }

    public World findById(UUID id) {
        Optional<World> world = worldRepository.findById(id);
        return world.orElse(null);
    }

    public World save(World world) {
        world.setStatus(ContainerStatus.CREATED);
        return worldRepository.save(world);
    }

    public World update(World world) {
        World existingWorld = findById(world.getId());
        existingWorld.setName(world.getName());
        existingWorld.setDescription(world.getDescription());

        return save(existingWorld);
    }

    public void deleteById(UUID id) {
        var world = findById(id);
        worldRepository.delete(world);
    }

}
