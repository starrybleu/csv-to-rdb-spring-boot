package io.github.starrybleu;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "panel_numbers_chunk")
@EntityListeners(AuditingEntityListener.class)
public class PanelNumbersChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    Long no;

    @Column(name = "label_no")
    Long labelNo;

    @Column(name = "panels")
    String panels; // note JSON Array 임. 마이그레이션할 때만 쓰이므로 그냥 String 으로 처리

    @CreatedDate
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    void adjustPanelsWhenEmpty() {
        panels = StringUtils.isEmpty(panels) ? "[]" : panels;
    }

}
