package com.villy.minecraftmanager.service;

import com.villy.minecraftmanager.repository.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PortService {

    @Value("${minecraft.port-range.start}")
    private Integer start;

    @Value("${minecraft.port-range.end}")
    private Integer end;

    private final WorldRepository worldRepository;

    public Integer findAvaliablePort() {
        Set<Integer> usedPorts = new HashSet<>(worldRepository.findAllPorts());

        for (int port = start; port <= end; port++) {
            if (!usedPorts.contains(port)) {
                return port;
            }
        }

        throw new IllegalStateException("No available port");
    }

}
