package com.example.simpleDropbox.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;

import com.example.simpleDropbox.model.MObject;
import com.example.simpleDropbox.service.MObjectStorageService;

@RestController
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


    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestBody MObject mObject,
			RedirectAttributes redirectAttributes) {

		mObjectStorageService.store(file, mObject);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}

	@GetMapping("/find-all")
	public List<MObject> findAll() {
		return mObjectStorageService.getAllSavedFiles();
	}
	

}
