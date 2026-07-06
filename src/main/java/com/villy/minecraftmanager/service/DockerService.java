package com.villy.minecraftmanager.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.villy.minecraftmanager.entity.World;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DockerService {

    private final DockerClient dockerClient;

    public String createMinecraftContainer(World world) {
        var container = dockerClient.createContainerCmd("itzg/minecraft-server")
                .withName("world-" + world.getId())
                .withEnv(
                        "EULA=TRUE",
                        "TYPE=PAPER",
                        "VERSION=26.2",

                        "MEMORY=2G",

                        "MODE=" + world.getGameMode().name().toLowerCase(),
                        "DIFFICULTY=" + world.getDifficulty().name().toLowerCase(),
                        "SEED=" + (world.getSeed() != null ? world.getSeed() : ""),

                        "ENABLE_RCON=true",
                        "RCON_PASSWORD=123456"
                )
                .withExposedPorts(new ExposedPort(25565))
                .withPortBindings(
                        new PortBinding(
                                Ports.Binding.bindPort(25565),
                                new ExposedPort(25565)
                        )
                )
                .exec();

        return container.getId();
    }

    public void startContainerById(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public void stopContainerById(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    public void restartContainerById(String containerId) {
        dockerClient.restartContainerCmd(containerId).exec();
    }

    public void deleteContainerById(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }

}
