package fudan.database.pj.controller;

import fudan.database.pj.controller.request.FilterRequest;
import fudan.database.pj.controller.request.StateRequest;
import fudan.database.pj.service.WardNurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WardNurseController {
    private final WardNurseService wardNurseService;

    @Autowired
    public WardNurseController(WardNurseService wardNurseService) {
        this.wardNurseService = wardNurseService;
    }

    @PostMapping("/wardNurseGetPatients")
    public ResponseEntity<?> getPatients(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(wardNurseService.getPatients(filterRequest.getFilter()));
    }

    @PostMapping("/wardNurseUpdateInfo")
    public ResponseEntity<?> updateInfo(@RequestBody StateRequest stateRequest) {
        return ResponseEntity.ok(wardNurseService.updateState(stateRequest.getId(), stateRequest.getTemperature(), stateRequest.getSymptom(), stateRequest.getCreateTime()));
    }
}
