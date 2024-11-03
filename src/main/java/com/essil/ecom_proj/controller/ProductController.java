package com.essil.ecom_proj.controller;


import com.essil.ecom_proj.model.Product;
import com.essil.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = service.getProductById(id);
        if (product != null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        try{
            Product returnedProduct = service.addProduct(product, imageFile);
            return new ResponseEntity<>(returnedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {
        var product = service.getProductById(productId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok().body(imageFile);

    }

    @PutMapping("product/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable int productId, @RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product updatedProduct = null;
        try {
            updatedProduct = service.updateProduct(productId, product, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (updatedProduct != null)
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        else
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId) {
        var product = service.getProductById(productId);

        if (product != null){
            service.deleteProduct(productId);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }else
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);


    }

    @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        System.out.println("Searching with " + keyword);
        List<Product> searchedProducts = service.searchProduct(keyword);
        return new ResponseEntity<>(searchedProducts, HttpStatus.OK);
    }
}
