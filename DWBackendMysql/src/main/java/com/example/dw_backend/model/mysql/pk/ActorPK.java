package com.example.dw_backend.model.mysql.pk;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ActorPK implements Serializable {
    private String productId;
    private String actorName;

    public String getActorName() {
        return actorName;
    }
}
