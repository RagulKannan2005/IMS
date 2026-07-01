import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierPurchaseOrders } from './supplier-purchase-orders';

describe('SupplierPurchaseOrders', () => {
  let component: SupplierPurchaseOrders;
  let fixture: ComponentFixture<SupplierPurchaseOrders>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierPurchaseOrders],
    }).compileComponents();

    fixture = TestBed.createComponent(SupplierPurchaseOrders);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
