const http = require('http');

const loginData = JSON.stringify({ email: 'admin@ims.com', password: 'Raguladmin@7' });
const loginOptions = {
    hostname: 'localhost',
    port: 8083,
    path: '/api/v1/auth/login',
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Content-Length': loginData.length }
};

const req = http.request(loginOptions, res => {
    let data = '';
    res.on('data', chunk => data += chunk);
    res.on('end', () => {
        const token = JSON.parse(data).token;
        console.log('Token received');
        
        const cats = [
            { name: 'Electronics', description: 'Devices', active_status: 'active' },
            { name: 'Office Supplies', description: 'Stationery', active_status: 'active' },
            { name: 'Furniture', description: 'Desks', active_status: 'active' }
        ];
        
        cats.forEach(c => {
            const catData = JSON.stringify(c);
            const req2 = http.request({
                hostname: 'localhost',
                port: 8083,
                path: '/api/v1/categories/newcategory',
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Content-Length': catData.length, 'Authorization': 'Bearer ' + token }
            }, res2 => {
                res2.on('data', d => process.stdout.write(d));
            });
            req2.write(catData);
            req2.end();
        });
    });
});
req.write(loginData);
req.end();
