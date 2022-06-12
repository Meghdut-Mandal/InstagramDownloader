package com.apps.inslibrary.entity.stroiesEntity;

import java.io.Serializable;
import java.util.List;

public class ImageVersions2 implements Serializable {
    private List<Candidates> candidates;

    public List<Candidates> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(List<Candidates> list) {
        this.candidates = list;
    }
}
