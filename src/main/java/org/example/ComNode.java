package org.example;


import java.util.List;

public class ComNode {

    String id;

    String pk;

    String score;

    String weight;

    String hCount;

    String mCount;

    List<String> result;

    String sort;

    String eComm;

    String dComm;

    public String getId() {
        return id;
    }

    public List<String> getResult() {
        return result;
    }

    public String gethCount() {
        return hCount;
    }

    public String getmCount() {
        return mCount;
    }

    public String getPk() {
        return pk;
    }

    public String getScore() {
        return score;
    }

    public String getWeight() {
        return weight;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void sethCount(String hCount) {
        this.hCount = hCount;
    }

    public void setmCount(String mCount) {
        this.mCount = mCount;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setdComm(String dComm) {
        this.dComm = dComm;
    }

    public void seteComm(String eComm) {
        this.eComm = eComm;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getdComm() {
        return dComm;
    }

    public String geteComm() {
        return eComm;
    }

    public String getSort() {
        return sort;
    }

}
