package himedia.project.highfourm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.CacheControl;
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
import himedia.project.highfourm.dto.WorkPerformanceListDTO;
import himedia.project.highfourm.dto.bom.BomRequiredMaterialDTO;
import himedia.project.highfourm.entity.Product;
import himedia.project.highfourm.entity.Process;
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
	public ResponseEntity<List<ProductDTO>> searchUserList(@RequestParam(value = "searchType") String searchType, @RequestParam(value = "search") String search) {
		List<ProductDTO> result = null;
		
		if(searchType.equals("제품 코드")) {
			result = productService.findByProductId(search);
		} else if(searchType.equals("제품명")) {
			result = productService.findByProductName(search);
		}
		if (result == null) {
			result = new ArrayList<ProductDTO>();
		}
		
		return ResponseEntity.ok(result);
	}

	@GetMapping("/api/bom/detail/{productId}")
	public ResponseEntity<Map<String, Object>> bomDetail(@PathVariable("productId") String productId) {
	    Map<String, Object> responseMap = new HashMap<>();
	    
	    // product findByProductProductId
	    Optional<Product> productEntity = productService.findById(productId);
	    if (productEntity.isPresent()) {
	    	// product Entity to DTO
	        ProductDTO productDTO = productEntity.get().toProductDTO();
	        List<ProductDTO> productDTOList =List.of(productDTO);
	        responseMap.put("product", productDTOList);
	    }

	    // process findByProductProductId
	    List<Process> processEntityList = processService.findByProductProductId(productId);

	    // process Entity to DTO
	    List<ProcessDTO> processDTOList = processEntityList.stream()
	            .map(process -> process.toProcessDTO())
	            .collect(Collectors.toList());
	    responseMap.put("process", processDTOList);
	    
	    // bomRequiredMaterialDTO findByProductProductId
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
