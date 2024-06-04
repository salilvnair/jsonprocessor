package com.github.salilvnair.jsonprocessor.graphql.test;

import com.github.salilvnair.jsonprocessor.graphql.builder.GQLBuilder;
import com.github.salilvnair.jsonprocessor.graphql.builder.GQLField;
import com.github.salilvnair.jsonprocessor.graphql.builder.GQLVariable;

public class GQLBuilderTest {
    public static void main(String[] args) {
        System.out.println(
                GQLBuilder
                        .query("studentsBySearchType")
                        .variables(
                                GQLVariable.of("stateId", "111200"),
                                GQLVariable.of("searchType", "standard"),
                                GQLVariable.of("searchValue", "high12")
                        )
                        .fields(
                                GQLField.of("total"),
                                GQLField.of("address")
                                        .fields("zipCode")
                        )
                .build());
    }
}
