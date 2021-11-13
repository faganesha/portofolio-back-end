package com.example.Library.Projection;

import com.fasterxml.jackson.annotation.JsonGetter;

public interface TopMemberProjection {
    @JsonGetter("member")
    String getMember();

    @JsonGetter("total")
    Integer getTotal();

    @JsonGetter("month")
    String getMonth();
}
