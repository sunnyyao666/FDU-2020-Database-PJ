package fudan.database.pj.service;

import fudan.database.pj.domain.*;
import fudan.database.pj.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PatientService {
    private final AuthorityRepository authorityRepository;
    private final WardRepository wardRepository;
    private final SickbedRepository sickbedRepository;
    private final PatientRepository patientRepository;
    private final StateRepository stateRepository;
    private final TestRepository testRepository;

    @Autowired
    public PatientService(AuthorityRepository authorityRepository, WardRepository wardRepository, SickbedRepository sickbedRepository, PatientRepository patientRepository, StateRepository stateRepository, TestRepository testRepository) {
        this.authorityRepository = authorityRepository;
        this.wardRepository = wardRepository;
        this.sickbedRepository = sickbedRepository;
        this.patientRepository = patientRepository;
        this.stateRepository = stateRepository;
        this.testRepository = testRepository;
    }

    public void newFreeNurse(int area, int number) {
        number = Math.min(number, getEmptyBedsNum(area));
        if (number == 0) return;
        for (int i = 5; i >= 1; i--) {
            if (i == area || i == 4) continue;
            Set<Patient> patients = patientRepository.findAllByAreaAndCondition(i, area);
            if (patients == null || patients.size() == 0) continue;
            int n = 0;
            for (Patient patient : patients) {
                // 先移到隔离区
                patient.setArea(5);
                patientRepository.save(patient);
                Sickbed sickbed = patient.getSickbed();
                if (sickbed != null) {
                    sickbed.setWardNurse(null);
                    sickbed.setPatient(null);
                    sickbedRepository.save(sickbed);
                    patient.setSickbed(null);
                    patientRepository.save(patient);
                }
                Patient newPatient = transferArea(patientRepository.findById(patient.getId()).get());
                if (newPatient.getArea() == newPatient.getCondition()) {
                    n++;
                    number--;
                    if (number == 0) break;
                }
            }
            if (n > 0 && i != 5) newFreeNurse(i, n);
            if (number == 0) break;
        }
    }

    public Patient transferArea(Patient patient) {
        int to = patient.getCondition();
        if (patient.getArea() == to) return patient;

        Set<Ward> wards = wardRepository.findAllByArea(to);
        Sickbed sickbed = null;
        for (Ward ward : wards) {
            Set<Sickbed> sickbeds = ward.getSickbeds();
            for (Sickbed s : sickbeds) if (s.getPatient() == null) sickbed = s;
        }
        if (sickbed == null) return patient;

        Set<Authority> wardNurses = authorityRepository.findAllByAuthorityAndArea("wardNurse", to);
        Authority wardNurse = null;
        int m = 4 - to;
        for (Authority a : wardNurses)
            if (a.getSickbeds().size() < m) wardNurse = a;
        if (wardNurse == null) return patient;

        patient.setArea(to);
        patient.setTransferred(true);
        patient.setSickbed(sickbed);
        patientRepository.save(patient);

        sickbed.setWardNurse(wardNurse);
        sickbedRepository.save(sickbed);

        return patientRepository.findById(patient.getId()).get();
    }

    public int getEmptyBedsNum(int area) {
        Set<Ward> wards = wardRepository.findAllByArea(area);
        int n = 0;
        for (Ward ward : wards) {
            Set<Sickbed> sickbeds = ward.getSickbeds();
            for (Sickbed s : sickbeds) if (s.getPatient() == null) n++;
        }
        return n;
    }

    public int getFreeNurseNum(int area) {
        Set<Authority> wardNurses = authorityRepository.findAllByAuthorityAndArea("wardNurse", area);
        int n = 0;
        int m = 4 - area;
        for (Authority a : wardNurses)
            if (a.getSickbeds().size() < m) n++;
        return n;
    }

    public boolean testDischarge(Patient patient) {
        if (patient.getStates().size() < 3 || patient.getTests().size() < 2) return false;
        if (patient.getCondition() != 1) return false;
        Set<State> states = stateRepository.findAllByPatientIDOrderByCreateTimeDesc(patient.getId());
        Set<Test> tests = testRepository.findAllByPatientIDOrderByCreateTimeDesc(patient.getId());
        int i = 0;
        for (State state : states) {
            if (state.getTemperature() >= 37.3) return false;
            i++;
            if (i >= 3) break;
        }
        i = 0;
        long startTime = 0;
        for (Test test : tests) {
            if (test.getResult() == 1) return false;
            if (i == 0) {
                startTime = test.getCreateTime().getTime();
                i++;
            } else {
                long endTime = test.getCreateTime().getTime();
                if (endTime - startTime >= 24 * 60 * 60 * 1000) {
                    i++;
                    startTime = endTime;
                } else continue;
            }
            if (i >= 2) break;
        }
        if (i < 2) return false;
        patient.setCondition(0);
        patientRepository.save(patient);
        return true;
    }
}
