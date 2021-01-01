package fudan.database.pj.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author YHT
 */
@Entity
public class Ward {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 1：轻症
     * 2：重症
     * 3：危重症
     */
    private int area;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "ward")
    private Set<Sickbed> sickbeds = new HashSet<>();

    public Ward() {
    }

    public Ward(int area) {
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public Set<Sickbed> getSickbeds() {
        return sickbeds;
    }

    public void setSickbeds(Set<Sickbed> sickbeds) {
        this.sickbeds = sickbeds;
    }
}
