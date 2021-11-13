package com.example.Library.Projection;

import com.fasterxml.jackson.annotation.JsonGetter;


public interface TopBookProjection {
    @JsonGetter("frequency")
    Integer getFrequency();

    @JsonGetter("book")
    String getBook();
}
