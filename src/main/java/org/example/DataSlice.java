package org.example;

import java.util.List;

public class DataSlice {
    String dataId;

    List<String> slice;

    List<String> r;

    List<String> commit;

    String dataCommit;

    public List<String> getCommit() {
        return commit;
    }

    public List<String> getR() {
        return r;
    }

    public List<String> getSlice() {
        return slice;
    }

    public String getDataCommit() {
        return dataCommit;
    }

    public String getDataId() {
        return dataId;
    }


    public void setCommit(List<String> commit) {
        this.commit = commit;
    }

    public void setDataCommit(String dataCommit) {
        this.dataCommit = dataCommit;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public void setR(List<String> r) {
        this.r = r;
    }

    public void setSlice(List<String> slice) {
        this.slice = slice;
    }

}
