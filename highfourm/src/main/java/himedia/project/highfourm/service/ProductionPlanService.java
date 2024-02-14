package himedia.project.highfourm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.ProductionStatusDTO;
import himedia.project.highfourm.dto.WorkPerformanceResponseDTO;
import himedia.project.highfourm.dto.performance.PerformanceDTO;
import himedia.project.highfourm.dto.plan.MonthlyProductionPlanDTO;
import himedia.project.highfourm.dto.plan.ProductionPlanFormDTO;
import himedia.project.highfourm.entity.MonthlyProductionPlan;
import himedia.project.highfourm.entity.ProductionPlan;
import himedia.project.highfourm.repository.MonthlyProductionPlanRepository;
import himedia.project.highfourm.repository.ProductionPlanRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionPlanService {
	
	private final ProductionPlanRepository productionPlanRepository;
	private final MonthlyProductionPlanRepository monthlyProductionPlanRepository;
	
	public List<ProductionPlanFormDTO> findAllProductionPlans() {
		List<ProductionPlanFormDTO> productionPlans = productionPlanRepository.findAllProductionPlan(Sort.by(Sort.Direction.DESC, "o.orderId"));
		return productionPlans;
	}
	
	public List<MonthlyProductionPlanDTO> findMonthlyProductionPlan(String productionPlanId){
		List<MonthlyProductionPlan> monthlyProductionPlans = monthlyProductionPlanRepository.findByProductionPlanId(productionPlanId);
		List<MonthlyProductionPlanDTO> monthlyProductionPlanDTOs = monthlyProductionPlans.stream()
				.map(MonthlyProductionPlanDTO::fromEntity)
				.collect(Collectors.toList());
		
		return monthlyProductionPlanDTOs;
	}
	
	public List<WorkPerformanceResponseDTO> findProductionPlanDetails() {
		List<WorkPerformanceResponseDTO> responseList = productionPlanRepository.findProductionPlanDetails();
		return responseList;
	}
	
	public List<ProductionPlanFormDTO> findAll(){
		List<ProductionPlanFormDTO> resultList = productionPlanRepository.findAll()
				.stream().map(productionPlan -> productionPlan.toDTO())
				.collect(Collectors.toList());
		return resultList;
	}
	
	public List<ProductionStatusDTO> findStatus() {
		List<ProductionStatusDTO> statusList = productionPlanRepository.findStatus();
		return statusList;
	}
	
	@Transactional
	public void updateProductionPlan(ProductionPlanFormDTO productionPlanFormDTO) {
	    ProductionPlan productionPlan = productionPlanRepository.findById(productionPlanFormDTO.getProductionPlanId())
	            .orElseThrow(() -> new EntityNotFoundException("ProductionPlan not found"));

	    List<MonthlyProductionPlan> monthlyPlans = productionPlanFormDTO.getMonthlyProductionPlans().stream()
	            .map(dto -> {
	                MonthlyProductionPlan monthlyPlan = dto.toEntity(); 
	                monthlyPlan.assignProductionPlan(productionPlan);
	                return monthlyPlan;
	            })
	            .collect(Collectors.toList());
	    
		productionPlan.updateProductionPlan(productionPlanFormDTO.getProductionPlanAmount()
											, productionPlanFormDTO.getProductionStartDate()
											, monthlyPlans);
	}
	
	public List<ProductionPlanFormDTO> productionPlanSearch(String searchType, String search){
		List<ProductionPlanFormDTO> response = new ArrayList<>();

		if (searchType.equals("주문 번호")) {
			response = productionPlanRepository.findProductionPlanByOrderId(Sort.by(Sort.Direction.DESC,"plan.productionPlanId"),"%" + search + "%");
		} else if (searchType.equals("거래처명")) {
			response = productionPlanRepository.findProductionPlanByVendor(Sort.by(Sort.Direction.DESC,"plan.productionPlanId"),"%" + search + "%");
		} else if (searchType.equals("품명")) {
			response = productionPlanRepository.findProductionPlanByProductName(Sort.by(Sort.Direction.DESC,"plan.productionPlanId"),"%" + search + "%");
		}
		return response;
	}
}
