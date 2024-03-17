package himedia.project.highfourm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import himedia.project.highfourm.dto.BomRequestDTO;
import himedia.project.highfourm.dto.ProcessDTO;
import himedia.project.highfourm.dto.ProductDTO;
import himedia.project.highfourm.dto.bom.BomRequiredMaterialDTO;
import himedia.project.highfourm.service.BomService;
import himedia.project.highfourm.service.ProcessService;
import himedia.project.highfourm.service.ProductService;
import himedia.project.highfourm.service.RequiredMaterialService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BomController {
	
	private final ProductService productService;
	private final ProcessService processService;
	private final RequiredMaterialService requiredMaterialService;
	private final BomService bomService;
	
	@GetMapping("/api/bom")
	public ResponseEntity<List<ProductDTO>> bom() {
	    List<ProductDTO> resultList = productService.findAllProduct();
	    return ResponseEntity.ok(resultList);
	}
	
	@GetMapping("/api/bom/search")
	public ResponseEntity<List<ProductDTO>> searchBomList(@RequestParam(value = "searchType") String searchType, @RequestParam(value = "search") String search) {
		List<ProductDTO> resultList = productService.search(searchType, search);
		return ResponseEntity.ok(resultList);
	}

	@GetMapping("/api/bom/detail/{productId}")
	public ResponseEntity<Map<String, Object>> bomDetail(@PathVariable("productId") String productId) {
	    Map<String, Object> responseMap = new HashMap<>();
	    
	    List<ProductDTO> productList = productService.findById(productId);
	    // id로 찾는 데이터는 무조건 1개지만 frontend에서 데이터 가공이 쉽도록 List처리
	    responseMap.put("product", productList);

	    List<ProcessDTO> processList = processService.findByProductProductId(productId);
	    responseMap.put("process", processList);
	    
	    List<BomRequiredMaterialDTO> bomRequiredMaterialList = requiredMaterialService.findBomRequiredMaterial(productId);
	    responseMap.put("bomRequiredMaterial", bomRequiredMaterialList);
	    
	    return ResponseEntity.ok(responseMap);
	}
	
	@PostMapping("/api/bom/new")
	public String addBom(@RequestBody BomRequestDTO bomRequestDTO) {
		bomService.saveBom(bomRequestDTO);
		return "redirect:http://localhost:3000/bom";
	}

}
