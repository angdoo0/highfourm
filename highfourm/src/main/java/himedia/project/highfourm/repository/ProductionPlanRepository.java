package himedia.project.highfourm.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import himedia.project.highfourm.dto.PerformanceDTO;
import himedia.project.highfourm.dto.ProductionPlanFormDTO;
import himedia.project.highfourm.dto.ProductionStatusDTO;
import himedia.project.highfourm.dto.WorkPerformanceDTO;
import himedia.project.highfourm.dto.WorkPerformanceResponseDTO;
import himedia.project.highfourm.entity.ProductionPlan;

public interface ProductionPlanRepository extends JpaRepository<ProductionPlan, String> {
	
	@Query("SELECT NEW himedia.project.highfourm.dto.ProductionStatusDTO(pp.productionPlanId, p.productId, p.productName, pp.orders.orderId, pp.productionPlanAmount, pp.productionStartDate) " +
            "FROM ProductionPlan pp " +
            "JOIN pp.product p")
	List<ProductionStatusDTO> findStatus();	
	
	@Query("SELECT NEW himedia.project.highfourm.dto.WorkPerformanceResponseDTO(pp.productionPlanId, p.productId, p.productName) " +
            "FROM ProductionPlan pp " +
            "JOIN pp.product p")
	List<WorkPerformanceResponseDTO> findProductionPlanDetails();
	
	@Query("SELECT DISTINCT new himedia.project.highfourm.dto.ProductionPlanFormDTO" +
		       "(od.productAmount, o.orderId, o.orderDate, o.dueDate, p.productName, plan.productionPlanId, plan.productionStartDate, plan.productionPlanAmount) " +
		       "FROM ProductionPlan plan " +
		       "LEFT JOIN plan.orders o " +
		       "LEFT JOIN plan.product p " +
		       "LEFT JOIN o.orderDetails od "
		       + "WHERE od.orders = o and od.product = p")
	List<ProductionPlanFormDTO> findAllProductionPlan(Sort sort);
	
	@Query("SELECT DISTINCT new himedia.project.highfourm.dto.ProductionPlanFormDTO" +
			"(od.productAmount, o.orderId, o.orderDate, o.dueDate, p.productName, plan.productionPlanId, plan.productionStartDate, plan.productionPlanAmount) " +
			"FROM ProductionPlan plan " +
			"LEFT JOIN plan.orders o " +
			"LEFT JOIN plan.product p " +
			"LEFT JOIN o.orderDetails od "
			+ "WHERE od.orders = o and od.product = p and o.orderId like :search")
	List<ProductionPlanFormDTO> findProductionPlanByOrderId(Sort sort, @Param("search") String search);
	
	@Query("SELECT DISTINCT new himedia.project.highfourm.dto.ProductionPlanFormDTO" +
			"(od.productAmount, o.orderId, o.orderDate, o.dueDate, p.productName, plan.productionPlanId, plan.productionStartDate, plan.productionPlanAmount) " +
			"FROM ProductionPlan plan " +
			"LEFT JOIN plan.orders o " +
			"LEFT JOIN plan.product p " +
			"LEFT JOIN o.orderDetails od "
			+ "WHERE od.orders = o and od.product = p and o.vendor like :search")
	List<ProductionPlanFormDTO> findProductionPlanByVendor(Sort sort, @Param("search") String search);
	
	@Query("SELECT DISTINCT new himedia.project.highfourm.dto.ProductionPlanFormDTO" +
			"(od.productAmount, o.orderId, o.orderDate, o.dueDate, p.productName, plan.productionPlanId, plan.productionStartDate, plan.productionPlanAmount) " +
			"FROM ProductionPlan plan " +
			"LEFT JOIN plan.orders o " +
			"LEFT JOIN plan.product p " +
			"LEFT JOIN o.orderDetails od "
			+ "WHERE od.orders = o and od.product = p and p.productName like :search")
	List<ProductionPlanFormDTO> findProductionPlanByProductName(Sort sort, @Param("search") String search);
	
	@Query("SELECT new himedia.project.highfourm.dto.PerformanceDTO"
			+ "(plan.productionPlanId, plan.productionStartDate,plan.productionPlanAmount,o.orderId,o.vendor,o.manager,o.orderDate,o.dueDate,o.endingState,p.productId,p.productName,od.productAmount) "
			+ "FROM ProductionPlan plan "
			+ "LEFT JOIN plan.orders o "
			+ "LEFT JOIN plan.product p "
			+ "LEFT JOIN o.orderDetails od "
			+ "WHERE od.orders = o and od.product = p")
	List<PerformanceDTO> findAllPerformances(Sort sort);
	
	@Query("SELECT new himedia.project.highfourm.dto.PerformanceDTO"
			+ "(plan.productionPlanId, plan.productionStartDate,plan.productionPlanAmount,o.orderId,o.vendor,o.manager,o.orderDate,o.dueDate,o.endingState,p.productId,p.productName,od.productAmount) "
			+ "FROM ProductionPlan plan "
			+ "LEFT JOIN plan.orders o "
			+ "LEFT JOIN plan.product p "
			+ "LEFT JOIN o.orderDetails od "
			+ "WHERE od.orders = o and od.product = p and o.orderId like :search")
	List<PerformanceDTO> findPerformanceByOrderId(Sort sort,@Param("search") String search);
	
	@Query("SELECT new himedia.project.highfourm.dto.PerformanceDTO"
			+ "(plan.productionPlanId, plan.productionStartDate,plan.productionPlanAmount,o.orderId,o.vendor,o.manager,o.orderDate,o.dueDate,o.endingState,p.productId,p.productName,od.productAmount) "
			+ "FROM ProductionPlan plan "
			+ "LEFT JOIN plan.orders o "
			+ "LEFT JOIN plan.product p "
			+ "LEFT JOIN o.orderDetails od "
			+ "WHERE od.orders = o and od.product = p and o.vendor like :search")
	List<PerformanceDTO> findPerformanceByVendor(Sort sort, @Param("search") String search);
	
	@Query("SELECT new himedia.project.highfourm.dto.PerformanceDTO"
			+ "(plan.productionPlanId, plan.productionStartDate,plan.productionPlanAmount,o.orderId,o.vendor,o.manager,o.orderDate,o.dueDate,o.endingState,p.productId,p.productName,od.productAmount) "
			+ "FROM ProductionPlan plan "
			+ "LEFT JOIN plan.orders o "
			+ "LEFT JOIN plan.product p "
			+ "LEFT JOIN o.orderDetails od "
			+ "WHERE od.orders = o and od.product = p and p.productName like :search")
	List<PerformanceDTO> findPerformanceByProductName(Sort sort, @Param("search") String search);
	
	@Query("SELECT new himedia.project.highfourm.dto.PerformanceDTO"
			+ "(plan.productionPlanId, plan.productionStartDate,plan.productionPlanAmount,o.orderId,o.vendor,o.manager,o.orderDate,o.dueDate,o.endingState,p.productId,p.productName,od.productAmount) "
			+ "FROM ProductionPlan plan "
			+ "LEFT JOIN plan.orders o "
			+ "LEFT JOIN plan.product p "
			+ "LEFT JOIN o.orderDetails od "
			+ "WHERE plan.productionPlanId = ?1 and od.orders = o and od.product = p")
	PerformanceDTO findPerformances(String productionPlanId);
	
	@Query(value = "SELECT IFNULL(SUM(acceptance_amount),0) FROM work_performance WHERE production_plan_id like ?1" , nativeQuery = true)
	Long sumProductionAmount(String productionPlanId);
	
	@Query("SELECT new himedia.project.highfourm.dto.WorkPerformanceDTO(wp.productionPlan.productionPlanId, wp.workDate, wp.productionAmount, wp.acceptanceAmount, wp.defectiveAmount, wp.workingTime) " +
		       "FROM WorkPerformance wp " +
		       "WHERE wp.productionPlan.productionPlanId LIKE ?1")
	List<WorkPerformanceDTO> findProductionList(String productionPlanId);
}