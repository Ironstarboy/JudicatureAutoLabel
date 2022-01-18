package DataSci.judicature.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CaseMarksArr {
    private Set<String> Criminals;   //当事人
    private Set<String> Gender;      //性别
    private Set<String> Ethnicity;   //民族
    private Set<String> Birthplace;  //出生地
    private Set<String> Accusation;  //案由
    private Set<String> Courts;      //相关法院

    public CaseMarksArr() {
        Criminals = new HashSet<>();
        Gender = new HashSet<>();
        Ethnicity = new HashSet<>();
        Birthplace = new HashSet<>();
        Accusation = new HashSet<>();
        Courts = new HashSet<>();
    }

    public CaseMarksArr(Set<String> criminals, Set<String> gender, Set<String> ethnicity, Set<String> birthplace, Set<String> accusation, Set<String> courts) {
        Criminals = criminals;
        Gender = gender;
        Ethnicity = ethnicity;
        Birthplace = birthplace;
        Accusation = accusation;
        Courts = courts;
    }

    public Set<String> getCriminals() {
        return Criminals;
    }

    public void setCriminals(Set<String> criminals) {
        Criminals = criminals;
    }

    public Set<String> getGender() {
        return Gender;
    }

    public void setGender(Set<String> gender) {
        Gender = gender;
    }

    public Set<String> getEthnicity() {
        return Ethnicity;
    }

    public void setEthnicity(Set<String> ethnicity) {
        Ethnicity = ethnicity;
    }

    public Set<String> getBirthplace() {
        return Birthplace;
    }

    public void setBirthplace(Set<String> birthplace) {
        Birthplace = birthplace;
    }

    public Set<String> getAccusation() {
        return Accusation;
    }

    public void setAccusation(Set<String> accusation) {
        Accusation = accusation;
    }

    public Set<String> getCourts() {
        return Courts;
    }

    public void setCourts(Set<String> courts) {
        Courts = courts;
    }

    //////////////////////////////////////////////////////////////////////////

    public void setCriminals(String Criminals) {
        this.Criminals.add(Criminals);
    }

    public void setGender(String Gender) {
        this.Gender.add(Gender);
    }

    public void setBirthplace(String Birthplace) {
        this.Birthplace.add(Birthplace);
    }

    public void setCourts(String courts) {
        this.Courts.add(courts);
    }

    public void setAccusation(String Accusation) {
        this.Accusation.add(Accusation);
    }

    public void setEthnicity(String Ethnicity) {
        this.Ethnicity.add(Ethnicity);
    }


    @Override
    public String toString() {
        return "CaseMarksArr{" +
                "Criminals=" + Criminals +
                ", Gender=" + Gender +
                ", Ethnicity=" + Ethnicity +
                ", Birthplace=" + Birthplace +
                ", Accusation=" + Accusation +
                ", Courts=" + Courts +
                '}';
    }
}
