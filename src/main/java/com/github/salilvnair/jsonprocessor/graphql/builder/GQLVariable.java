package com.github.salilvnair.jsonprocessor.graphql.builder;

import java.util.HashSet;
import java.util.Set;

public final class GQLVariable {
    private static final String PREFIX_PLACEHOLDER = "==GQLVariable==PREFIX==";
    private static final String SUFFIX_PLACEHOLDER = "==GQLVariable==SUFFIX==";
    private static final Set<Class<?>> JAVA_BOX_TYPES = initBoxTypes();

    private static Set<Class<?>> initBoxTypes() {
        Set<Class<?>> boxTypes = new HashSet<>();
        boxTypes.add(Integer.class);
        boxTypes.add(Long.class);
        boxTypes.add(Double.class);
        boxTypes.add(Float.class);
        return boxTypes;
    }

    private static final String TEMPLATE = "%s:%s";
    private final String name;
    private final Object value;

    private GQLVariable(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static GQLVariable of(String name, Object value) {
        return new GQLVariable(name, value);
    }

    public String build() {
        return String.format(TEMPLATE, name, buildValue());
    }

    private String buildValue() {
        if (value instanceof GQLVariable) {
            throw new IllegalArgumentException("GQLVariable cannot be used as the value of GQLVariable");
        }
        if (value instanceof GQLVariables) {
            return ((GQLVariables) value).build();
        }
        boolean javaBoxTypes = JAVA_BOX_TYPES.stream().anyMatch(type -> value.getClass().isAssignableFrom(type));
        if (javaBoxTypes) {
            return value.toString();
        }
        return PREFIX_PLACEHOLDER + value.toString() + SUFFIX_PLACEHOLDER;
    }

    protected static String replacePlaceholder(String variables, String prefix, String suffix) {
        variables = variables.replace(PREFIX_PLACEHOLDER, prefix);
        variables = variables.replace(SUFFIX_PLACEHOLDER, suffix);
        return variables;
    }
}
