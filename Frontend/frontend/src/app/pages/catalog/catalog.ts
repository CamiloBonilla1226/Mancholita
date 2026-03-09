import { Component, OnInit, ChangeDetectorRef, Input, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/products.service';
import { Product } from '../../models/product';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog.html',
  styleUrl: './catalog.scss'
})
export class CatalogComponent implements OnInit, OnChanges {

  @Input() searchText = '';
  @Input() selectedGender = 0;
  @Input() selectedCategory = 0;
  currentPage = 1;
  pageSize = 6;


  products: Product[] = [];

  constructor(
    private productService: ProductService,
    private cdr: ChangeDetectorRef,
    private cartService: CartService
  ) { }

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data: any) => {
        this.products = data.content;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error cargando productos', err);
      }
    });
  }

  addToCart(product: Product) {
    this.cartService.addProduct(product);
  }

  get filteredProducts(): Product[] {
    let filtered = this.products;

    if (this.selectedGender !== 0) {
      filtered = filtered.filter(p => p.genderId === this.selectedGender);
    }

    if (this.selectedCategory !== 0) {
      filtered = filtered.filter(p => p.categoryId === this.selectedCategory);
    }

    if (this.searchText.trim()) {
      filtered = filtered.filter(p =>
        p.name.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }

    return filtered;
  }

  get paginatedProducts(): Product[] {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    return this.filteredProducts.slice(start, end);
  }

  get totalPages(): number {
    return Math.ceil(this.filteredProducts.length / this.pageSize);
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }
  ngOnChanges() {
  this.currentPage = 1;
  }


}