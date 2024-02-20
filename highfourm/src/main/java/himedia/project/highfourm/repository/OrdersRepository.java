package himedia.project.highfourm.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import himedia.project.highfourm.dto.orders.OrdersDTO;
import himedia.project.highfourm.entity.Orders;
import jakarta.transaction.Transactional;

public interface OrdersRepository extends JpaRepository<Orders, String>{
	@Query(value = "SELECT count(o) FROM Orders o where o.orderDate = ?1")
	int countOrdersWithOrderDate(LocalDate orderDate);
	
	@Query("SELECT o FROM Orders o WHERE o.orderId LIKE ?1")
	List<Orders> findByOrderId(String search);
	
	@Query("SELECT o FROM Orders o WHERE o.vendor LIKE ?1")
	List<Orders> findByVendor(String search);
	
	@Query("SELECT o FROM Orders o WHERE o.manager LIKE ?1")
	List<Orders> findByManager(String search);
	
	@Query("SELECT o FROM Orders o " +
		       "JOIN o.orderDetails d " +
		       "JOIN d.product p " +
		       "WHERE p.productName LIKE ?1")
	List<Orders> findByProductName(String search);

	@Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.endingState = true WHERE o.orderId = :orderId")
    void updateEndingStateToY(String orderId);
}