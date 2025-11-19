package com.productmanagement.domain;

import org.springframework.data.neo4j.core.schema.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Node("InternalProduct")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "internalId")
public class InternalProduct {

    @Id
    private String internalId;

    @Property("globalTradeId")
    private String globalTradeIdentifier;

    @Relationship(type = "HAS_ATTRIBUTE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<InternalProductAttribute> attributes = new HashSet<>();
    
    public void addAttribute(InternalProductAttribute attribute) {
        this.attributes.add(attribute);
    }
    
    public void removeAttribute(InternalProductAttribute attribute) {
        this.attributes.remove(attribute);
    }
}

