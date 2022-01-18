package DataSci.judicature.domain;

/**
 * 案件信息实体类
 */
public class CaseMsg {
    private String criminals_text;   //当事人
    private String gender_text;      //性别
    private String ethnicity_text;   //民族
    private String birthplace_text;  //出生地
    private String accusation_text;  //案由
    private String courts_text;      //相关法院
    private String summary_text;     //其他

    public CaseMsg() {
    }

    public CaseMsg(String criminals_text, String gender_text, String ethnicity_text, String birthplace_text, String accusation_text, String courts_text, String summary_text) {
        this.criminals_text = criminals_text;
        this.gender_text = gender_text;
        this.ethnicity_text = ethnicity_text;
        this.birthplace_text = birthplace_text;
        this.accusation_text = accusation_text;
        this.courts_text = courts_text;
        this.summary_text = summary_text;
    }

    public String getCriminals_text() {
        return criminals_text;
    }

    public void setCriminals_text(String criminals_text) {
        this.criminals_text = criminals_text;
    }

    public String getGender_text() {
        return gender_text;
    }

    public void setGender_text(String gender_text) {
        this.gender_text = gender_text;
    }

    public String getEthnicity_text() {
        return ethnicity_text;
    }

    public void setEthnicity_text(String ethnicity_text) {
        this.ethnicity_text = ethnicity_text;
    }

    public String getBirthplace_text() {
        return birthplace_text;
    }

    public void setBirthplace_text(String birthplace_text) {
        this.birthplace_text = birthplace_text;
    }

    public String getAccusation_text() {
        return accusation_text;
    }

    public void setAccusation_text(String accusation_text) {
        this.accusation_text = accusation_text;
    }

    public String getCourts_text() {
        return courts_text;
    }

    public void setCourts_text(String courts_text) {
        this.courts_text = courts_text;
    }

    public String getSummary_text() {
        return summary_text;
    }

    public void setSummary_text(String summary_text) {
        this.summary_text = summary_text;
    }

    @Override
    public String toString() {
        return "CaseMsg{" +
                "criminals_text='" + criminals_text + '\'' +
                ", gender_text='" + gender_text + '\'' +
                ", ethnicity_text='" + ethnicity_text + '\'' +
                ", birthplace_text='" + birthplace_text + '\'' +
                ", accusation_text='" + accusation_text + '\'' +
                ", courts_text='" + courts_text + '\'' +
                ", summary_text='" + summary_text + '\'' +
                '}';
    }
}
