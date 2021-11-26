package DataSci.judicature.domain;

/**
 * 案件信息实体类
 */
public class CaseMsg {
    private String Criminals;   //当事人
    private String Gender;      //性别
    private String Ethnicity;   //民族
    private String Birthplace;  //出生地
    private String Accusation;  //案由
    private String Courts;      //相关法院

    public CaseMsg() {
    }

    public CaseMsg(String criminals, String gender, String ethnicity, String birthplace, String accusation, String courts) {
        Criminals = criminals;
        Gender = gender;
        Ethnicity = ethnicity;
        Birthplace = birthplace;
        Accusation = accusation;
        Courts = courts;
    }

    public String getCriminals() {
        return Criminals;
    }

    public void setCriminals(String criminals) {
        Criminals = criminals;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getEthnicity() {
        return Ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        Ethnicity = ethnicity;
    }

    public String getBirthplace() {
        return Birthplace;
    }

    public void setBirthplace(String birthplace) {
        Birthplace = birthplace;
    }

    public String getAccusation() {
        return Accusation;
    }

    public void setAccusation(String accusation) {
        Accusation = accusation;
    }

    public String getCourts() {
        return Courts;
    }

    public void setCourts(String courts) {
        Courts = courts;
    }

    @Override
    public String toString() {
        return "CaseMsg{" +
                "Criminals='" + Criminals + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Ethnicity='" + Ethnicity + '\'' +
                ", Birthplace='" + Birthplace + '\'' +
                ", Accusation='" + Accusation + '\'' +
                ", Courts='" + Courts + '\'' +
                '}';
    }
}
