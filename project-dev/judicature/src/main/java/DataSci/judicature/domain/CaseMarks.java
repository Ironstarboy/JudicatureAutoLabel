package DataSci.judicature.domain;

import java.util.Arrays;

public class CaseMarks {
    private String[] Criminals;   //当事人
    private String[] Gender;      //性别
    private String[] Ethnicity;   //民族
    private String[] Birthplace;  //出生地
    private String[] Accusation;  //案由
    private String[] Courts;      //相关法院

    public CaseMarks() {
    }

    public CaseMarks(String[] criminals, String[] gender, String[] ethnicity, String[] birthplace, String[] accusation, String[] courts) {
        Criminals = criminals;
        Gender = gender;
        Ethnicity = ethnicity;
        Birthplace = birthplace;
        Accusation = accusation;
        Courts = courts;
    }

    public String[] getCriminals() {
        return Criminals;
    }

    public void setCriminals(String[] criminals) {
        Criminals = criminals;
    }

    public String[] getGender() {
        return Gender;
    }

    public void setGender(String[] gender) {
        Gender = gender;
    }

    public String[] getEthnicity() {
        return Ethnicity;
    }

    public void setEthnicity(String[] ethnicity) {
        Ethnicity = ethnicity;
    }

    public String[] getBirthplace() {
        return Birthplace;
    }

    public void setBirthplace(String[] birthplace) {
        Birthplace = birthplace;
    }

    public String[] getAccusation() {
        return Accusation;
    }

    public void setAccusation(String[] accusation) {
        Accusation = accusation;
    }

    public String[] getCourts() {
        return Courts;
    }

    public void setCourts(String[] courts) {
        Courts = courts;
    }

    @Override
    public String toString() {
        return "CaseMarks{" +
                "Criminals=" + Arrays.toString(Criminals) +
                ", Gender=" + Arrays.toString(Gender) +
                ", Ethnicity=" + Arrays.toString(Ethnicity) +
                ", Birthplace=" + Arrays.toString(Birthplace) +
                ", Accusation=" + Arrays.toString(Accusation) +
                ", Courts=" + Arrays.toString(Courts) +
                '}';
    }
}
