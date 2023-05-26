package com.food.diary.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse {
    protected boolean successful;
    protected boolean hasMore;
    protected boolean hasPrev = false;
    protected int prev;
    protected int next;
    protected int size;
    protected String data;

    public ListResponse(boolean successful, boolean hasMore) {
        this.successful = successful;
        this.hasMore = hasMore;
    }
}
