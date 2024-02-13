package himedia.project.highfourm.dto.orders;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import himedia.project.highfourm.entity.OrderDetail;
import himedia.project.highfourm.entity.Orders;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdersDTO {
	private String orderId;
	private String vendor;
	private String manager; 
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDate; 
	private Boolean endingState;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate orderDate;
	private List<OrderDetailDTO> orderDetails;
	
	public Orders toEntity(OrdersDTO ordersDTO) {
	    return Orders.builder()
	            .orderId(ordersDTO.getOrderId())
	            .vendor(ordersDTO.getVendor())
	            .manager(ordersDTO.getManager())
	            .dueDate(ordersDTO.getDueDate())
	            .endingState(ordersDTO.getEndingState())
	            .orderDate(ordersDTO.getOrderDate())
	            .build();
	}

	@Builder
	public OrdersDTO(String orderId, String vendor, String manager, LocalDate dueDate, Boolean endingState,
			LocalDate orderDate, List<OrderDetail> orderDetails) {
		super();
		this.orderId = orderId;
		this.vendor = vendor;
		this.manager = manager;
		this.dueDate = dueDate;
		this.endingState = endingState;
		this.orderDate = orderDate;
		this.orderDetails = orderDetails.stream()
			    .map(OrderDetailDTO::fromEntity)
			    .collect(Collectors.toList());
	}
}