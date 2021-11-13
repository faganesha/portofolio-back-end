package com.example.Library.Projection;

import com.fasterxml.jackson.annotation.JsonGetter;

public interface TopFineProjection {
    @JsonGetter("frequency")
    Integer getFrequency();

    @JsonGetter("member")
    String getMember();

    @JsonGetter("fine")
    Integer getFine();
}
