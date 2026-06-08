const http = require('http');

// Register a new supplier
const registerData = JSON.stringify({
    username: 'test_supplier2',
    firstName: 'Test',
    lastName: 'Supplier',
    email: 'test_supplier2@ims.com',
    password: 'Password@123',
    phoneNumber: '1234567890',
    role: 'SUPPLIER'
});

const req = http.request({
    hostname: 'localhost',
    port: 8082,
    path: '/api/v1/auth/register',
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Content-Length': registerData.length }
}, res => {
    console.log('Register status:', res.statusCode);
    
    // Login
    const loginData = JSON.stringify({ email: 'test_supplier2@ims.com', password: 'Password@123' });
    const req2 = http.request({
        hostname: 'localhost',
        port: 8082,
        path: '/api/v1/auth/login',
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Content-Length': loginData.length }
    }, res2 => {
        let data2 = '';
        res2.on('data', chunk => data2 += chunk);
        res2.on('end', () => {
            const token = JSON.parse(data2).token;
            console.log('Login status:', res2.statusCode);
            
            // Add product
            const productData = JSON.stringify({
                sku: 'TEST-SKU-1',
                name: 'Test Product',
                description: 'Test',
                costPrice: 10.0,
                sellingPrice: 15.0,
                stockQuantity: 100,
                reorderLevel: 10,
                reorderQuantity: 50,
                active_status: 'active',
                category: 'Electronics'
            });
            
            const req3 = http.request({
                hostname: 'localhost',
                port: 8082,
                path: '/api/v1/products/addproduct',
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Content-Length': productData.length, 'Authorization': 'Bearer ' + token }
            }, res3 => {
                let data3 = '';
                res3.on('data', chunk => data3 += chunk);
                res3.on('end', () => {
                    console.log('Add Product status:', res3.statusCode);
                    console.log('Add Product response:', data3);
                    
                    // Get my-products
                    const req4 = http.request({
                        hostname: 'localhost',
                        port: 8082,
                        path: '/api/v1/suppliers/my-products',
                        method: 'GET',
                        headers: { 'Authorization': 'Bearer ' + token }
                    }, res4 => {
                        let data4 = '';
                        res4.on('data', chunk => data4 += chunk);
                        res4.on('end', () => {
                            console.log('Get My Products status:', res4.statusCode);
                            console.log('Get My Products response:', data4);
                        });
                    });
                    req4.end();
                });
            });
            req3.write(productData);
            req3.end();
        });
    });
    req2.write(loginData);
    req2.end();
});
req.write(registerData);
req.end();
