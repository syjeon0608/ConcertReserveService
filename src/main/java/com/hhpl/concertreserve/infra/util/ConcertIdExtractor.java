package com.hhpl.concertreserve.infra.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConcertIdExtractor {

    private static final Pattern pattern = Pattern.compile("/concerts/(\\d+)");

    public static Long extractConcertId(String uri) {
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }

        return null;
    }
}
