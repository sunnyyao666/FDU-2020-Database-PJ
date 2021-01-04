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
    private final AuthorityRepository authorityRepository;
    private final WardRepository wardRepository;
    private final SickbedRepository sickbedRepository;
    private final PatientRepository patientRepository;
    private final StateRepository stateRepository;

    @Autowired
    public EmergencyNurseService(UserRepository userRepository, AuthorityRepository authorityRepository, WardRepository wardRepository, SickbedRepository sickbedRepository, PatientRepository patientRepository, StateRepository stateRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.wardRepository = wardRepository;
        this.sickbedRepository = sickbedRepository;
        this.patientRepository = patientRepository;
        this.stateRepository = stateRepository;
    }

    public Patient addPatient(String name, String information, int condition) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"emergencyNurse".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Patient patient = new Patient(name, information, condition);
        patientRepository.save(patient);
        patient = transferArea(patient);
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

    public Patient transferArea(Patient patient) {
        int from = patient.getArea();
        int to = patient.getCondition();
        if (from == to) return patient;

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

        if (patientRepository.findById(patient.getId()).isPresent())
            patient = patientRepository.findById(patient.getId()).get();
        else throw new BadCredentialsException("No such Patient!");
        return patient;
    }
}
