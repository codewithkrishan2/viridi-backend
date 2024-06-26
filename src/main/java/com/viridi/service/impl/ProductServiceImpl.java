package com.viridi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viridi.dto.ProductDetailDto;
import com.viridi.dto.ProductDto;
import com.viridi.dto.ReviewDto;
import com.viridi.entity.Category;
import com.viridi.entity.Product;
import com.viridi.entity.Review;
import com.viridi.exception.ResourceNotFoundException;
import com.viridi.repo.CategoryRepo;
import com.viridi.repo.ProductRepo;
import com.viridi.repo.ReviewRepo;
import com.viridi.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ReviewRepo reviewRepo;
	
	@Override
	public ProductDto addOneProduct(ProductDto productDto, Long categoryId) {
		
		Category category = categoryRepo.findById(categoryId).orElseThrow( ()-> new ResourceNotFoundException("Category ", " category id", categoryId));
		
		Product product = modelMapper.map(productDto, Product.class);
		product.setName(productDto.getName());
		product.setDescription(productDto.getDescription());
		product.setDetails(productDto.getDetails());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setImages(productDto.getImages());
		product.setCategory(category);
		product.setQuantity(productDto.getQuantity());
		
		Product saved = productRepo.save(product);
		ProductDto dto = modelMapper.map(saved, ProductDto.class);
		
		return dto;
	}

	@Override
	public ProductDto updateOneProduct(ProductDto productDto, Long id) {
		
		Product product = productRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product ", "Product Id ", id));
		
		Category category = categoryRepo.findById(product.getCategory().getId()).get();
		
		product.setName(productDto.getName());
		product.setDescription(productDto.getDescription());
		product.setDetails(productDto.getDetails());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setImages(productDto.getImages());
		product.setQuantity(productDto.getQuantity());
		product.setCategory(category);
		
		Product saved = productRepo.save(product);
		
		return modelMapper.map(saved, ProductDto.class);
	}

	@Override
	public void deleteOneProduct(Long productId) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "Product Id ", productId));
		productRepo.delete(product);
	}

	@Override
	public ProductDto getOneProductById(Long id) {
		Product product = productRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product", "Product Id ", id));
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public List<ProductDto> getAllProduct() {
		List<Product> all = productRepo.findAll();
		
		List<ProductDto> productDtos = all.stream().map((prouct)->modelMapper.map(prouct, ProductDto.class)).collect(Collectors.toList());
		
		return productDtos;
	}

	@Override
	public ProductDetailDto getOneProductDetailById(Long id) {
			Product product = productRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product", "Product Id ", id));
		
			List<Review> reviewsList = reviewRepo.findByProductId(id);
			
			ProductDetailDto productDetailDto = new ProductDetailDto();
			productDetailDto.setProduct(modelMapper.map(product, ProductDto.class));
			productDetailDto.setReviews(reviewsList.stream().map((review)->modelMapper.map(review, ReviewDto.class)).collect(Collectors.toList()));
			
			return productDetailDto;
	}

}
