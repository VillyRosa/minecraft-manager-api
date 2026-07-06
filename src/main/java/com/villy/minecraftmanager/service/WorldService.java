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
    private final PortService portService;
    private final DockerService dockerService;

    public List<World> findAll() {
        return worldRepository.findAll();
    }

    public World findById(UUID id) {
        Optional<World> world = worldRepository.findById(id);
        return world.orElse(null);
    }

    public World save(World world) {
        world.setStatus(ContainerStatus.CREATED);
        world.setPort(portService.findAvaliablePort());
        World savedWorld = worldRepository.save(world);

        try {
            String containerId = dockerService.createMinecraftContainer(savedWorld);
            dockerService.startContainerById(containerId);

            savedWorld.setContainerId(containerId);
            savedWorld.setStatus(ContainerStatus.RUNNING);
        } catch (Exception e) {
            e.printStackTrace();
            savedWorld.setStatus(ContainerStatus.ERROR);
        }

        return worldRepository.save(savedWorld);
    }

    public World update(World world) {
        World existingWorld = findById(world.getId());
        existingWorld.setName(world.getName());
        existingWorld.setDescription(world.getDescription());

        return save(existingWorld);
    }

    public void deleteById(UUID id) {
        var world = findById(id);
        dockerService.deleteContainerById(world.getContainerId());
        worldRepository.delete(world);
    }

    public void startContainerByWorldId(UUID id) {
        World world = findById(id);
        dockerService.startContainerById(world.getContainerId());
        world.setStatus(ContainerStatus.RUNNING);
        worldRepository.save(world);
    }

    public void stopContainerByWorldId(UUID id) {
        World world = findById(id);
        dockerService.stopContainerById(world.getContainerId());
        world.setStatus(ContainerStatus.STOPPED);
        worldRepository.save(world);
    }

    public void restartContainerByWorldId(UUID id) {
        World world = findById(id);
        dockerService.restartContainerById(world.getContainerId());
        world.setStatus(ContainerStatus.RUNNING);
        worldRepository.save(world);
    }
}
