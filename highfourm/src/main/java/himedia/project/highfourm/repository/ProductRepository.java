package himedia.project.highfourm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import himedia.project.highfourm.dto.ProductDTO;
import himedia.project.highfourm.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String>{
	@Query(value = "SELECT p FROM Product p where p.productName like ?1")
	Product findByProductName(String productName);
	
	@Query(value = "SELECT p.productName FROM Product p")
	List<String> findAllProductNames();
	
	@Query(value = "SELECT * from product where product_id like %?%", nativeQuery = true) 
	List<Product> findByProductId(String productId);
	
	@Query(value = "SELECT * from product where product_name like %?%", nativeQuery = true)
	List<Product> findAllByProductName(String productName);
}