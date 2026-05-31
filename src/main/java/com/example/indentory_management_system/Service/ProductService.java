package com.example.indentory_management_system.Service;

import java.util.List;

import com.example.indentory_management_system.dto.*;

public interface ProductService {

    public ProductResponsedto createProduct(ProductRequestdto productRequestDto);
    public List<ProductResponsedto> getAllProducts();
    public List<ProductResponsedto> getActiveProducts();
    public ProductResponsedto getByProductId(Long id);
    public ProductResponsedto getByProductSku(String sku);
    public ProductResponsedto getByProductName(String name);
    public ProductResponsedto updateProduct(Long id, ProductRequestdto productRequestDto);
    public ProductResponsedto deleteProduct(Long id);
    public List<ProductResponsedto> findproductCategory(String name);
    public ProductResponsedto adjustStock(Long id, StockAdjustmentRequest request);
    public ProductResponsedto updateProductPrice(Long id, PriceUpdateRequestdto request);
}
