package com.github.salilvnair.jsonprocessor.graphql.builder;

import java.util.*;
import java.util.stream.Collectors;

public class GQLVariables {
    private final boolean root;
    private List<GQLVariable> variables;

    protected GQLVariables() {
        this(false);
    }

    protected GQLVariables(boolean root) {
        this.root = root;
    }

    public static GQLVariables of(GQLVariable... variables) {
        return new GQLVariables(false).add(variables);
    }

    public static GQLVariables root() {
        return new GQLVariables(true);
    }

    public GQLVariables add(GQLVariable... variables) {
        this.variables = Optional.ofNullable(this.variables).orElseGet(ArrayList::new);
        this.variables.addAll(Arrays.stream(variables)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        return this;
    }

    public String build() {
        StringJoiner joiner = new StringJoiner(",", getPrefix(), getSuffix());
        variables.stream().map(GQLVariable::build).forEach(joiner::add);
        return joiner.toString();
    }


    private String getPrefix() {
        return this.root ? "(" : "{";
    }

    private String getSuffix() {
        return this.root ? ")" : "}";
    }
}
