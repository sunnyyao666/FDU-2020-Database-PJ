package fudan.database.pj.controller;

import fudan.database.pj.controller.request.FilterRequest;
import fudan.database.pj.controller.request.PatientRequest;
import fudan.database.pj.service.EmergencyNurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmergencyNurseController {
    private final EmergencyNurseService emergencyNurseService;

    @Autowired
    public EmergencyNurseController(EmergencyNurseService emergencyNurseService) {
        this.emergencyNurseService = emergencyNurseService;
    }

    @PostMapping("/emergencyNurseGetPatients")
    public ResponseEntity<?> getPatients(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(emergencyNurseService.getPatients(filterRequest.getFilter()));
    }

    @PostMapping("/emergencyNurseAddPatient")
    public ResponseEntity<?> addPatient(@RequestBody PatientRequest patientRequest) {
        return ResponseEntity.ok(emergencyNurseService.addPatient(patientRequest.getName(), patientRequest.getInformation(), patientRequest.getCondition(), patientRequest.getResult(), patientRequest.getCreateTime()));
    }
}
