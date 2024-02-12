package himedia.project.highfourm.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.ProductionPlanFormDTO;
import himedia.project.highfourm.dto.orders.OrderFormDTO;
import himedia.project.highfourm.dto.orders.OrdersAndDetailsDTO;
import himedia.project.highfourm.dto.orders.OrdersDTO;
import himedia.project.highfourm.entity.OrderDetail;
import himedia.project.highfourm.entity.Orders;
import himedia.project.highfourm.entity.Product;
import himedia.project.highfourm.entity.ProductionPlan;
import himedia.project.highfourm.repository.OrderDetailRepository;
import himedia.project.highfourm.repository.OrdersRepository;
import himedia.project.highfourm.repository.ProductRepository;
import himedia.project.highfourm.repository.ProductionPlanRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderService {

	private final OrdersRepository ordersRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final ProductRepository productRepository;
	private final ProductionPlanRepository productionPlanRepository;
	private final EntityManager em;

	public Map<String, Object> findAllOrders() {
		List<Orders> orders = ordersRepository.findAll(Sort.by(Sort.Direction.DESC, "orderId"));
		Map<String, String> productNames = findProductNames();

		List<OrdersDTO> ordersDTOs = orders.stream().map(Orders::fromEntity).collect(Collectors.toList());

		Map<String, Object> response = new HashMap<>();
		response.put("orders", ordersDTOs);
		response.put("productNames", productNames);

		return response;
	}
	
	@Transactional
	public void saveOrder(OrdersAndDetailsDTO ordersAndDetailsDTO) {
		LocalDate orderDate = ordersAndDetailsDTO.getOrders().getOrderDate();
		OrdersDTO ordersDTO = ordersAndDetailsDTO.getOrders();
		
		String orderId = orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-"
				+ (ordersRepository.countOrdersWithOrderDate(orderDate) + 1);
		ordersDTO.setOrderId(orderId);
		Orders orders = ordersDTO.toEntity(ordersDTO);
		
		List<OrderFormDTO> orderForm = ordersAndDetailsDTO.getOrderDetails();
		List<OrderDetail> orderDetails = orderForm.stream()
				.map(e -> e.toEntity(orders,productRepository.findByProductName(e.getProductName())))
				.toList();
		
		orders.assignOrderDetail(orderDetails);
		em.persist(orders);
		
		saveProductionPlan(orders, orderDetails);
	}

	public void saveProductionPlan(Orders orders, List<OrderDetail> orderDetails) {
		List<ProductionPlan> productionPlans = orderDetails.stream()
				.map(detail -> ProductionPlanFormDTO.toEntityForInsert(
						orders.getOrderId().replaceAll("-", "").concat("-")
								.concat(detail.getOrderDetailPK().getProductId()),
						"EA", detail.getOrders(), detail.getProduct()))
				.collect(Collectors.toList());

		productionPlans.stream().forEach(e -> productionPlanRepository.save(e));
	}

	public Map<String, Object> orderSearch(String searchType, String search) {
		Map<String, Object> response = new HashMap<>();
		Map<String, String> productNames = findProductNames();
		response.put("productNames", productNames);

		if (searchType.equals("주문 번호")) {
			response.put("orders", ordersRepository.findByOrderId("%" + search + "%").stream()
					.map(Orders::fromEntity).collect(Collectors.toList()));
		} else if (searchType.equals("거래처명")) {
			response.put("orders", ordersRepository.findByVendor("%" + search + "%").stream()
					.map(Orders::fromEntity).collect(Collectors.toList()));
		} else if (searchType.equals("담당자")) {
			response.put("orders", ordersRepository.findByManager("%" + search + "%").stream()
					.map(Orders::fromEntity).collect(Collectors.toList()));
		} else if (searchType.equals("품목")) {
			response.put("orders", ordersRepository.findByProductName("%" + search + "%").stream()
					.map(Orders::fromEntity).collect(Collectors.toList()));
		}
		if (response.isEmpty()) {
			response.put("orders", new ArrayList<OrdersDTO>());
		}

		return response;
	}

	public Map<String, String> findProductNames() {
		List<Product> products = productRepository.findAll();
		Map<String, String> productNames = new HashMap<>();
		products.stream().forEach(e -> productNames.put(e.getProductId(), e.getProductName()));

		return productNames;
	}
	
	public Map<String, String> findProductId(){
		List<Product> products = productRepository.findAll();
		Map<String, String> productIds = new HashMap<>();
		products.stream().forEach(e -> productIds.put(e.getProductName(), e.getProductId()));
		
		return productIds;
	}
}
