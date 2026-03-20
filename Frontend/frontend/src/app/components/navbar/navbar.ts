import {
  Component,
  DoCheck,
  EventEmitter,
  OnInit,
  Output,
  HostListener,
  ChangeDetectorRef,
  Input,
  OnChanges,
  SimpleChanges
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
export class NavbarComponent implements DoCheck, OnInit, OnChanges {

  @Input() currentGender = 0;
  @Input() currentCategory = 0;
  @Input() hideSubcategories = false;

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

  searchOpen = false;

  @Output() cartClicked = new EventEmitter<void>();
  @Output() searchChanged = new EventEmitter<string>();
  @Output() genderChanged = new EventEmitter<number>();
  @Output() categoryChanged = new EventEmitter<number>();
  @Output() logoClicked = new EventEmitter<void>();

  constructor(
    private cartService: CartService,
    private categoryService: CategoryService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
        this.syncNavbarState();
      },
      error: (err) => {
        console.error('Error cargando categorÃ­as', err);
      }
    });

    this.cartCount = this.cartService.getCount();
    this.previousCount = this.cartCount;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['currentGender'] || changes['currentCategory']) {
      this.syncNavbarState();
    }
  }

  ngDoCheck(): void {
    const currentCount = this.cartService.getCount();

    if (currentCount !== this.cartCount) {
      this.cartCount = currentCount;
    }

    if (currentCount > this.previousCount) {
      this.animateCart();
    }

    this.previousCount = currentCount;
  }

  private syncNavbarState(): void {
    this.selectedGender = this.currentGender ?? 0;
    this.selectedCategory = this.currentCategory ?? 0;

    if (!this.categories.length) return;

    if (this.selectedGender > 0) {
      const gender = this.categories.find(c => c.id === this.selectedGender);
      this.subcategories = gender?.children ?? [];
    } else {
      this.subcategories = [];
    }
  }

  animateCart(): void {
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

  openCart(): void {
    this.cartClicked.emit();
  }

  onSearchChange(): void {
    this.searchChanged.emit(this.searchText);
  }

  toggleSearch(): void {
    this.searchOpen = !this.searchOpen;

    if (!this.searchOpen) {
      this.searchText = '';
      this.searchChanged.emit('');
    }
  }

  filterByGender(genderId: number): void {
    this.selectedGender = genderId;
    this.selectedCategory = 0;

    this.genderChanged.emit(genderId);
    this.categoryChanged.emit(0);

    const gender = this.categories.find(c => c.id === genderId);
    this.subcategories = gender?.children ?? [];
  }

  filterByCategory(categoryId: number): void {
    this.selectedCategory = categoryId;
    this.categoryChanged.emit(categoryId);
  }

  goToHome(): void {
    this.selectedGender = 0;
    this.selectedCategory = 0;
    this.subcategories = [];
    this.searchText = '';
    this.searchOpen = false;

    this.genderChanged.emit(0);
    this.categoryChanged.emit(0);
    this.searchChanged.emit('');

    this.logoClicked.emit();
  }

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
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
