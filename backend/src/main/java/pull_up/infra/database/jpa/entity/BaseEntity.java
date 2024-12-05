package pull_up.infra.database.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(updatable = false)
    @CreatedDate
    protected LocalDateTime createdTime;

    @Column
    @LastModifiedDate
    protected LocalDateTime updatedTime;

    @Column(columnDefinition = "TINYINT", length = 1)
    @ColumnDefault("0")
    protected boolean isDeleted = false;

    protected void updateTime() {
        updatedTime = LocalDateTime.now();
    }

    public void softDelete() {
        if (isDeleted) {
            throw new IllegalStateException();
        }
        this.isDeleted = true;
    }

}
