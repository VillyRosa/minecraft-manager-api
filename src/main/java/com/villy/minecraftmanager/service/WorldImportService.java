package com.villy.minecraftmanager.service;

import com.villy.minecraftmanager.domain.ImportedWorld;
import com.villy.minecraftmanager.enums.Difficulty;
import com.villy.minecraftmanager.enums.GameMode;
import jakarta.ws.rs.BadRequestException;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class WorldImportService {

    public ImportedWorld importWorld(MultipartFile file) throws IOException {
        validateFile(file);
        Path tempFile = Files.createTempFile("minecraft-", ".zip");
        file.transferTo(tempFile);
        Path extractDir = Files.createTempDirectory("minecraft-world-");
        extractZip(tempFile, extractDir);

        return readLevelData(findWorldDirectory(extractDir));
    }

    private ImportedWorld readLevelData(Path worldDir) {
        File levelDataFile = worldDir.resolve("level.dat").toFile();

        try {
            CompoundTag root = (CompoundTag) NBTUtil.read(levelDataFile).getTag();
            CompoundTag data = root.getCompoundTag("Data");

            String seed = "";
            if (data.containsKey("WorldGenSettings")) {
                CompoundTag worldGen = data.getCompoundTag("WorldGenSettings");
                seed = worldGen.getString("seed");
            } else if (data.containsKey("RandomSeed")) {
                seed = data.getString("RandomSeed");
            }

            CompoundTag version = data.getCompoundTag("Version");
            String minecraftVersion = version.getString("Name");

            CompoundTag difficultySettings = data.getCompoundTag("difficulty_settings");
            String difficultyName = difficultySettings.getString("difficulty");

            return new ImportedWorld(
                    worldDir,
                    data.getString("LevelName"),
                    seed,
                    GameMode.fromId(data.getInt("GameType")),
                    Difficulty.fromName(difficultyName),
                    minecraftVersion
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read Minecraft level.dat", e);
        }
    }

    private void extractZip(Path zipPath, Path destination) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipPath.toFile())) {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {

                ZipEntry entry = entries.nextElement();

                Path output = destination.resolve(entry.getName()).normalize();

                if (!output.startsWith(destination)) {
                    throw new IOException("Invalid zip entry: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(output);
                    continue;
                }

                Files.createDirectories(output.getParent());

                try (InputStream in = zipFile.getInputStream(entry)) {
                    Files.copy(in, output, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private Path findWorldDirectory(Path extractDir) throws IOException {
        try (Stream<Path> paths = Files.walk(extractDir)) {

            return paths
                    .filter(path -> path.getFileName().toString().equals("level.dat"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Minecraft world."))
                    .getParent();
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        if (!file.getOriginalFilename().endsWith(".zip")) {
            throw new BadRequestException("Only zip files are supported");
        }
    }

}
