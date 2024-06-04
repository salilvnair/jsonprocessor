package com.github.salilvnair.jsonprocessor.graphql.builder;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;


public final class GQLBuilder {
    private static final String QUERY_TEMPLATE = "{\"query\":\"%s %s\"}";
    private static final String SEGMENT_TEMPLATE = "%s %s";
    private static final String QUERY = "query";
    private static final String MUTATION = "mutation";
    private final String type;
    private final String name;

    private GQLVariables variables;
    private GQLFields fields;

    private GQLBuilder(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static GQLBuilder query(String name) {
        return new GQLBuilder(QUERY, name);
    }

    public static GQLBuilder mutation(String name) {
        return new GQLBuilder(MUTATION, name);
    }

    public GQLBuilder fields(GQLFields fields) {
        this.fields = fields;
        return this;
    }

    public GQLBuilder fields(GQLField... fields) {
        this.fields = Optional.ofNullable(this.fields).orElseGet(GQLFields::new);
        this.fields.add(fields);
        return this;
    }

    public GQLBuilder variables(GQLVariable... variables) {
        this.variables = Optional.ofNullable(this.variables).orElseGet(GQLVariables::root);
        Arrays.stream(variables)
                .filter(Objects::nonNull)
                .forEach(this.variables::add);
        return this;
    }

    public String build() {
        return String.format(QUERY_TEMPLATE, type, buildSegment("\\\"", "\\\""));
    }

    public String buildSegment() {
        return String.format(SEGMENT_TEMPLATE, type, buildSegment("\"", "\""));
    }

    public static String prettify(String query) {
        StringBuilder prettyQuery = new StringBuilder();
        Stack<String> indentStack = new Stack<>();
        String indent = "  ";
        boolean[] inString = {false};

        query.chars().forEach(c -> {
            char character = (char) c;

            if (character == '\"') {
                inString[0] = !inString[0];
                prettyQuery.append(character);
                return;
            }

            if (inString[0]) {
                prettyQuery.append(character);
                return;
            }

            switch (character) {
                case '{':
                case '[':
                    prettyQuery.append(character).append('\n');
                    indentStack.push(indent);
                    prettyQuery.append(String.join("", indentStack));
                    break;
                case '}':
                case ']':
                    prettyQuery.append('\n');
                    if (!indentStack.isEmpty()) indentStack.pop();
                    prettyQuery.append(String.join("", indentStack));
                    prettyQuery.append(character);
                    break;
                case ',':
                    prettyQuery.append(character).append('\n');
                    prettyQuery.append(String.join("", indentStack));
                    break;
                case ' ':
                case '\n':
                    break;
                case ':':
                    prettyQuery.append(": ");
                    break;
                default:
                    prettyQuery.append(character);
                    break;
            }
        });

        return prettyQuery.toString().trim();
    }

    private String buildSegment(String variablePrefix, String variableSuffix) {
        StringBuilder builder = new StringBuilder("{").append(name);
        builder.append(buildVariables(variablePrefix, variableSuffix));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.build()));
        builder.append("}");
        return builder.toString();
    }

    private String buildVariables(String variablePrefix, String variableSuffix) {
        return Optional.ofNullable(variables)
                .map(GQLVariables::build)
                .map(vs -> GQLVariable.replacePlaceholder(vs, variablePrefix, variableSuffix))
                .orElse("");
    }
}
