package pull_up.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "created_time", updatable = false)
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column(name = "is_deleted", columnDefinition = "TINYINT", length = 1)
    @ColumnDefault("0")
    private boolean isDeleted = false;

    public void softDelete() {
        if (isDeleted) {
            throw new IllegalStateException();
        }
        this.isDeleted = true;
    }

}
