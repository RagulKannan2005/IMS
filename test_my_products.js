const http = require('http');

const loginData = JSON.stringify({ email: 'supplier@ims.com', password: 'Supplier@123' });
const loginOptions = {
    hostname: 'localhost',
    port: 8082,
    path: '/api/v1/auth/login',
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Content-Length': loginData.length }
};

const req = http.request(loginOptions, res => {
    let data = '';
    res.on('data', chunk => data += chunk);
    res.on('end', () => {
        const token = JSON.parse(data).token;
        console.log('Login status:', res.statusCode);
        
        const req2 = http.request({
            hostname: 'localhost',
            port: 8082,
            path: '/api/v1/suppliers/my-products',
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token }
        }, res2 => {
            console.log('GET /my-products status:', res2.statusCode);
            let data2 = '';
            res2.on('data', chunk => data2 += chunk);
            res2.on('end', () => {
                console.log('Response:', data2);
            });
        });
        req2.end();
    });
});
req.write(loginData);
req.end();
