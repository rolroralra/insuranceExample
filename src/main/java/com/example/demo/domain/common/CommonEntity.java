package com.example.demo.domain.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class CommonEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;                                        // 고유번호

    @CreationTimestamp
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdAt;                        // 등록 일자

    @UpdateTimestamp
    @Column(length = 20)
    private LocalDateTime updatedAt;                        // 수정 일자

    public Boolean isEqualId(CommonEntity commonEntity) {
        return Objects.equals(this.id, commonEntity.getId());
    }

}
