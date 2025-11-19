package com.productmanagement.domain;

import org.springframework.data.neo4j.core.schema.*;
import lombok.*;

@Node("DataproviderAttribute")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"name", "value"})
public class DataProviderAttribute {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String value;

    public static DataProviderAttribute of(String name, String value) {
        return DataProviderAttribute.builder()
                .name(name)
                .value(value)
                .build();
    }
}

