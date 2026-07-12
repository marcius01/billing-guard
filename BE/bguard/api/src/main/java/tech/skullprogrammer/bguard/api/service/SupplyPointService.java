package tech.skullprogrammer.bguard.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.dto.SupplyPointRequest;
import tech.skullprogrammer.bguard.api.mapper.SupplyPointMapper;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointStatus;
import tech.skullprogrammer.bguard.domain.repository.SupplyPointRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class SupplyPointService {

    private final SupplyPointRepository supplyPointRepository;
    private final CustomerService customerService;
    private final SupplyPointMapper supplyPointMapper;

    @Autowired
    public SupplyPointService(SupplyPointRepository repository,
                              CustomerService customerService,
                              SupplyPointMapper mapper) {
        this.supplyPointRepository = repository;
        this.customerService = customerService;
        this.supplyPointMapper = mapper;
    }

    public SupplyPoint getSupplyPointById(Long id){
        return supplyPointRepository.findById(id).orElse(null);
    }

    public SupplyPoint getSupplyPointByIdAndCustomerId(Long id, Long customerId){
        return supplyPointRepository.findByIdAndCustomerId(id, customerId);
    }

    public SupplyPoint getSupplyPointByCodeAndCustomerCode(String code, String customerCode){
        return supplyPointRepository.findByCodeAndCustomerExternalCode(code, customerCode);
    }
    public Page<SupplyPoint> getAllSupplyPoint(Pageable pageable){
        return supplyPointRepository.findAll(pageable);
    }

    @Transactional
    public SupplyPoint saveSupplyPoint(SupplyPointRequest supplyPointRequest){
        SupplyPoint supplyPoint = supplyPointMapper.toEntity(supplyPointRequest);
        boolean exists = supplyPointRepository.existsByTypeAndCode(supplyPointRequest.getType(), supplyPointRequest.getCode());
        if (exists) throw new SkullException(SkullException.ErrorType.ENTITY_ALREADY_EXISTS);
        supplyPoint.setStatus(supplyPoint.getStatus() == null ? ESupplyPointStatus.ACTIVE : supplyPoint.getStatus());
        supplyPoint.setCreatedAt(supplyPoint.getCreatedAt() == null ? LocalDate.now() : supplyPoint.getCreatedAt());
        Customer customer = customerService.getCustomerById(supplyPointRequest.getCustomerId());
        if(customer != null) throw new SkullException(SkullException.ErrorType.CUSTOMER_NOT_FOUND);
        supplyPointRepository.save(supplyPoint);
        return supplyPoint;
    }
}
