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
}
