package com.ay_smart_tech.facility_tracker_api.facility;

import com.ay_smart_tech.facility_tracker_api.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "facility")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;


    @Enumerated(EnumType.STRING)
    @Column(name = "facility_type", nullable = false, length = 20)
    private FacilityType facilityType;


    @Column(name = "principal", nullable = false, precision = 15, scale = 2)
    private BigDecimal principal;


    @Column(name = "profit_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal profitRate;


    @ColumnDefault("PENDING")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FacilitySatus status;

    @Column(name = "start_date")
    private LocalDate startDate;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Legal transitions map: current status -> set of statuses it may move to
    private static final Map<FacilitySatus, Set<FacilitySatus>> ALLOWED_TRANSITIONS = Map.of(
            FacilitySatus.PENDING, Set.of(FacilitySatus.APPROVED, FacilitySatus.REJECTED),
            FacilitySatus.APPROVED, Set.of(FacilitySatus.ACTIVE),
            FacilitySatus.ACTIVE, Set.of(FacilitySatus.CLOSED),
            FacilitySatus.CLOSED, Set.of(),
            FacilitySatus.REJECTED, Set.of()
    );

    public void transitionTo(FacilitySatus newStatus){
        Set<FacilitySatus> allowed = ALLOWED_TRANSITIONS.get(this.status);
        if(allowed == null || allowed.contains(newStatus)) {
            throw new IllegalStateException(
                    "Cannot Transition facility from " + this.status + " to " + newStatus
            );
        }
        this.status = newStatus;
        if(newStatus == FacilitySatus.ACTIVE && this.startDate == null){
            this.startDate = LocalDate.now();
        }

    }

    @PrePersist
    protected  void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if(this.status == null)
            this.status = FacilitySatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
