package com.viridi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridi.dto.ProductDetailDto;
import com.viridi.dto.ProductDto;
import com.viridi.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	// add product: localhost:8080/api/v1/product/1/add

	@PostMapping("/category/{categoryId}/add")
	public ResponseEntity<?> addOneProduct(@PathVariable Long categoryId, @Valid @RequestBody ProductDto productDto) {

		ResponseEntity<?> response = null;

		try {
			ProductDto addedProduct = productService.addOneProduct(productDto, categoryId);

			response = new ResponseEntity<ProductDto>(addedProduct, HttpStatus.CREATED);

		} catch (Exception e) {
			response = new ResponseEntity<String>("Unable to save product", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}

		return response;
	}

	// get product by id
	@GetMapping("/{id}")
	public ResponseEntity<?> getOneProduct(@PathVariable Long id) {

		ResponseEntity<?> response = null;

		try {
			ProductDto productById = productService.getOneProductById(id);
			response = new ResponseEntity<ProductDto>(productById, HttpStatus.OK);

		} catch (Exception e) {
			response = new ResponseEntity<String>("Unable to get product with id "+id, HttpStatus.NOT_FOUND);
			e.printStackTrace();
		}

		return response;
	}
	
	

	// get detailed product with reviews by id
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getOneProductDetailedDto(@PathVariable Long id) {

		ResponseEntity<?> response = null;

		try {
			ProductDetailDto oneProductDetailById = productService.getOneProductDetailById(id);
			response = new ResponseEntity<ProductDetailDto>(oneProductDetailById, HttpStatus.OK);

		} catch (Exception e) {
			response = new ResponseEntity<String>("Unable to get product with id "+id, HttpStatus.NOT_FOUND);
			e.printStackTrace();
		}

		return response;
	}

	// get all product
	@GetMapping("/all")
	public ResponseEntity<?> getAllProductsEntity() {

		ResponseEntity<?> response = null;

		try {
			List<ProductDto> allProduct = productService.getAllProduct();
			response = new ResponseEntity<List<ProductDto>>(allProduct, HttpStatus.OK);

		} catch (Exception e) {
			response = new ResponseEntity<String>("Unable to get all product", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}

		return response;
	}

	// update product
	@PutMapping("/{id}")
	public ResponseEntity<?> updateTheProduct(@Valid @RequestBody ProductDto productDto, @PathVariable Long id) {

		ResponseEntity<?> response = null;

		try {
			ProductDto updatedProduct = productService.updateOneProduct(productDto, id);
			response = new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<String>("Unable to Update the product", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}

		return response;
	}

	// delete product
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteproductEntity(@PathVariable Long id) {

		ResponseEntity<?> response = null;

		try {
			productService.deleteOneProduct(id);
			response = new ResponseEntity<String>("Product deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<String>("Unable to delete product", HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}

		return response;
	}

}
