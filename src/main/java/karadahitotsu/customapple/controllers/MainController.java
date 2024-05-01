package karadahitotsu.customapple.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class MainController {
    @Autowired
    private Environment environment;
    @GetMapping("/")
    public String mainPage(){

        return "main";
    }

    @GetMapping("/catalog")
    public String catalogPage(){
        return "catalog";
    }
    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }
    @GetMapping("/btob")
    public String btobPage(){
        return "btob";
    }
    @GetMapping("/cart")
    public String cartPage(){
        return "cart";
    }
    @GetMapping("/galery")
    public String galeryPage(){
        return "galery";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/make")
    public String makePage(){
        return "make";
    }
    @GetMapping("/profile")
    public String profilePage(){
        return "profile";
    }
    @GetMapping("/registration")
    public String registrationPage(){
        return "registration";
    }
    @GetMapping("/images/{folder}/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String folder, @PathVariable String filename) throws IOException {
        String filePath = environment.getProperty("image.storage.path") + folder + "/" + filename;

        File file = new File(filePath);

        if (file.exists()) {
            byte[] imageBytes = new byte[(int) file.length()];
            try (InputStream inputStream = new FileInputStream(file)) {
                inputStream.read(imageBytes);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
