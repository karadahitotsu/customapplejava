package karadahitotsu.customapple.controllers;

import karadahitotsu.customapple.entity.Cart;
import karadahitotsu.customapple.entity.Products;
import karadahitotsu.customapple.entity.Users;
import karadahitotsu.customapple.repository.CartRepository;
import karadahitotsu.customapple.repository.ProductsRepository;
import karadahitotsu.customapple.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    private Environment environment;
    @GetMapping("/")
    public String mainPage(Model model){
        List<Products> universal;
        List<Products> designer;
        universal = productsRepository.findByCategory("universal");
        designer = productsRepository.findByCategory("designer");
        Collections.shuffle(universal);
        Collections.shuffle(designer);

        if (universal.size() > 4) {
            universal = universal.subList(0, 4);
        }
        if (designer.size() > 4) {
            designer = designer.subList(0, 4);
        }
        model.addAttribute("universal",universal);
        model.addAttribute("designer",designer);

        return "main";
    }

    @GetMapping("/catalog")
    public String catalogPage(@RequestParam(name = "category",required = false,defaultValue = "all") String category,@RequestParam(name="page",required = false,defaultValue = "1")int page, Model model){
        List<Products> products;
        if(category.equals("all")){
            products = productsRepository.findAll();
            if(products.size()>12){
                if(products.size()<page*12){
                    int difference = page*12-products.size();
                    if(0+(page-1)*12>page*12-difference){
                        products=products.subList(0,0);
                    }
                    else{
                        products = products.subList(0+(page-1)*12,page*12-difference);

                    }

                }
                else{

                    products = products.subList(0+(page-1)*12,page*12);

                }
            }
            model.addAttribute("products",products);
        }
        else{
            String[] categorys = category.split("pon");
            products = productsRepository.findByModelIn(categorys);
            if(products.size()>12){
                if(products.size()<page*12){
                    int difference = page*12-products.size();
                    if(0+(page-1)*12>page*12-difference){
                        products=products.subList(0,0);
                    }
                    else{
                        products = products.subList(0+(page-1)*12,page*12-difference);

                    }

                }
                else{

                    products = products.subList(0+(page-1)*12,page*12);

                }
            }
            model.addAttribute("products",products);
        }
        return "catalog";
    }
    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }
    @GetMapping("/btob")
    public String btobPage(){
        return "btob";
    }
    @GetMapping("/cart")
    public String cartPage(@RequestParam(name="id",required = false)Long userid,Model model){
        Users user = usersRepository.getReferenceById(userid);
        List<Cart> cart = cartRepository.findByUserid(user);
        model.addAttribute("cart",cart);
        return "cart";
    }
    @GetMapping("/galery")
    public String galeryPage(){
        return "galery";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/make")
    public String makePage(){
        return "make";
    }
    @GetMapping("/profile")
    public String profilePage(@RequestParam(name="id",required = true)Long userid,Model model){
        model.addAttribute("login",usersRepository.getReferenceById(userid).getLogin());
        model.addAttribute("image",usersRepository.getReferenceById(userid).getPhoto());
        System.out.println(usersRepository.getReferenceById(userid).getPhoto());
        return "profile";
    }
    @GetMapping("/registration")
    public String registrationPage(){
        return "registration";
    }
    @GetMapping("/payment")
    public String paymentPage(@RequestParam(name="id",required = true)Long id,Model model){
        Users user = usersRepository.getReferenceById(id);
        List<Cart> cart = cartRepository.findByUserid(user);
        int summa = 0;
        for (int i = 0;i<cart.size();i++){
            Products products = cart.get(i).getProductid();
            summa+=products.getPrice()*cart.get(i).getCount();
        }
        model.addAttribute("summa",summa);
        return "payment";
    }
    @GetMapping("/product")
    public String productPage(@RequestParam(name="productid",required = true)Long productid,Model model){
        Products product = productsRepository.getReferenceById(productid);
        model.addAttribute("product",product);
        return "product";
    }
    @GetMapping("/images/{folder}/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String folder, @PathVariable String filename) throws IOException {
        String filePath = environment.getProperty("image.storage.path") + folder + "/" + filename;

        File file = new File(filePath);

        if (file.exists()) {
            byte[] imageBytes = new byte[(int) file.length()];
            try (InputStream inputStream = new FileInputStream(file)) {
                inputStream.read(imageBytes);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
