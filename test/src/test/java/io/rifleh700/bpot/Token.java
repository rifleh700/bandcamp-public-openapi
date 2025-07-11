package io.rifleh700.bpot;

import io.rifleh700.bpot.api.model.TralbumType;

import java.time.Instant;

public class Token {

    private final String token;

    public Token(Instant purchased,
                 Long tralbumId,
                 TralbumType tralbumType,
                 Integer index) {

        this.token = String.format("%s:%s:%s:%s:",
                purchased != null ? purchased.getEpochSecond() : "",
                tralbumId != null ? tralbumId : "",
                tralbumType != null ? tralbumType : "",
                index != null ? index : "");
    }

    @Override
    public String toString() {

        return token;
    }
}
