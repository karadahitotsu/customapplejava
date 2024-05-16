package karadahitotsu.customapple.controllers;

import karadahitotsu.customapple.entity.*;
import karadahitotsu.customapple.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
public class MainRestController {
    @Autowired
    private Environment environment;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    MakeRepository makeRepository;
    @Autowired
    BtoBRepository btoBRepository;
    @PostMapping("/api/createuser")
    public String createUser(@RequestBody Users user) {
        System.out.println(user.getLogin());
        user.setPhoto("default.png");
        usersRepository.save(user);
        return "user";

    }
    @PostMapping("/api/make")
    public void makerequest(@RequestBody MakeRequest makeRequest){

        makeRepository.save(makeRequest);


    }
    @PostMapping("/api/btob")
    public void btobrequest(@RequestBody BToBRequest bToBRequest){
        btoBRepository.save(bToBRequest);


    }
    @PostMapping("/api/cart/add")
    public void addCart(@RequestParam("userid") Long userid, @RequestParam("productid") Long productid){
        Users user = usersRepository.getReferenceById(userid);
        Products product = productsRepository.getReferenceById(productid);
        List<Cart> carts = cartRepository.findByUseridAndProductid(user,product);
        if(!carts.isEmpty()){
            Cart cart = carts.get(0);
            cart.setCount(cart.getCount()+1);

            cartRepository.save(cart);
        }
        else{
            Cart cart = new Cart();
            cart.setCount(1);
            cart.setProductid(product);
            cart.setUserid(user);
            cartRepository.save(cart);
        }
    }
    @PostMapping("/api/cart/delete")
    public void deleteCart(@RequestParam("cartid")Long cartid){
        Cart cart = cartRepository.getReferenceById(cartid);
        cartRepository.delete(cart);
    }
    @PostMapping("/api/loginuser")
    public HashMap loginUser(@RequestBody Users user) {
        HashMap<String,String> mappa = new HashMap<>();
        List<Users> userr = usersRepository.findByLoginAndPassword(user.getLogin(),user.getPassword());
        if(userr.isEmpty()){

            mappa.put("status","wrong data");
            return mappa;

        }
        mappa.put("userid",userr.get(0).getId().toString());
        return mappa;

    }
    @PostMapping("/api/payment")
    public void Payment(@RequestBody Payment payment){

        paymentRepository.save(payment);
    }
    @PostMapping("/api/image/catalog")
    public void uploadImageCatalog(@RequestParam("file") MultipartFile file,@RequestParam("previewFile") MultipartFile previewFile, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("category") String category, @RequestParam("price") Integer price,@RequestParam("model") String model)throws IOException{
        String filePath = saveImageCatalog(file);
        String previewPath = saveImageCatalog(previewFile);
        Products product = new Products();
        product.setCategory(category);
        product.setDescription(description);
        product.setName(name);
        product.setImagePath(filePath);
        product.setPreviewPath(previewPath);
        product.setPrice(price);
        System.out.println(model);
        System.out.println("kek");
        product.setModel(model);
        productsRepository.save(product);

    }
    @PostMapping("/api/image/profile")
    public void uploadImageProfile(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) throws IOException {
        // Сохранить файл
        String filePath = saveImageProfile(file);

        // Сохранить путь к файлу в базе данных
        Users user = usersRepository.findById(userId).orElseThrow();
        user.setPhoto(filePath);
        usersRepository.save(user);
    }
    private String saveImageCatalog(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        String filePath = environment.getProperty("image.storage.path")+"products/" + fileName;


        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] bytes = new byte[1024];
            int read;
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

        return fileName;
    }
    private String saveImageProfile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        String filePath = environment.getProperty("image.storage.path")+"avatars/" + fileName;


        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] bytes = new byte[1024];
            int read;
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

        return fileName;
    }
}
