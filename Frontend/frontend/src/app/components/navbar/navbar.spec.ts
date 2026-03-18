import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { NavbarComponent } from './navbar';
import { CartService } from '../../services/cart.service';
import { CategoryService } from '../../services/category.service';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  const cartServiceStub = {
    getCount: () => 0,
  };

  const categoryServiceStub = {
    getCategories: () => of([]),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarComponent],
      providers: [
        { provide: CartService, useValue: cartServiceStub },
        { provide: CategoryService, useValue: categoryServiceStub },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
