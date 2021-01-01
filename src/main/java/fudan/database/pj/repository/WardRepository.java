package fudan.database.pj.repository;

import fudan.database.pj.domain.Ward;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WardRepository extends CrudRepository<Ward, Long> {
    Set<Ward> findAllByArea(int area);
}
