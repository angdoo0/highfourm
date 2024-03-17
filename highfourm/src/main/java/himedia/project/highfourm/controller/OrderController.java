package himedia.project.highfourm.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import himedia.project.highfourm.dto.orders.OrdersAndDetailsDTO;
import himedia.project.highfourm.entity.File;
import himedia.project.highfourm.service.OrderService;
import himedia.project.highfourm.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	private final ProductService productService;
	
	@GetMapping("/api/orders")
	public Map<String, Object> orderList() {
		return orderService.findAllOrders();
	}
	
	@GetMapping("/api/orders/new")
	public List<String> ordersNew(){
		List<String> productNameList = productService.findAllProductName();
		return productNameList;
	}
	
	@PostMapping("/api/orders/new")
	public void saveOrders(@RequestBody OrdersAndDetailsDTO ordersAndDetailsDTO) {
		orderService.saveOrder(ordersAndDetailsDTO);
	}
	
	@GetMapping("/api/orders/search")
	public Map<String, Object> orderSearch(@RequestParam(value = "searchType") String searchType,
			@RequestParam(value = "search") String search) {
		Map<String, Object> response = orderService.orderSearch(searchType, search);
		
		return response;
	}

}
