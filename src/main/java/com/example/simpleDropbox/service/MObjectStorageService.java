package com.example.simpleDropbox.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.simpleDropbox.model.MObject;
import com.example.simpleDropbox.properties.StorageProperties;
import com.example.simpleDropbox.repository.MObjectRepository;

@Service
public class MObjectStorageService {
    
    private MObjectRepository mObjectRepository;
    private final Path rootLocation;

    public MObjectStorageService(MObjectRepository mObjectRepository, StorageProperties properties) {
        this.mObjectRepository = mObjectRepository;
        if(properties.getLocation().trim().length() == 0){
            throw new RuntimeException("File upload location can not be Empty."); 
        }
        rootLocation = Paths.get(properties.getLocation());
    }

    public void store(MultipartFile file, MObject mObject) {
        Long savedFileId = null;
        try {
            Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize()
                    .toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            
            mObjectRepository.findFirstByName(file.getOriginalFilename())
                .ifPresent(a -> {
                    a.setLastUpdated(LocalDateTime.now());
                    mObjectRepository.save(a);
                });

            mObject.setLastUpdated(LocalDateTime.now());
            mObject.setTimeSaved(LocalDateTime.now());
            mObject.setName(file.getOriginalFilename());
            mObject.setLocation(destinationFile.getParent().toString());
            MObject mObject2 = mObjectRepository.save(mObject);
            savedFileId = mObject2.getId();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            if (savedFileId != null) {
                mObjectRepository.deleteById(savedFileId);
            }
            throw new RuntimeException("Failed to store file.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error in saving to the database", e);
        }

    }

    public List<MObject> getAllSavedFiles() {
        return mObjectRepository.findAll();
    }


    public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new RuntimeException("Could not initialize storage", e);
		}
	}
}
