package himedia.project.highfourm.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.ProductDTO;
import himedia.project.highfourm.entity.Product;
import himedia.project.highfourm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	
	public List<ProductDTO> findAllProduct(){
		List<ProductDTO> resultList = productRepository.findAll().stream()
	            .map(product -> product.toProductDTO())
	            .collect(Collectors.toList());
		return resultList;
	}
	
	public Optional<Product> findById(String productId) {
		return productRepository.findById(productId);
	}
	
	public List<String> findAllProductName(){
		return productRepository.findAllProductNames();
	}

	public List<ProductDTO> findByProductId(String productId) {
		return productRepository.findByProductId(productId)
				.stream().map(product -> product.toProductDTO()).collect(Collectors.toList());
	}

	public List<ProductDTO> findByProductName(String productName) {
		return productRepository.findAllByProductName(productName)
				.stream().map(product -> product.toProductDTO()).collect(Collectors.toList());
	}
}