package fudan.database.pj.service;

import fudan.database.pj.domain.Authority;
import fudan.database.pj.domain.Patient;
import fudan.database.pj.domain.User;
import fudan.database.pj.domain.Ward;
import fudan.database.pj.exception.ResponsibleToPatientsException;
import fudan.database.pj.repository.AuthorityRepository;
import fudan.database.pj.repository.PatientRepository;
import fudan.database.pj.repository.UserRepository;
import fudan.database.pj.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MatronService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;

    @Autowired
    public MatronService(UserRepository userRepository, AuthorityRepository authorityRepository, WardRepository wardRepository, PatientRepository patientRepository, PatientService patientService) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.wardRepository = wardRepository;
        this.patientRepository = patientRepository;
        this.patientService = patientService;
    }

    public Set<Patient> getPatients(int filter) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"matron".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Set<Patient> result = new HashSet<Patient>();
        Set<Patient> patients = patientRepository.findAllByArea(authority.getArea());

        for (Patient patient : patients) {
            switch (filter) {
                case 1:
                    // 是否可出院
                    if (patient.getCondition() == 0) result.add(patient);
                    break;
                case 2:
                    // 是否待转其他区域
                    if (patient.getArea() != patient.getCondition()) result.add(patient);
                    break;
                default:
                    result.add(patient);
            }
        }
        if (result.size() != 0) return result;
        else return null;
    }

    public Set<User> getNurses(int filter) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"matron".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        int area = authority.getArea();
        if (filter == 0) area = 0;
        Set<Authority> wardNurses = authorityRepository.findAllByAuthorityAndArea("wardNurse", area);
        Set<User> result = new HashSet<User>();
        for (Authority wardNurse : wardNurses) result.add(wardNurse.getUser());
        if (result.size() == 0) return null;
        else return result;
    }

    public boolean updateNurse(String username, int filter) throws BadCredentialsException, ResponsibleToPatientsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"matron".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        User u = userRepository.findByUsername(username);
        if (u == null) throw new BadCredentialsException("No such user!");
        Authority wardNurse = (Authority) u.getAuthorities().toArray()[0];
        if ("wardNurse".equals(wardNurse.getAuthority())) throw new BadCredentialsException("No such wardNurse!");

        if (filter == 0) {
            if (wardNurse.getSickbeds().size() > 0) throw new ResponsibleToPatientsException(username);
            wardNurse.setArea(0);
            authorityRepository.save(wardNurse);
            return true;
        } else {
            wardNurse.setArea(authority.getArea());
            patientService.newFreeNurse(authority.getArea(), 4 - authority.getArea());
            return true;
        }
    }

    public Set<Ward> getWards() throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"matron".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        return wardRepository.findAllByArea(authority.getArea());
    }
}
