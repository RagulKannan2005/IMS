import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Maindashboard } from './maindashboard';

describe('Maindashboard', () => {
  let component: Maindashboard;
  let fixture: ComponentFixture<Maindashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Maindashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(Maindashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
