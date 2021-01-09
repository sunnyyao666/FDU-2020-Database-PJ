package fudan.database.pj.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * @author YHT
 */
@Entity
public class State {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Patient patient;

    private Long patientID;

    private float temperature;
    private String symptom;
    private int condition;
    private Date createTime;

    public State() {
    }

    public State(Patient patient, float temperature, String symptom, Date createTime) {
        this.patient = patient;
        this.patientID = patient.getId();
        this.temperature = temperature;
        this.symptom = symptom;
        this.condition = patient.getCondition();
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        this.patientID = patient.getId();
        this.condition = patient.getCondition();
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
