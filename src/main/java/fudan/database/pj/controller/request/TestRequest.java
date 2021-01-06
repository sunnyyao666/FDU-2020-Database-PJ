package fudan.database.pj.controller.request;

import java.util.Date;

public class TestRequest {
    private Long patientID;
    private int result;
    private Date createTime;

    public TestRequest() {
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
