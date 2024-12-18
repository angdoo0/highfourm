package himedia.project.highfourm.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import himedia.project.highfourm.dto.orders.OrdersDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Orders {
	@Id
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "vendor")
	private String vendor;
	
	@Column(name = "manager")
	private String manager; 
	
	@Column(name = "due_date")
	private LocalDate dueDate; 
	
	@Column(name = "ending_state")
	private Boolean endingState;
	
	@Column(name = "order_date")
	private LocalDate orderDate;
	
	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails;
	
	@OneToMany(mappedBy = "orders")
	private List<ProductionPlan> productionPlans;

	@Builder
	public Orders(String orderId, String vendor, String manager, LocalDate dueDate, Boolean endingState,
			LocalDate orderDate, List<OrderDetail> orderDetails, List<ProductionPlan> productionPlans) {
		this.orderId = orderId;
		this.vendor = vendor;
		this.manager = manager;
		this.dueDate = dueDate;
		this.endingState = endingState;
		this.orderDate = orderDate;
		this.orderDetails = orderDetails;
		this.productionPlans = productionPlans;
	}
	
   public OrdersDTO fromEntity() {
        return OrdersDTO.builder()
                .orderId(this.orderId)
                .vendor(this.vendor)
                .manager(this.manager)
                .dueDate(this.dueDate)
                .endingState(this.endingState)
                .orderDate(this.orderDate)
                .orderDetails(this.orderDetails)
                .build();
    }
   
   public void assignOrderDetail(List<OrderDetail> orderDetails) {
       this.orderDetails = orderDetails;
   }
   
}