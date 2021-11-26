package DataSci.judicature.domain;

import java.util.ArrayList;

/**
 * 词汇类型实体类
 */
public class Words {
    private ArrayList<String> noun;     //名词
    private ArrayList<String> verb;     //动词
    private ArrayList<String> adj;      //形容词

    public Words() {
    }

    public Words(ArrayList<String> noun, ArrayList<String> verb, ArrayList<String> adj) {
        this.noun = noun;
        this.verb = verb;
        this.adj = adj;
    }

    public ArrayList<String> getNoun() {
        return noun;
    }

    public void setNoun(ArrayList<String> noun) {
        this.noun = noun;
    }

    public ArrayList<String> getVerb() {
        return verb;
    }

    public void setVerb(ArrayList<String> verb) {
        this.verb = verb;
    }

    public ArrayList<String> getAdj() {
        return adj;
    }

    public void setAdj(ArrayList<String> adj) {
        this.adj = adj;
    }
}
