package fudan.database.pj;

import fudan.database.pj.domain.*;
import fudan.database.pj.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author YHT
 */
@SpringBootApplication
@EnableJpaAuditing
public class PJApplication {
    public static void main(String[] args) {
        SpringApplication.run(PJApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, AuthorityRepository authorityRepository, WardRepository wardRepository, SickbedRepository sickbedRepository, PasswordEncoder passwordEncoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                init(userRepository, authorityRepository, wardRepository, sickbedRepository, passwordEncoder);
            }
        };
    }

    private void init(UserRepository userRepository, AuthorityRepository authorityRepository, WardRepository wardRepository, SickbedRepository sickbedRepository, PasswordEncoder passwordEncoder) {
        String password = "123456";
        for (int i = 1; i <= 3; i++) {
            User doctor = new User("Doctor" + i, passwordEncoder.encode(password), "Doctor" + i);
            userRepository.save(doctor);
            Authority docAuthority = new Authority("doctor", doctor, i);
            authorityRepository.save(docAuthority);

            User matron = new User("Matron" + i, passwordEncoder.encode(password), "Matron" + i);
            userRepository.save(matron);
            Authority matAuthority = new Authority("matron", matron, i);
            authorityRepository.save(matAuthority);

            for (int j = 1; j <= 2; j++) {
                User wardNurse = new User("WardNurse" + i + j, passwordEncoder.encode(password), "WardNurse" + i + j);
                userRepository.save(wardNurse);
                Authority wardAuthority = new Authority("wardNurse", wardNurse, i);
                authorityRepository.save(wardAuthority);
            }
        }

        User emergencyNurse = new User("EmergencyNurse", passwordEncoder.encode(password), "EmergencyNurse");
        userRepository.save(emergencyNurse);
        Authority emeAuthority = new Authority("emergencyNurse", emergencyNurse, 0);
        authorityRepository.save(emeAuthority);

        for (int i = 1; i <= 2; i++)
            for (int j = 1; j <= 3; j++) {
                Ward ward = new Ward(j, i);
                wardRepository.save(ward);
                int m = 0;
                if (j == 1) m = 4;
                else if (j == 2) m = 2;
                else m = 1;
                for (int k = 1; k <= m; k++) {
                    Sickbed sickbed = new Sickbed(ward, k, null, null);
                    sickbedRepository.save(sickbed);
                }
            }
    }
}
