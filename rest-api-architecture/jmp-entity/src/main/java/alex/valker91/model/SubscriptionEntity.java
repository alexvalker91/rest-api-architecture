package alex.valker91.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(schema = "public", name = "subscription_table")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscription_user_id")
    private Long userId;

    @Column(name = "subscription_start_date")
    private LocalDate startDate;

    public SubscriptionEntity() {
    }

    public SubscriptionEntity(Long id, Long userId, LocalDate startDate) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
