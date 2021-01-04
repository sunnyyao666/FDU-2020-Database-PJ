package fudan.database.pj.controller;

import fudan.database.pj.controller.request.FilterRequest;
import fudan.database.pj.service.MatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatronController {
    private final MatronService matronService;

    @Autowired
    public MatronController(MatronService matronService) {
        this.matronService = matronService;
    }

    @PostMapping("/matronGetPatients")
    public ResponseEntity<?> getPatients(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(matronService.getPatients(filterRequest.getFilter()));
    }

    @PostMapping("/matronGetWards")
    public ResponseEntity<?> getWards() {
        return ResponseEntity.ok(matronService.getWards());
    }

    @PostMapping("/matronUpdateNurse")
    public ResponseEntity<?> updateNurse(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(matronService.updateNurse(filterRequest.getUsername(), filterRequest.getFilter()));
    }

    @PostMapping("/matronGetNurses")
    public ResponseEntity<?> getNurses(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(matronService.getNurses(filterRequest.getFilter()));
    }
}
