package com.productmanagement.domain;

import org.springframework.data.neo4j.core.schema.*;
import lombok.*;

@Node("InternalProductAttribute")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"name", "value"})
public class InternalProductAttribute {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String value;

    public static InternalProductAttribute of(String name, String value) {
        return InternalProductAttribute.builder()
                .name(name)
                .value(value)
                .build();
    }
}