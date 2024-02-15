package himedia.project.highfourm.service;

import java.util.ArrayList;
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
	
	public List<ProductDTO> findById(String productId) {
		Optional<Product> productEntity = productRepository.findById(productId);
		ProductDTO productDTO = productEntity.get().toProductDTO();
		List<ProductDTO> productList =List.of(productDTO);
		return productList;
	}
	
	public List<String> findAllProductName(){
		return productRepository.findAllProductNames();
	}

	public List<ProductDTO> findByProductName(String productName) {
		return productRepository.findAllByProductName(productName)
				.stream().map(product -> product.toProductDTO()).collect(Collectors.toList());
	}
	
	public List<ProductDTO> search(String searchType, String search) {
		List<Product> productList = new ArrayList<Product>();
		
		if(searchType.equals("제품 코드")) {
			productList = productRepository.findAllByProductId(search);
		} else if(searchType.equals("제품명")) {
			productList = productRepository.findAllByProductName(search);
		}
		return productList.stream()
				.map(product -> product.toProductDTO()).toList();
	}
}
