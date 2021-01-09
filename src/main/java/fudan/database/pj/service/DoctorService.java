package fudan.database.pj.service;

import fudan.database.pj.domain.*;
import fudan.database.pj.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class DoctorService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final SickbedRepository sickbedRepository;
    private final PatientRepository patientRepository;
    private final TestRepository testRepository;
    private final PatientService patientService;

    @Autowired
    public DoctorService(UserRepository userRepository, AuthorityRepository authorityRepository, SickbedRepository sickbedRepository, PatientRepository patientRepository, TestRepository testRepository, PatientService patientService) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.sickbedRepository = sickbedRepository;
        this.patientRepository = patientRepository;
        this.testRepository = testRepository;
        this.patientService = patientService;
    }

    public Set<Patient> getPatients(int filter) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"doctor".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

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

    public Set<User> getNurses() throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"doctor".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Set<User> result = new HashSet<User>();
        Authority matron = (Authority) authorityRepository.findAllByAuthorityAndArea("matron", authority.getArea()).toArray()[0];
        result.add(matron.getUser());
        Set<Authority> wardNurses = authorityRepository.findAllByAuthorityAndArea("wardNurse", authority.getArea());
        for (Authority wardNurse : wardNurses) result.add(wardNurse.getUser());
        return result;
    }

    public Patient updateCondition(Long patientID, int condition) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"doctor".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Patient patient = null;
        if (!patientRepository.findById(patientID).isPresent()) throw new BadCredentialsException("No such patient.");
        else patient = patientRepository.findById(patientID).get();
        if ((condition == patient.getCondition() && condition != 0) || (condition == 0 && patient.getCondition() != 0))
            return patient;
        patient.setCondition(condition);
        patientRepository.save(patient);
        if (condition == 4 || condition == 0) {
            Sickbed sickbed = patient.getSickbed();

            Authority wardNurse = sickbed.getWardNurse();
            Set<Sickbed> sickbeds=wardNurse.getSickbeds();
            sickbeds.remove(sickbed);
            wardNurse.setSickbeds(sickbeds);
            authorityRepository.save(wardNurse);

            sickbed.setPatient(null);
            sickbed.setWardNurse(null);
            sickbedRepository.save(sickbed);

            patient.setArea(condition);
            patient.setSickbed(null);
            patientRepository.save(patient);

            patientService.newFreeNurse(authority.getArea(), 1);
            return patient;
        } else {
            int n = Math.min(patientService.getEmptyBedsNum(condition), patientService.getFreeNurseNum(condition));
            if (n == 0) return patient;
            Sickbed sickbed = patient.getSickbed();

            Authority wardNurse = sickbed.getWardNurse();
            Set<Sickbed> sickbeds=wardNurse.getSickbeds();
            sickbeds.remove(sickbed);
            wardNurse.setSickbeds(sickbeds);
            authorityRepository.save(wardNurse);

            sickbed.setPatient(null);
            sickbed.setWardNurse(null);
            sickbedRepository.save(sickbed);

            patient.setArea(5);
            patient.setSickbed(null);
            patientRepository.save(patient);

            patientService.transferArea(patientID);
            patientService.newFreeNurse(authority.getArea(), 1);
            return patientRepository.findById(patientID).get();
        }
    }

    public Patient createTest(Long patientID, int result, Date createTime) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        Authority authority = (Authority) user.getAuthorities().toArray()[0];
        if (!"doctor".equals(authority.getAuthority())) throw new BadCredentialsException("Not authorized.");

        Patient patient = null;
        if (!patientRepository.findById(patientID).isPresent()) throw new BadCredentialsException("No such patient.");
        else patient = patientRepository.findById(patientID).get();

        Test test = new Test(patient, result, createTime);
        testRepository.save(test);
        Set<Test> tests = patient.getTests();
        tests.add(test);
        patient.setTests(tests);
        patientRepository.save(patient);
        patientService.testDischarge(patientID);
        return patientRepository.findById(patientID).get();
    }
}
