package himedia.project.highfourm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import himedia.project.highfourm.dto.WorkPerformanceDTO;
import himedia.project.highfourm.dto.WorkPerformanceListDTO;
import himedia.project.highfourm.dto.WorkPerformanceResponseDTO;
import himedia.project.highfourm.service.ProductionPlanService;
import himedia.project.highfourm.service.WorkPerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WorkPerformanceController {

	private final WorkPerformanceService workPerformanceService;
	private final ProductionPlanService productionPlanService;
	
	@GetMapping("/api/work-performance")
	public ResponseEntity<List<WorkPerformanceListDTO>> workPerformance() {
		List<WorkPerformanceListDTO> WorkPerformanceList = workPerformanceService.findList();
		return ResponseEntity.ok(WorkPerformanceList);
	}
	
	@GetMapping("/api/work-performance/search")
	public ResponseEntity<List<WorkPerformanceListDTO>> searchUserList(@RequestParam(value = "searchType") String searchType, @RequestParam(value = "search") String search) {
		List<WorkPerformanceListDTO> result = null;
		
		if(searchType.equals("생산 계획 코드")) {
			result = workPerformanceService.findByProductionPlanId(search);
		} else if(searchType.equals("담당자")) {
			result = workPerformanceService.findByManager(search);
		} else if(searchType.equals("생산품명")) {
			result = workPerformanceService.findByProductName(search);
		}
		if (result == null) {
			result = new ArrayList<WorkPerformanceListDTO>();
		}
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/api/work-performance/new")
	public ResponseEntity<List<WorkPerformanceResponseDTO>> workPerformanceNew() {
		List<WorkPerformanceResponseDTO> reponseList = productionPlanService.findProductionPlanDetails();
		return ResponseEntity.ok(reponseList);
	}
	
	@PostMapping("/api/work-performance/new")
	public ResponseEntity<String> saveWorkPerformanceAndUpdateMaterialStock(@RequestBody WorkPerformanceDTO[] workPerformanceDTOArray) {
		for (WorkPerformanceDTO workPerformanceDTO : workPerformanceDTOArray) {
			workPerformanceService.saveWorkPerformanceAndUpdateMaterialStock(workPerformanceDTO);
		}
		log.info(workPerformanceDTOArray.toString());
		return ResponseEntity.ok("Success");
	}
}
