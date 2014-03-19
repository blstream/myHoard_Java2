package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.CollectionDTO;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class SortResult {

    @JsonProperty("total_count")
    private int totalCount;
    private List<CollectionDTO> collections;

    public SortResult() {}

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<CollectionDTO> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionDTO> collections) {
        this.collections = collections;
    }
}
