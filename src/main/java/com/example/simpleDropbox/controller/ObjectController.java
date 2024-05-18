package com.example.simpleDropbox.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.simpleDropbox.model.MObject;
import com.example.simpleDropbox.service.MObjectStorageService;

@Controller
@RequestMapping("/files")
public class ObjectController {
    
    MObjectStorageService mObjectStorageService;

    public ObjectController(MObjectStorageService mObjectRepository) {
        this.mObjectStorageService = mObjectRepository;
    }

    @GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", mObjectStorageService.getAllSavedFiles().stream()
            .map(a -> a.getName())
            .collect(Collectors.toList()));
		return "uploadForm.html";
	}


    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("metadata") String metadata,
			RedirectAttributes redirectAttributes) {
		var mObject = MObject.builder().metadata(metadata).build();
		mObjectStorageService.store(file, mObject);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/files/";
	}

	@GetMapping("/find-all")
	public ResponseEntity<List<MObject>> findAll() {
		return ResponseEntity.ok(mObjectStorageService.getAllSavedFiles());
	}
	

}
