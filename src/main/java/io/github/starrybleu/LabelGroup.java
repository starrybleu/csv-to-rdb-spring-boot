package io.github.starrybleu;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Entity
@Table(name = "label_group")
public class LabelGroup {

    @Id
    @Column(name = "no")
    private Long no;

    @Column(name = "name")
    String name;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Transient
    long createdAtTimestamp;

    void convertTimeStampToLocalDateTime() {
        LocalDateTime converted = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAtTimestamp), ZoneId.systemDefault());
        this.createdAt = converted;
        this.updatedAt = converted;
    }

}
