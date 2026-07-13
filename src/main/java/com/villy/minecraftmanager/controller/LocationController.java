package com.villy.minecraftmanager.controller;

import com.villy.minecraftmanager.controller.request.LocationRequest;
import com.villy.minecraftmanager.controller.response.LocationDetailsResponse;
import com.villy.minecraftmanager.controller.response.LocationResponse;
import com.villy.minecraftmanager.entity.Location;
import com.villy.minecraftmanager.entity.World;
import com.villy.minecraftmanager.mapper.LocationMapper;
import com.villy.minecraftmanager.service.LocationService;
import com.villy.minecraftmanager.service.WorldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final WorldService worldService;

    @GetMapping("/worlds/{worldId}/locations")
    public ResponseEntity<List<LocationResponse>> findByWorldId(@PathVariable UUID worldId) {
        return ResponseEntity.ok(
                locationService.findByWorldId(worldId)
                        .stream()
                        .map(LocationMapper::toResponse)
                        .toList());
    }

    @PostMapping("/worlds/{worldId}/locations")
    public ResponseEntity<LocationResponse> save(@PathVariable UUID worldId, @RequestBody LocationRequest request) {
        World world = worldService.findById(worldId);
        Location location = LocationMapper.toEntity(request, world);
        Location savedLocation = locationService.save(location);
        URI uriLocation = URI.create("/locations/" + savedLocation.getId());

        return ResponseEntity.created(uriLocation).body(LocationMapper.toResponse(savedLocation));
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationDetailsResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(LocationMapper.toDetailResponse(locationService.findById(id)));
    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<LocationResponse> update(@PathVariable UUID id, @RequestBody LocationRequest request) {
        Location location = LocationMapper.toEntity(request, locationService.findById(id).getWorld());
        location.setId(id);
        Location savedLocation = locationService.update(location);

        return ResponseEntity.ok(LocationMapper.toResponse(savedLocation));
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        locationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
