package fudan.database.pj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author YHT
 */
@RestController
public class AuthController {
    @Autowired
    public AuthController() {
    }

    @GetMapping("/hello")
    public ResponseEntity<?> register() {
        return ResponseEntity.ok("1");
    }
}
