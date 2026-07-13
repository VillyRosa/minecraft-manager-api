package com.villy.minecraftmanager.service;

import com.villy.minecraftmanager.entity.Location;
import com.villy.minecraftmanager.exception.ResourceNotFoundException;
import com.villy.minecraftmanager.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> findByWorldId(UUID worldId) {
        return locationRepository.findByWorldId(worldId);
    }

    public Location findById(UUID id) {
        return locationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Location with id " + id + " not found!"));
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public Location update(Location location) {
        Location existingLocation = findById(location.getId());
        existingLocation.setName(location.getName());
        existingLocation.setDescription(location.getDescription());
        existingLocation.setDimension(location.getDimension());
        existingLocation.setX(location.getX());
        existingLocation.setY(location.getY());
        existingLocation.setZ(location.getZ());
        existingLocation.setYaw(location.getYaw());
        existingLocation.setPitch(location.getPitch());

        return locationRepository.save(location);
    }

    public void deleteById(UUID id) {
        Location location = findById(id);
        locationRepository.delete(location);
    }

}
