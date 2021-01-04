package fudan.database.pj.repository;

import fudan.database.pj.domain.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {
    Optional<Patient> findById(Long id);

    Set<Patient> findAllByArea(int area);

    Set<Patient> findAllByCondition(int condition);

    Set<Patient> findAllByAreaAndCondition(int area, int condition);
}
