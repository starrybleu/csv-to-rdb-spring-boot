package io.github.starrybleu;


import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Entity
@Table(name = "label")
public class Label {

    @Id
    @Column(name = "no")
    Long no;

    @Column(name = "group_no")
    Long groupNo;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Transient
    long createdTimestamp;

    @Transient
    long updatedTimestamp;

    void convertTimeStampToLocalDateTime() {
        LocalDateTime convertedCreatedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTimestamp), ZoneId.systemDefault());
        this.createdAt = convertedCreatedAt;

        LocalDateTime convertedUpdatedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(updatedTimestamp), ZoneId.systemDefault());
        this.updatedAt = convertedUpdatedAt;
    }

}
