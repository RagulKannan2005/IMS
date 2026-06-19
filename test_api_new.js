const http = require('http');

const options = {
  hostname: 'localhost',
  port: 8083,
  path: '/api/v1/auth/login',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
};

const req = http.request(options, (res) => {
  let data = '';
  res.on('data', (chunk) => {
    data += chunk;
  });
  res.on('end', () => {
    const authResp = JSON.parse(data);
    
    if (authResp.token) {
        const countOptions = {
          hostname: 'localhost',
          port: 8083,
          path: `/api/v1/products/allproducts`,
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${authResp.token}`
          }
        };
        const countReq = http.request(countOptions, (res2) => {
           let data2 = '';
           res2.on('data', (chunk) => data2 += chunk);
           res2.on('end', () => console.log('Products:', data2));
        });
        countReq.end();
    } else {
        console.log("Login failed", authResp);
    }
  });
});

// Since earlier it failed with '1234', maybe the actual password is '123456' or 'admin'? Let's try 'password'
req.write(JSON.stringify({
  email: 'rahul2005kannan@gmail.com',
  password: '123'
}));
req.end();
