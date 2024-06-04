package com.github.salilvnair.jsonprocessor.graphql.builder;

import java.util.*;
import java.util.stream.Collectors;

public final class GQLFields {
    private List<GQLField> fields;

    protected GQLFields() {
    }

    public static GQLFields of(GQLField... fields) {
        return new GQLFields().add(fields);
    }

    public static GQLFields of(String... names) {
        return new GQLFields().add(names);
    }

    public GQLFields add(GQLField... fields) {
        return add(Arrays.stream(fields)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    public GQLFields add(String... names) {
        return add(Arrays.stream(names)
                .filter(Objects::nonNull)
                .map(GQLField::of)
                .collect(Collectors.toList()));
    }

    public GQLFields add(Collection<GQLField> fields) {
        this.fields = Optional.ofNullable(this.fields).orElseGet(ArrayList::new);
        this.fields.addAll(fields);
        return this;
    }


    public String build() {
        StringJoiner joiner = new StringJoiner(" ", "{", "}");
        fields.stream().map(GQLField::build).forEach(joiner::add);
        return joiner.toString();
    }
}
