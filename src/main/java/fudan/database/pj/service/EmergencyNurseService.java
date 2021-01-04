package fudan.database.pj.service;

import fudan.database.pj.domain.*;
import fudan.database.pj.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class EmergencyNurseService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;

    @Autowired
    public EmergencyNurseService(UserRepository userRepository, PatientRepository patientRepository, PatientService patientService) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.patientService = patientService;
    }

    public Patient addPatient(String name, String information, int condition) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"emergencyNurse".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Patient patient = new Patient(name, information, condition);
        patientRepository.save(patient);
        patient = patientService.transferArea(patient);
        return patient;
    }

    public Set<Patient> getPatients(int filter) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"emergencyNurse".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Set<Patient> result = new HashSet<Patient>();

        if (filter >= 1 && filter <= 5) result = patientRepository.findAllByArea(filter);
        else if (filter >= 6 && filter <= 9) result = patientRepository.findAllByCondition(filter - 6);

        if (result == null || result.size() == 0) return null;
        else return result;
    }
}
