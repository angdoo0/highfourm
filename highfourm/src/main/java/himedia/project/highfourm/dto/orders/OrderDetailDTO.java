package himedia.project.highfourm.dto.orders;

import himedia.project.highfourm.entity.OrderDetail;
import himedia.project.highfourm.entity.Orders;
import himedia.project.highfourm.entity.Product;
import himedia.project.highfourm.entity.pk.MonthlyProductionPlanPK;
import himedia.project.highfourm.entity.pk.OrderDetailPK;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailDTO {
	private String orderId;
	private String productId;
	private Long productAmount;
	private Long unitPrice;
	
	public OrderDetail toEntity() {
	    return OrderDetail.builder()
	    		.orderDetailPK(new OrderDetailPK(this.orderId, this.productId))
		        .productAmount(this.productAmount)
		        .unitPrice(this.unitPrice)
		        .build();
	}
	
	public static OrderDetailDTO fromEntity(OrderDetail orderDetail) {
	    return new OrderDetailDTO(
	            orderDetail.getOrderDetailPK().getOrderId(),
	            orderDetail.getOrderDetailPK().getProductId(),
	            orderDetail.getProductAmount(),
	            orderDetail.getUnitPrice()
	    );
	}

	
	public static OrderDetailDTO fromFormDTO(OrderFormDTO formDTO, String productId) {
	    return OrderDetailDTO.builder()
	            .orderId(formDTO.getOrderId())
	            .productId(productId)
	            .productAmount(formDTO.getProductAmount())
	            .unitPrice(formDTO.getUnitPrice())
	            .build();
	}
}