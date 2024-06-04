package com.github.salilvnair.jsonprocessor.graphql.builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public final class GQLField {
    private final String name;
    private GQLVariables variables;
    private GQLFields fields;

    private GQLField(String name) {
        this.name = name;
    }

    public static GQLField of(String name) {
        return new GQLField(name);
    }

    public GQLField variables(GQLVariable... variables) {
        this.variables = Optional.ofNullable(this.variables).orElseGet(GQLVariables::of);
        this.variables.add(variables);
        return this;
    }

    public GQLField fields(String... names) {
        return fields(Arrays.stream(names).map(GQLField::of).collect(Collectors.toList()));
    }

    public GQLField fields(GQLField... fields) {
        return fields(Arrays.asList(fields));
    }

    public GQLField fields(Collection<GQLField> fields) {
        this.fields = Optional.ofNullable(this.fields).orElseGet(GQLFields::new);
        this.fields.add(fields);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(name);
        Optional.ofNullable(variables).ifPresent(vs -> builder.append(vs.build()));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.build()));
        return builder.toString();
    }

}
