package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String externalCode;
    private String name;
    private String taxCode;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany (mappedBy = "customer")
    private List<SupplyPoint> supplyPoints;
    @OneToMany (mappedBy = "customer")
    private List<Invoice> invoices;
}
