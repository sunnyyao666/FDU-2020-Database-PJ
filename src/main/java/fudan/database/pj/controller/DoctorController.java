package fudan.database.pj.controller;

import fudan.database.pj.controller.request.FilterRequest;
import fudan.database.pj.controller.request.PatientRequest;
import fudan.database.pj.controller.request.TestRequest;
import fudan.database.pj.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/doctorGetPatients")
    public ResponseEntity<?> getPatients(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(doctorService.getPatients(filterRequest.getFilter()));
    }

    @PostMapping("/doctorGetNurses")
    public ResponseEntity<?> getNurses() {
        return ResponseEntity.ok(doctorService.getNurses());
    }

    @PostMapping("/doctorUpdateCondition")
    public ResponseEntity<?> updateCondition(@RequestBody PatientRequest patientRequest) {
        return ResponseEntity.ok(doctorService.updateCondition(patientRequest.getPatientID(), patientRequest.getCondition()));
    }

    @PostMapping("/doctorCreateTest")
    public ResponseEntity<?> createTest(@RequestBody TestRequest testRequest) {
        return ResponseEntity.ok(doctorService.createTest(testRequest.getPatientID(), testRequest.getResult(), testRequest.getCreateTime()));
    }
}
