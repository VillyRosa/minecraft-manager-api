package com.villy.minecraftmanager.controller;

import com.villy.minecraftmanager.controller.request.WorldRequest;
import com.villy.minecraftmanager.controller.response.WorldResponse;
import com.villy.minecraftmanager.entity.World;
import com.villy.minecraftmanager.mapper.WorldMapper;
import com.villy.minecraftmanager.service.WorldExportService;
import com.villy.minecraftmanager.service.WorldService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/worlds")
@RequiredArgsConstructor
public class WorldController {

    @Value("${minecraft.host}")
    private String host;

    private final WorldService worldService;
    private final WorldExportService worldExportService;

    @GetMapping
    public ResponseEntity<List<WorldResponse>> findAll() {
        return ResponseEntity.ok(
                worldService.findAll()
                        .stream()
                        .map(world -> WorldMapper.toResponse(world, host))
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorldResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(WorldMapper.toResponse(worldService.findById(id), host));
    }

    @PostMapping
    public ResponseEntity<WorldResponse> save(@RequestBody WorldRequest request) {
        WorldResponse response = WorldMapper.toResponse(worldService.save(WorldMapper.toEntity(request)), host);
        URI location = URI.create("/worlds/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/import")
    public ResponseEntity<WorldResponse> importWorld(@RequestParam("file") MultipartFile file) throws IOException {
        WorldResponse response = WorldMapper.toResponse(worldService.save(file), host);
        URI location = URI.create("/worlds/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<Resource> exportWorld(@PathVariable UUID id) throws IOException {
        Path zip = worldExportService.exportWorld(id);
        Resource resource = new FileSystemResource(zip);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"world.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorldResponse> updateById(@PathVariable UUID id, @RequestBody WorldRequest request) {
        World world = WorldMapper.toEntity(request);
        world.setId(id);

        return ResponseEntity.ok(WorldMapper.toResponse(worldService.update(world), host));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        worldService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startContainer(@PathVariable UUID id) {
        worldService.startContainerByWorldId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<Void> stopContainer(@PathVariable UUID id) {
        worldService.stopContainerByWorldId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restart")
    public ResponseEntity<Void> restartContainer(@PathVariable UUID id) {
        worldService.restartContainerByWorldId(id);
        return ResponseEntity.noContent().build();
    }

}
