import { Component, DoCheck, EventEmitter, OnInit, Output, HostListener, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../services/cart.service';
import { CategoryService, CategoryNode } from '../../services/category.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss']
})
export class NavbarComponent implements DoCheck, OnInit {

  cartCount = 0;
  searchText = '';
  lastScrollTop = 0;
  isVisible = true;
  cartBump = false;

  categories: CategoryNode[] = [];
  subcategories: CategoryNode[] = [];
  selectedGender = 0;

  @Output() cartClicked = new EventEmitter<void>();
  @Output() searchChanged = new EventEmitter<string>();
  @Output() genderChanged = new EventEmitter<number>();
  @Output() categoryChanged = new EventEmitter<number>();
  @HostListener('window:scroll', [])
  onWindowScroll() {
    const currentScroll = window.pageYOffset || document.documentElement.scrollTop;

    if (currentScroll > this.lastScrollTop && currentScroll > 100) {
      this.isVisible = false;
    } else {
      this.isVisible = true;
    }

    this.lastScrollTop = currentScroll <= 0 ? 0 : currentScroll;
  }
 constructor(
  private cartService: CartService,
  private categoryService: CategoryService,
  private cdr: ChangeDetectorRef
) {}
  ngOnInit(): void {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Error cargando categorías', err);
      }
    });
  }

  previousCount = 0;

ngDoCheck(): void {
  const currentCount = this.cartService.getCount();
  this.cartCount = currentCount;

  if (currentCount > this.previousCount) {
    this.animateCart();
  }

  this.previousCount = currentCount;
}
  openCart() {
    this.cartClicked.emit();
  }

  onSearchChange() {
    this.searchChanged.emit(this.searchText);
  }

  filterByGender(genderId: number) {
    this.selectedGender = genderId;
    this.genderChanged.emit(genderId);

    if (genderId === 0) {
      this.subcategories = [];
      this.categoryChanged.emit(0);
      return;
    }

    const gender = this.categories.find(c => c.id === genderId);
    this.subcategories = gender?.children ?? [];
    this.categoryChanged.emit(0);
  }

  filterByCategory(categoryId: number) {
    this.categoryChanged.emit(categoryId);
  }

 animateCart() {
  this.cartBump = false;
  this.cdr.detectChanges();

  setTimeout(() => {
    this.cartBump = true;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.cartBump = false;
      this.cdr.detectChanges();
    }, 300);
  }, 20);
}
}