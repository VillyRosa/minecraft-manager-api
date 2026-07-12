package com.villy.minecraftmanager.service;

import com.villy.minecraftmanager.domain.ImportedWorld;
import com.villy.minecraftmanager.entity.World;
import com.villy.minecraftmanager.enums.ContainerStatus;
import com.villy.minecraftmanager.repository.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorldService {

    private final WorldRepository worldRepository;
    private final WorldImportService worldImportService;
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

        return provisionWorld(world, null);
    }

    public World save(MultipartFile file) throws IOException {
        ImportedWorld importedWorld = worldImportService.importWorld(file);

        World world = World.builder()
                .name(importedWorld.name())
                .status(ContainerStatus.CREATED)
                .gameMode(importedWorld.gameMode())
                .difficulty(importedWorld.difficulty())
                .port(portService.findAvaliablePort())
                .build();

        return provisionWorld(world, importedWorld.worldPath());
    }

    private World provisionWorld(World world, Path worldDir) {
        World savedWorld = worldRepository.save(world);
        boolean imported = worldDir != null;

        try {
            if (imported) {
                worldImportService.moveWorldToWorldsDirectory(worldDir, savedWorld.getId());
            }

            String containerId = dockerService.createMinecraftContainer(savedWorld, imported);

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
