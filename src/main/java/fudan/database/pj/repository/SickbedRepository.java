package fudan.database.pj.repository;

import fudan.database.pj.domain.Authority;
import fudan.database.pj.domain.Sickbed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SickbedRepository extends CrudRepository<Sickbed, Long> {
    Set<Sickbed> findAllByWardNurseName(String wardNurseName);
}
