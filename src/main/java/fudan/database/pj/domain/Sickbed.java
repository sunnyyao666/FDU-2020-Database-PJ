package fudan.database.pj.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author YHT
 */
@Entity
public class Sickbed {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Ward ward;

    private int number;

    @ManyToOne
    @JsonIgnore
    private Authority wardNurse;

    private String wardNurseName;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "sickbed")
    private Patient patient;

    private Long patientID;
    private String patientName;

    public Sickbed() {
    }

    public Sickbed(Ward ward, int number, Authority wardNurse, Patient patient) {
        this.ward = ward;
        this.number = number;
        this.wardNurse = wardNurse;
        if (wardNurse != null) this.wardNurseName = wardNurse.getUser().getUsername();
        else this.wardNurseName = null;
        if (patient != null) {
            this.patientName = patient.getName();
            this.patientID = patient.getId();
        } else {
            this.patientName = null;
            this.patientID = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Authority getWardNurse() {
        return wardNurse;
    }

    public void setWardNurse(Authority wardNurse) {
        this.wardNurse = wardNurse;
        if (wardNurse != null) this.wardNurseName = wardNurse.getUser().getUsername();
        else this.wardNurseName = "";
    }

    public String getWardNurseName() {
        return wardNurseName;
    }

    public void setWardNurseName(String wardNurseName) {
        this.wardNurseName = wardNurseName;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (patient != null) {
            this.patientName = patient.getName();
            this.patientID = patient.getId();
        } else {
            this.patientName = null;
            this.patientID = null;
        }
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
