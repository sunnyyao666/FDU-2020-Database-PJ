package fudan.database.pj.controller.request;

/**
 * @author YHT
 */
public class PatientRequest {
    private String name;
    private String information;
    private int condition;

    public PatientRequest(){
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

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
