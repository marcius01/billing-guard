package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointStatus;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class SupplyPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    @Enumerated(EnumType.STRING)
    private ESupplyPointType type;
    private String region;
    private String city;
    @Enumerated(EnumType.STRING)
    private ESupplyPointStatus status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "supplyPoint")
    private List<Invoice> invoices;

}
