package fudan.database.pj.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author YHT
 */
@Entity
public class Patient {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String information;

    /**
     * 0：康复出院
     * 1：轻症
     * 2：重症
     * 3：危重症
     * 4：病亡
     * 5：隔离区
     */
    private int area;
    private int condition;

    private boolean transferred;

    @OneToOne
    @JsonIgnore
    private Sickbed sickbed;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "patient")
    private Set<State> states = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "patient")
    private Set<Test> tests = new HashSet<>();

    public Patient() {
    }

    public Patient(String name, String information, int condition) {
        this.name = name;
        this.information = information;
        this.area = 5;
        this.condition = condition;
        this.transferred = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public boolean isTransferred() {
        return transferred;
    }

    public void setTransferred(boolean transferred) {
        this.transferred = transferred;
    }

    public Sickbed getSickbed() {
        return sickbed;
    }

    public void setSickbed(Sickbed sickbed) {
        this.sickbed = sickbed;
    }

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public Set<Test> getTests() {
        return tests;
    }

    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }
}
