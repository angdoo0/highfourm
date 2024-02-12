package himedia.project.highfourm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.PerformanceDTO;
import himedia.project.highfourm.dto.WorkPerformanceDTO;
import himedia.project.highfourm.dto.orders.OrdersDTO;
import himedia.project.highfourm.entity.Orders;
import himedia.project.highfourm.repository.ProductionPlanRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceService {
	private final ProductionPlanRepository productionPlanRepository;
	
	public List<PerformanceDTO> findAllPerformance(){
		List<PerformanceDTO> performances = productionPlanRepository.findAllPerformances(Sort.by(Sort.Direction.DESC,"plan.productionPlanId"));
		performances.stream()
			.forEach(e -> e.setTotalProductionAmount(productionPlanRepository.sumProductionAmount(e.getProductionPlanId())));
		return performances;
	}
	
	public PerformanceDTO findPerformance(String productionPlanId){
		System.out.println(productionPlanId);
		PerformanceDTO performance = productionPlanRepository.findPerformances(productionPlanId);
		System.out.println(performance);
		List<WorkPerformanceDTO> productionList = productionPlanRepository.findProductionList(productionPlanId);
		
//		performance.setTotalProductionAmount(productionList.stream()
//				.mapToLong(e -> e.getAcceptanceAmount())
//				.sum());
		performance.setTotalProductionAmount(productionPlanRepository.sumProductionAmount(productionPlanId));
		performance.setWorkPerformances(productionList);
		
		return performance;
	}
	
	public List<PerformanceDTO> performanceSearch(String searchType, String search){
		List<PerformanceDTO> response = new ArrayList<>();

		if (searchType.equals("주문 번호")) {
			response = productionPlanRepository.findPerformanceByOrderId(Sort.by(Sort.Direction.DESC,"plan.productionPlanId"),"%" + search + "%");
		} else if (searchType.equals("거래처명")) {
			response = productionPlanRepository.findPerformanceByVendor(Sort.by(Sort.Direction.DESC,"plan.productionPlanId"),"%" + search + "%");
		} else if (searchType.equals("품명")) {
			response = productionPlanRepository.findPerformanceByProductName(Sort.by(Sort.Direction.DESC,"plan.productionPlanId"),"%" + search + "%");
		}

		return response;
	}
}
