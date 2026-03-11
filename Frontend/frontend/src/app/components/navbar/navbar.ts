import {
  Component,
  DoCheck,
  EventEmitter,
  OnInit,
  Output,
  HostListener,
  ChangeDetectorRef
} from '@angular/core';
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

  categories: CategoryNode[] = [];
  subcategories: CategoryNode[] = [];

  selectedGender = 0;
  selectedCategory = 0;

  cartBump = false;
  previousCount = 0;

  lastScrollTop = 0;
  isVisible = true;

  @Output() cartClicked = new EventEmitter<void>();
  @Output() searchChanged = new EventEmitter<string>();
  @Output() genderChanged = new EventEmitter<number>();
  @Output() categoryChanged = new EventEmitter<number>();
  @Output() logoClicked = new EventEmitter<void>();


  constructor(
    private cartService: CartService,
    private categoryService: CategoryService,
    private cdr: ChangeDetectorRef
  ) { }

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
  goToHome() {
    this.logoClicked.emit();
  }

  ngDoCheck(): void {
    const currentCount = this.cartService.getCount();
    this.cartCount = currentCount;

    if (currentCount > this.previousCount) {
      this.animateCart();
    }

    this.previousCount = currentCount;
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

  openCart() {
    this.cartClicked.emit();
  }

  onSearchChange() {
    this.searchChanged.emit(this.searchText);
  }

  filterByGender(genderId: number) {
    this.selectedGender = genderId;
    this.selectedCategory = 0;

    this.genderChanged.emit(genderId);
    this.categoryChanged.emit(0);

    const gender = this.categories.find(c => c.id === genderId);
    this.subcategories = gender?.children ?? [];
  }

  filterByCategory(categoryId: number) {
    this.selectedCategory = categoryId;
    this.categoryChanged.emit(categoryId);
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const currentScroll = window.pageYOffset || document.documentElement.scrollTop;

    if (currentScroll <= 0) {
      this.isVisible = true;
      this.lastScrollTop = 0;
      return;
    }

    if (currentScroll > this.lastScrollTop && currentScroll > 80) {
      this.isVisible = false;
    } else if (currentScroll < this.lastScrollTop) {
      this.isVisible = true;
    }

    this.lastScrollTop = currentScroll;
  }
}