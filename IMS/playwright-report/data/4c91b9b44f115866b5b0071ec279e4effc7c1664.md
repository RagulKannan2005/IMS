# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-dashboard.spec.ts >> IMS Admin Dashboard E2E Tests >> Full Admin Journey: Register, Login, and Manage Inventory
- Location: e2e\admin-dashboard.spec.ts:13:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:4200/register
Call log:
  - navigating to "http://localhost:4200/register", waiting until "load"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | test.describe('IMS Admin Dashboard E2E Tests', () => {
  4   |   
  5   |   test.beforeEach(async ({ page }) => {
  6   |     // Automatically dismiss browser alert dialogs by accepting them
  7   |     page.on('dialog', async (dialog) => {
  8   |       console.log(`[Alert Dialog]: ${dialog.message()}`);
  9   |       await dialog.accept();
  10  |     });
  11  |   });
  12  | 
  13  |   test('Full Admin Journey: Register, Login, and Manage Inventory', async ({ page }) => {
  14  |     // Step 1: Registration
  15  |     const randomSuffix = Math.floor(Math.random() * 10000);
  16  |     const adminEmail = `e2e_admin_${randomSuffix}@example.com`;
  17  |     const adminUser = `e2e_admin_${randomSuffix}`;
  18  | 
  19  |     console.log(`Starting Registration for ${adminEmail}`);
> 20  |     await page.goto('/register');
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:4200/register
  21  |     await expect(page).toHaveURL(/.*register/);
  22  | 
  23  |     await page.fill('#username', adminUser);
  24  |     await page.fill('#firstname', 'E2E');
  25  |     await page.fill('#lastname', 'Admin');
  26  |     await page.fill('#email', adminEmail);
  27  |     await page.fill('#password', 'password123');
  28  |     await page.fill('#phone_number', '1234567890');
  29  |     await page.selectOption('#role', 'ADMIN');
  30  |     
  31  |     // Click register button
  32  |     await page.click('button[type="submit"]');
  33  | 
  34  |     // Step 2: Login
  35  |     console.log('Logging in...');
  36  |     await page.goto('/login');
  37  |     await page.fill('#email', adminEmail);
  38  |     await page.fill('#password', 'password123');
  39  |     await page.click('button[type="submit"]');
  40  |     
  41  |     // Verify redirect to admin dashboard
  42  |     await expect(page).toHaveURL(/.*admin/);
  43  |     console.log('Successfully logged in as Admin');
  44  | 
  45  |     // Step 3: Add a Manager (required for Warehouse assignment)
  46  |     console.log('Navigating to User Management...');
  47  |     await page.click('text=User Management');
  48  |     await page.click('text=Users');
  49  |     await expect(page).toHaveURL(/.*users/);
  50  | 
  51  |     await page.click('text=Add User');
  52  |     await page.selectOption('select[name="role"]', 'MANAGER');
  53  |     await page.fill('input[name="username"]', `e2e_mgr_${randomSuffix}`);
  54  |     await page.fill('input[name="firstName"]', 'E2E');
  55  |     await page.fill('input[name="lastName"]', 'Manager');
  56  |     await page.fill('input[name="email"]', `e2e_manager_${randomSuffix}@example.com`);
  57  |     await page.fill('input[name="phone_number"]', '9876543210');
  58  |     await page.fill('input[name="password"]', 'passwordManager');
  59  |     await page.click('button[type="submit"]'); // Submits form
  60  |     
  61  |     // Wait for the table to refresh and show manager
  62  |     await expect(page.locator('table')).toContainText(`e2e_mgr_${randomSuffix}`);
  63  |     console.log('Manager user created successfully');
  64  | 
  65  |     // Step 4: Add a Category
  66  |     console.log('Navigating to Categories...');
  67  |     await page.click('text=Inventory');
  68  |     await page.click('text=Categories');
  69  |     await expect(page).toHaveURL(/.*categories/);
  70  | 
  71  |     await page.click('text=Add Category');
  72  |     await page.fill('input[name="name"]', `E2E_Category_${randomSuffix}`);
  73  |     await page.fill('input[name="description"]', 'E2E Testing Category');
  74  |     await page.selectOption('select[name="active_status"]', 'active');
  75  |     await page.click('button:has-text("Add Category")');
  76  | 
  77  |     await expect(page.locator('table')).toContainText(`E2E_Category_${randomSuffix}`);
  78  |     console.log('Category created successfully');
  79  | 
  80  |     // Step 5: Add a Product
  81  |     console.log('Navigating to Products...');
  82  |     await page.click('text=Inventory');
  83  |     await page.click('text=Products');
  84  |     await expect(page).toHaveURL(/.*products/);
  85  | 
  86  |     await page.click('text=Add Product');
  87  |     await page.fill('input[name="sku"]', `SKU-${randomSuffix}`);
  88  |     await page.fill('input[name="name"]', `E2E_Product_${randomSuffix}`);
  89  |     await page.fill('textarea[name="description"]', 'E2E Testing Product');
  90  |     await page.fill('input[name="costPrice"]', '500');
  91  |     await page.fill('input[name="sellingPrice"]', '750');
  92  |     await page.fill('input[name="stockQuantity"]', '100');
  93  |     await page.fill('input[name="reorderLevel"]', '10');
  94  |     await page.fill('input[name="reorderQuantity"]', '25');
  95  |     await page.selectOption('select[name="active_status"]', 'ACTIVE');
  96  |     await page.selectOption('select[name="category"]', `E2E_Category_${randomSuffix}`);
  97  |     await page.click('button:has-text("Save Product")');
  98  | 
  99  |     await expect(page.locator('table')).toContainText(`E2E_Product_${randomSuffix}`);
  100 |     console.log('Product created successfully');
  101 | 
  102 |     // Step 6: Create a Warehouse
  103 |     console.log('Navigating to Warehouses...');
  104 |     await page.click('text=Warehouses');
  105 |     await expect(page).toHaveURL(/.*warehouse/);
  106 | 
  107 |     await page.click('text=Add Warehouse');
  108 |     await page.fill('input[name="name"]', `E2E_Warehouse_${randomSuffix}`);
  109 |     await page.fill('input[name="warehouseCode"]', `WH-${randomSuffix}`);
  110 |     await page.fill('input[name="capacity"]', '50000');
  111 |     // Select the manager we created
  112 |     await page.selectOption('select[name="managerSelect"]', { index: 1 });
  113 |     await page.fill('input[name="contactNumber"]', '1234567890');
  114 |     // Select the manager email
  115 |     await page.selectOption('select[name="email"]', { index: 1 });
  116 |     await page.click('button[type="submit"]');
  117 | 
  118 |     await expect(page.locator('table')).toContainText(`E2E_Warehouse_${randomSuffix}`);
  119 |     console.log('Warehouse created successfully');
  120 | 
```