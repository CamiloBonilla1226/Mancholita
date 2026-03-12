import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/products.service';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './about.html',
  styleUrls: ['./about.scss']
})
export class AboutComponent implements OnInit, OnDestroy {

  allProducts: any[] = [];
  visibleProducts: any[] = [];
  currentIndex = 0;
  itemsPerView = 3;
  isAnimating = false;

  private autoSlideInterval: any;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data: any) => {
        const products = data.content ?? [];
        this.allProducts = this.shuffleArray(products);
        this.updateVisibleProducts();
        this.startAutoSlide();
      },
      error: (err) => {
        console.error('Error cargando productos para About', err);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.autoSlideInterval) {
      clearInterval(this.autoSlideInterval);
    }
  }

  nextProducts() {
    if (this.allProducts.length === 0 || this.isAnimating) return;

    this.isAnimating = true;

    setTimeout(() => {
      this.currentIndex += this.itemsPerView;

      if (this.currentIndex >= this.allProducts.length) {
        this.currentIndex = 0;
        this.allProducts = this.shuffleArray([...this.allProducts]);
      }

      this.updateVisibleProducts();
      this.isAnimating = false;
    }, 250);
  }

  prevProducts() {
    if (this.allProducts.length === 0 || this.isAnimating) return;

    this.isAnimating = true;

    setTimeout(() => {
      this.currentIndex -= this.itemsPerView;

      if (this.currentIndex < 0) {
        this.currentIndex = Math.max(0, this.allProducts.length - this.itemsPerView);
      }

      this.updateVisibleProducts();
      this.isAnimating = false;
    }, 250);
  }

  private startAutoSlide() {
    this.autoSlideInterval = setInterval(() => {
      this.nextProducts();
    }, 10000);
  }

  private updateVisibleProducts() {
    this.visibleProducts = this.allProducts.slice(
      this.currentIndex,
      this.currentIndex + this.itemsPerView
    );

    if (this.visibleProducts.length < this.itemsPerView && this.allProducts.length > 0) {
      const faltantes = this.itemsPerView - this.visibleProducts.length;
      this.visibleProducts = [
        ...this.visibleProducts,
        ...this.allProducts.slice(0, faltantes)
      ];
    }
  }

  private shuffleArray(array: any[]) {
    return array
      .map(value => ({ value, sort: Math.random() }))
      .sort((a, b) => a.sort - b.sort)
      .map(({ value }) => value);
  }
}