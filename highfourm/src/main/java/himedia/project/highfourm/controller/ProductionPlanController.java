package himedia.project.highfourm.controller;

import java.util.List;

//import org.hibernate.internal.build.AllowSysOut;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import himedia.project.highfourm.dto.performance.PerformanceDTO;
import himedia.project.highfourm.dto.plan.MonthlyProductionPlanDTO;
import himedia.project.highfourm.dto.plan.ProductionPlanFormDTO;
import himedia.project.highfourm.service.ProductionPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductionPlanController {
	private final ProductionPlanService productionPlanService;
	
	@GetMapping("/api/production-plan")
	public List<ProductionPlanFormDTO> productionPlan(){
		return productionPlanService.findAllProductionPlans();
	}
	
	@PostMapping("/api/production-plan")
	public void saveProductionPlan(@RequestBody ProductionPlanFormDTO productionPlanForm) {
		productionPlanService.updateProductionPlan(productionPlanForm);
	}
	
	@GetMapping("/api/production-plan/{productionPlanId}")
	public List<MonthlyProductionPlanDTO> monthlyProductionPlan(@PathVariable("productionPlanId") String productionPlanId) {
		List<MonthlyProductionPlanDTO> monthlyProductionPlans = productionPlanService.findMonthlyProductionPlan(productionPlanId);
		return monthlyProductionPlans;
	}
	
	@GetMapping("/api/production-plan/search")
	public List<ProductionPlanFormDTO> productionPlanSearch(@RequestParam(value = "searchType") String searchType,
			@RequestParam(value = "search") String search) {
		List<ProductionPlanFormDTO> response = productionPlanService.productionPlanSearch(searchType, search);
		
		return response;
	}
	

}
