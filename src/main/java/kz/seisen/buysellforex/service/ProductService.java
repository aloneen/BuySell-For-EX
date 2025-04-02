package kz.seisen.buysellforex.service;


import kz.seisen.buysellforex.model.Product;
import kz.seisen.buysellforex.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public void saveProduct(Product product) {
        productRepository.save(product);
    }
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public void updateProduct(Product product) {
        productRepository.save(product);
    }


}
