package DataSci.judicature.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 案件信息实体类
 */
public class CaseInfoSets {

    private String title;          // 文书标题
    private Set<String> courts;    // 审理法院
    private Set<String> type;      // 案件类型
    private Set<String> accusation;// 案由
    private Set<String> category;  // 文书类型
    private Set<String> date;      // 裁判日期
    private Set<String> caseno;    // 案号

    public CaseInfoSets() {
        courts = new HashSet<>();
        type = new HashSet<>();
        accusation = new HashSet<>();
        category = new HashSet<>();
        date = new HashSet<>();
        caseno = new HashSet<>();
        title = "";
    }

    public CaseInfoSets(String title, Set<String> courts, Set<String> type, Set<String> accusation, Set<String> category, Set<String> date, Set<String> caseno) {
        this.title = title;
        this.courts = courts;
        this.type = type;
        this.accusation = accusation;
        this.category = category;
        this.date = date;
        this.caseno = caseno;
    }

    public Set<String> getCourts() {
        return courts;
    }

    public void setCourts(Set<String> courts) {
        this.courts = courts;
    }

    public Set<String> getType() {
        return type;
    }

    public void setType(Set<String> type) {
        this.type = type;
    }

    public Set<String> getAccusation() {
        return accusation;
    }

    public void setAccusation(Set<String> accusation) {
        this.accusation = accusation;
    }

    public Set<String> getCategory() {
        return category;
    }

    public void setCategory(Set<String> category) {
        this.category = category;
    }

    public Set<String> getDate() {
        return date;
    }

    public void setDate(Set<String> date) {
        this.date = date;
    }

    public Set<String> getCaseno() {
        return caseno;
    }

    public void setCaseno(Set<String> caseno) {
        this.caseno = caseno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /////////////////////////////////////////////////////////////////////////////

    public void setCaseno(String caseno) {
        this.caseno.add(caseno);
    }

    public void setDate(String date) {
        this.date.clear();
        this.date.add(date);
    }

    public void setCategory(String category) {
        this.category.add(category);
    }

    public void setAccusation(String accusation) {
        this.accusation.add(accusation);
    }

    public void setType(String type) {
        this.type.add(type);
    }

    public void setCourts(String courts) {
        this.courts.add(courts);
    }

    @Override
    public String toString() {
        return "CaseInfoSets{" +
                "title='" + title + '\'' +
                ", courts=" + courts +
                ", type=" + type +
                ", accusation=" + accusation +
                ", category=" + category +
                ", date=" + date +
                ", caseno=" + caseno +
                '}';
    }
}
