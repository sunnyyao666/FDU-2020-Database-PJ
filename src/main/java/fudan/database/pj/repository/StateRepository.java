package fudan.database.pj.repository;

import fudan.database.pj.domain.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StateRepository extends CrudRepository<State, Long> {
    Set<State> findAllByPatientIDOrderByCreateTimeDesc(Long patientID);
}
