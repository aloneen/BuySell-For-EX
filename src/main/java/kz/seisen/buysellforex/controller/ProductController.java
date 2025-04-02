package kz.seisen.buysellforex.controller;

import kz.seisen.buysellforex.model.Category;
import kz.seisen.buysellforex.model.Image;
import kz.seisen.buysellforex.model.Product;
import kz.seisen.buysellforex.model.User;
import kz.seisen.buysellforex.service.CategoryService;
import kz.seisen.buysellforex.service.ProductService;
import kz.seisen.buysellforex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;

    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            Model model) {
        List<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productService.searchByTitle(search);
        } else if (categoryId != null) {
            products = productService.filterByCategory(categoryId);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("searchQuery", search);
        return "index";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("category", product.getCategory().getName());
        return "detail";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add";
    }

    @PostMapping("/add")
    public String addProduct(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam int price,
            @RequestParam String city,
            @RequestParam Long category,
            @RequestParam("images") MultipartFile[] imageFiles) throws IOException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setCity(city);
        product.setUser(user); // Set the authenticated user

        Category productCategory = categoryService.getCategoryById(category);
        product.setCategory(productCategory);

        for (MultipartFile file : imageFiles) {
            if (file.getSize() > MAX_IMAGE_SIZE) {
                return "redirect:/";
            }
            if (!file.isEmpty() && !file.getContentType().startsWith("image/")) {
                continue;
            }
            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setBase64Data(Base64.getEncoder().encodeToString(file.getBytes()));
            product.addImage(image);
        }

        productService.saveProduct(product);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/login";
    }
}