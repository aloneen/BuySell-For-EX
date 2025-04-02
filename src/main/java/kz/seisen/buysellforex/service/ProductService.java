package kz.seisen.buysellforex.service;


import kz.seisen.buysellforex.model.Image;
import kz.seisen.buysellforex.model.Product;
import kz.seisen.buysellforex.repository.ImageRepository;
import kz.seisen.buysellforex.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;





    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

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
    public List<Product> searchByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Product> filterByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }


}
