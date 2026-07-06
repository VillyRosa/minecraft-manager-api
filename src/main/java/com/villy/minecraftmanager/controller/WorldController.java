package com.villy.minecraftmanager.controller;

import com.villy.minecraftmanager.controller.request.WorldRequest;
import com.villy.minecraftmanager.controller.response.WorldResponse;
import com.villy.minecraftmanager.entity.World;
import com.villy.minecraftmanager.mapper.WorldMapper;
import com.villy.minecraftmanager.service.WorldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/worlds")
@RequiredArgsConstructor
public class WorldController {

    private final WorldService worldService;

    @GetMapping
    public ResponseEntity<List<WorldResponse>> findAll() {
        return ResponseEntity.ok(worldService.findAll().stream().map(WorldMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorldResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(WorldMapper.toResponse(worldService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<WorldResponse> save(@RequestBody WorldRequest request) {
        WorldResponse response = WorldMapper.toResponse(worldService.save(WorldMapper.toEntity(request)));
        URI location = URI.create("/worlds/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorldResponse> updateById(@PathVariable UUID id, @RequestBody WorldRequest request) {
        World world = WorldMapper.toEntity(request);
        world.setId(id);

        return ResponseEntity.ok(WorldMapper.toResponse(worldService.update(world)));
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
