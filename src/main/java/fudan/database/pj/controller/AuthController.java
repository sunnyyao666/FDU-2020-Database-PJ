package fudan.database.pj.controller;

import fudan.database.pj.controller.request.UserRequest;
import fudan.database.pj.domain.User;
import fudan.database.pj.security.jwt.JwtTokenUtil;
import fudan.database.pj.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author YHT
 */
@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(AuthService authService, JwtTokenUtil jwtTokenUtil) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> register() {
        return ResponseEntity.ok("1");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.header("token", jwtTokenUtil.generateToken(user));
        return builder.body(user);
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@RequestBody UserRequest request) {
        return ResponseEntity.ok(authService.updateInfo(request.getPassword(), request.getInformation()));
    }

}
