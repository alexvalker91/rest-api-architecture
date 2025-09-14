package alex.valker91.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(schema = "public", name = "subscription_table")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userentity;

    @Column(name = "subscription_start_date")
    private LocalDate startDate;

    public SubscriptionEntity() {
    }

    public SubscriptionEntity(Long id, UserEntity userentity, LocalDate startDate) {
        this.id = id;
        this.userentity = userentity;
        this.startDate = startDate;
    }
}
