package DataSci.judicature.domain;

import java.util.List;

public class CaseMarks {
    private List<String> Criminals;   //当事人
    private List<String> Gender;      //性别
    private List<String> Ethnicity;   //民族
    private List<String> Birthplace;  //出生地
    private List<String> Accusation;  //案由
    private List<String> Courts;      //相关法院

    public CaseMarks() {
    }

    public CaseMarks(List<String> criminals, List<String> gender, List<String> ethnicity, List<String> birthplace, List<String> accusation, List<String> courts) {
        Criminals = criminals;
        Gender = gender;
        Ethnicity = ethnicity;
        Birthplace = birthplace;
        Accusation = accusation;
        Courts = courts;
    }

    public List<String> getCriminals() {
        return Criminals;
    }

    public void setCriminals(List<String> criminals) {
        Criminals = criminals;
    }

    public List<String> getGender() {
        return Gender;
    }

    public void setGender(List<String> gender) {
        Gender = gender;
    }

    public List<String> getEthnicity() {
        return Ethnicity;
    }

    public void setEthnicity(List<String> ethnicity) {
        Ethnicity = ethnicity;
    }

    public List<String> getBirthplace() {
        return Birthplace;
    }

    public void setBirthplace(List<String> birthplace) {
        Birthplace = birthplace;
    }

    public List<String> getAccusation() {
        return Accusation;
    }

    public void setAccusation(List<String> accusation) {
        Accusation = accusation;
    }

    public List<String> getCourts() {
        return Courts;
    }

    public void setCourts(List<String> courts) {
        Courts = courts;
    }

    @Override
    public String toString() {
        return "CaseMarks{" +
                "Criminals=" + Criminals +
                ", Gender=" + Gender +
                ", Ethnicity=" + Ethnicity +
                ", Birthplace=" + Birthplace +
                ", Accusation=" + Accusation +
                ", Courts=" + Courts +
                '}';
    }
}
