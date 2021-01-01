package fudan.database.pj.repository;

import fudan.database.pj.domain.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Set<Authority> findAllByAuthority(String authority);
}
