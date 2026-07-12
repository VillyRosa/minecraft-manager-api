package com.villy.minecraftmanager.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
import com.villy.minecraftmanager.entity.World;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DockerService {

    @Value("${minecraft.worlds-directory}")
    private String worldsDirectory;
    private final DockerClient dockerClient;

    public String createMinecraftContainer(World world, boolean imported) {
        var container = dockerClient.createContainerCmd("itzg/minecraft-server")
                .withName("world-" + world.getId())
                .withEnv(createEnvironment(world, imported))
                .withHostConfig(createHostConfig(world))
                .withExposedPorts(new ExposedPort(25565))
                .exec();

        return container.getId();
    }

    private String[] createEnvironment(World world, boolean imported) {
        List<String> env = new ArrayList<>();

        env.add("EULA=TRUE");
        env.add("TYPE=PAPER");
        env.add("VERSION=26.2");
        env.add("MEMORY=" + world.getMemoryMb() + "M");
        env.add("MAX_PLAYERS=" + world.getMaxPlayers());
        env.add("VIEW_DISTANCE=" + world.getViewDistance());
        env.add("SIMULATION_DISTANCE=" + world.getSimulationDistance());
        env.add("MOTD=" + world.getMotd());
        env.add("ENABLE_RCON=true");
        env.add("RCON_PASSWORD=123456");

        if (!imported) {
            env.add("MODE=" + world.getGameMode().name().toLowerCase());
            env.add("DIFFICULTY=" + world.getDifficulty().name().toLowerCase());
            env.add("SEED=" + (world.getSeed() != null ? world.getSeed() : ""));
        }

        return env.toArray(String[]::new);
    }

    private HostConfig createHostConfig(World world) {
        String worldPath = Paths.get(worldsDirectory, world.getId().toString())
                .toAbsolutePath()
                .toString();

        return HostConfig.newHostConfig()
                .withBinds(
                        new Bind(
                                worldPath,
                                new Volume("/data")
                        )
                )
                .withPortBindings(
                        new PortBinding(
                                Ports.Binding.bindPort(world.getPort()),
                                new ExposedPort(25565)
                        )
                );
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
