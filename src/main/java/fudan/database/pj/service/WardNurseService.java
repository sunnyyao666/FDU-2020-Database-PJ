package fudan.database.pj.service;

import fudan.database.pj.domain.*;
import fudan.database.pj.repository.PatientRepository;
import fudan.database.pj.repository.StateRepository;
import fudan.database.pj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class WardNurseService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final StateRepository stateRepository;
    private final PatientService patientService;

    @Autowired
    public WardNurseService(UserRepository userRepository, PatientRepository patientRepository, StateRepository stateRepository, PatientService patientService) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.stateRepository = stateRepository;
        this.patientService = patientService;
    }

    public Set<Patient> getPatients(int filter) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"wardNurse".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Set<Patient> result = new HashSet<Patient>();
        Set<Sickbed> sickbeds = authority.getSickbeds();
        if (sickbeds == null) return null;
        for (Sickbed sickbed : sickbeds) {
            Patient patient;
            if (sickbed.getPatient() != null) patient = sickbed.getPatient();
            else continue;

            switch (filter) {
                case 1:
                    // 是否可以出院
                    if (patient.getCondition() == 0) result.add(patient);
                    break;
                case 2:
                    // 是否病亡
                    if (patient.getCondition() == 4) result.add(patient);
                    break;
                default:
                    result.add(patient);
            }

        }
        if (result.size() != 0) return result;
        else return null;
    }

    public Patient updateState(Long patientID, float temperature, String symptom, Date createTime) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"wardNurse".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Set<Sickbed> sickbeds = authority.getSickbeds();
        if (sickbeds == null) return null;
        Patient patient = null;
        for (Sickbed sickbed : sickbeds)
            if (patientID.equals(sickbed.getPatientID())) patient = sickbed.getPatient();
        if (patient == null) throw new BadCredentialsException("No such Patient!");

        State state = new State(patient, temperature, symptom, createTime);
        stateRepository.save(state);

        if (patientRepository.findById(patientID).isPresent()) patient = patientRepository.findById(patientID).get();
        else throw new BadCredentialsException("No such Patient!");
        patientService.testDischarge(patient);
        return patientRepository.findById(patientID).get();
    }
}
