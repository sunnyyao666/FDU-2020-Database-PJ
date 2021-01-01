package fudan.database.pj.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author YHT
 */
@Entity
public class Authority implements GrantedAuthority {
    private static final long serialVersionUID = -8974777274465208640L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * doctor：主治医生
     * matron：护士长
     * wardNurse：病房护士
     * emergencyNurse：急诊护士
     */
    private String authority;

    @ManyToOne
    @JsonIgnore
    private User user;

    /**
     * 1：轻症
     * 2：重症
     * 3：危重症
     */
    private int area;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "wardNurse")
    private Set<Sickbed> sickbeds = new HashSet<>();

    public Authority() {
    }

    public Authority(String authority, User user, int area) {
        this.authority = authority;
        this.user = user;
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
