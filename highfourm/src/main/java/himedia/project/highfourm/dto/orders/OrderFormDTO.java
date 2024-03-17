package himedia.project.highfourm.dto.orders;

import himedia.project.highfourm.entity.OrderDetail;
import himedia.project.highfourm.entity.Orders;
import himedia.project.highfourm.entity.Product;
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
public class OrderFormDTO {
	private String orderId;
	private String productId;
	private Long productAmount;
	private Long unitPrice;
	private String productName;
	
	public OrderFormDTO(String productName, Long productionAmount, Long unitPrice) {
		this.productName = productName;
		this.productAmount = productionAmount;
		this.unitPrice = unitPrice;
	}
	
	public static OrderFormDTO fromEntity(OrderDetail orderDetail) {
        return OrderFormDTO.builder()
                .orderId(orderDetail.getOrderDetailPK().getOrderId())
                .productId(orderDetail.getOrderDetailPK().getProductId())
                .productAmount(orderDetail.getProductAmount())
                .unitPrice(orderDetail.getUnitPrice())
                .productName(orderDetail.getProduct().getProductName())
                .build();
    }
	
	public OrderDetail toEntity(Orders orders, Product product) {
		return OrderDetail.builder()
				.orderDetailPK(new OrderDetailPK(orders.getOrderId(), product.getProductId()))
				.orders(orders)
				.product(product)
				.productAmount(this.productAmount)
				.unitPrice(this.unitPrice)
				.build();
	}
	
}
