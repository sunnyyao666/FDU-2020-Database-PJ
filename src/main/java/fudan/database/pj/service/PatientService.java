package fudan.database.pj.service;

import fudan.database.pj.domain.Authority;
import fudan.database.pj.domain.Patient;
import fudan.database.pj.domain.Sickbed;
import fudan.database.pj.domain.Ward;
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

    @Autowired
    public PatientService(AuthorityRepository authorityRepository, WardRepository wardRepository, SickbedRepository sickbedRepository, PatientRepository patientRepository) {
        this.authorityRepository = authorityRepository;
        this.wardRepository = wardRepository;
        this.sickbedRepository = sickbedRepository;
        this.patientRepository = patientRepository;
    }

    public void newFreeNurse(int area, int number) {
        number = Math.min(number, getEmptyBedsNum(area));
        if (number == 0) return;
        for (int i = 1; i <= 5; i++) {
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
        patientRepository.save(patient);

        sickbed.setWardNurse(wardNurse);
        sickbed.setPatient(patient);
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
}
