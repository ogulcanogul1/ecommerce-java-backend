package com.ecommerce.project.services.productService;

import com.ecommerce.project.dtos.PaginationInfo;
import com.ecommerce.project.dtos.productDtos.CreateProductDto;
import com.ecommerce.project.dtos.productDtos.ProductDto;
import com.ecommerce.project.dtos.productDtos.ResponseProductDto;
import com.ecommerce.project.dtos.productDtos.UpdateProductDto;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.models.Category;
import com.ecommerce.project.models.Product;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.services.fileService.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, CategoryRepository categoryRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    @Override
    public ServiceResult<ResponseProductDto> getProducts(Integer pageSize, Integer pageNumber, String sortDirection, String sortBy) {

        Sort sort = sortDirection.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if(pageSize == null || pageNumber == null) {

            List<ProductDto> products = productRepository.findAll(sort).stream().map(product ->  modelMapper.map(product, ProductDto.class)).toList();

            if(products.isEmpty()){
                throw new APIException("No Product created yet!");
            }

            ResponseProductDto responseProductDto = new ResponseProductDto(
                products,
                products.stream().count(),
                null
            );
            return ServiceResult.success(responseProductDto, "all products");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize , sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDto> products = productPage.getContent().stream().map(product ->  modelMapper.map(product, ProductDto.class)).toList();

        PaginationInfo paginationInfo = new PaginationInfo(
                pageNumber,
                pageSize,
                productPage.getTotalPages()
        );
        ResponseProductDto responseProductDto = new ResponseProductDto(
                products,
                products.stream().count(),
                paginationInfo
        );

        return ServiceResult.success(responseProductDto, "get products by page");
    }

    @Override
    public ServiceResult<String> addProduct(CreateProductDto createProductDto,Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        if(category == null) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }

        Product product = new Product();

        product.setProductId(null);
        product.setImageUrl("default.png");
        product.setCategory(category);
        product.setProductName(createProductDto.getProductName());
        product.setPrice(createProductDto.getPrice());
        product.setQuantity(createProductDto.getQuantity());
        product.setDescription(createProductDto.getDescription());

        productRepository.save(product);

        return ServiceResult.success("Product added successfully!");
    }

    @Override
    public ServiceResult<String> deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (product == null){
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        productRepository.delete(product);
        return ServiceResult.success("deleted successfully!");
    }

    @Override
    public ServiceResult<ProductDto> updateProduct(Long productId, UpdateProductDto productRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

       Category category =  categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", productRequest.getCategoryId()));

        product.setCategory(category);
        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());

        productRepository.save(product);

        ProductDto dto = modelMapper.map(product, ProductDto.class);

        return ServiceResult.success(dto, "updated successfully!");
    }

    @Override
    public ServiceResult<ResponseProductDto> getProductsByCategory(Long categoryId, Integer pageSize, Integer pageNumber, String sortDirection, String sortBy) {

        Sort sort = sortDirection.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        if(pageSize == null || pageNumber == null) {

            List<Product> products = productRepository.getProductsByCategory(category, sort);

            if(products.isEmpty()){
                throw new APIException("No Product created yet!");
            }

            List<ProductDto> dto = products.stream().map(product ->  modelMapper.map(product, ProductDto.class)).toList();

            ResponseProductDto result =  new ResponseProductDto(
                dto,
                dto.stream().count(),
                null
            );

            return ServiceResult.success(result, "get products by category");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize , sort);

        Page<Product> productsPage = productRepository.getProductsByCategory(category,pageable);

        List<ProductDto> dto =  productsPage.getContent().stream().map(product ->  modelMapper.map(product, ProductDto.class)).toList();

        PaginationInfo paginationInfo = new PaginationInfo(
                pageNumber,
                pageSize,
                productsPage.getTotalPages()
        );

        ResponseProductDto result = new ResponseProductDto(
                dto,
                productsPage.getTotalElements(),
                paginationInfo
        );
        return ServiceResult.success(result, "get products by category with pagination");
    }

    @Override
    public ServiceResult<ResponseProductDto> getProductsByKeyword(String keyword, Integer pageSize, Integer pageNumber, String sortDirection, String sortBy) {

        Sort sort = sortDirection.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if(pageSize == null || pageNumber == null) {

            List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%", sort);

            if(products.isEmpty()){
                throw new APIException("No Product created yet!");
            }

            List<ProductDto> dto = products.stream().map(product ->  modelMapper.map(product, ProductDto.class)).toList();

            ResponseProductDto responseProductDto = new ResponseProductDto(
                dto,
                dto.stream().count(),
                null
            );
            return ServiceResult.success(responseProductDto, "get products by keyword");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize , sort);

        Page<Product> productsPage = productRepository.findByProductNameLikeIgnoreCase(keyword, pageable);

        List<ProductDto> dto = productsPage.getContent().stream().map(product ->  modelMapper.map(product, ProductDto.class)).toList();

        if(dto.isEmpty()){
            throw new APIException("No Product created yet!");
        }

        PaginationInfo paginationInfo = new PaginationInfo(
                pageNumber,
                pageSize,
                productsPage.getTotalPages()
        );
        ResponseProductDto responseProductDto = new ResponseProductDto(
                dto,
                productsPage.getTotalElements(),
                paginationInfo
        );
        return ServiceResult.success(responseProductDto, "get products by keyword with pagination");
    }

    @Override
    public ServiceResult<ProductDto> patchProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        String fileName = fileService.uploadImage(path, image);

        productFromDb.setImageUrl(fileName);

        productRepository.save(productFromDb);

        return ServiceResult.success(modelMapper.map(productFromDb, ProductDto.class), "Image updated successfully!");
    }


}
