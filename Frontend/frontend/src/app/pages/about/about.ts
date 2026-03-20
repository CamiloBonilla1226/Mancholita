import { Component, EventEmitter, OnDestroy, OnInit, Output, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/products.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './about.html',
  styleUrls: ['./about.scss']
})
export class AboutComponent implements OnInit, OnDestroy {

  @Output() viewMoreClicked = new EventEmitter<any>();

  allProducts: any[] = [];
  visibleProducts: any[] = [];
  currentIndex = 0;
  itemsPerView = 4;
  isAnimating = false;
  slideDirection: 'left' | 'right' = 'left';

  // Mapped gender IDs for the hero buttons (based on available products)
  femaleGenderId: number | null = null;
  maleGenderId: number | null = null;

  selectedProduct: any = null;
  modalQuantity = 1;
  showAddedMessage = false;

  private autoSlideInterval: any;
  private slideTimeoutId: ReturnType<typeof setTimeout> | null = null;

  constructor(
  private productService: ProductService,
  private cartService: CartService,
  private cdr: ChangeDetectorRef
) {}

  ngOnInit(): void {
  this.productService.getProducts().subscribe({
    next: (data: any) => {
      const products = data.content ?? [];
      this.allProducts = this.shuffleArray(products);
      this.updateVisibleProducts();
      this.determineHeroGenderIds(products);

      setTimeout(() => {
        window.dispatchEvent(new Event('resize'));
        this.cdr.detectChanges();
      }, 50);

      this.startAutoSlide();
    },
    error: (err) => {
      console.error('Error cargando productos para About', err);
    }
  });
}

  ngOnDestroy(): void {
    this.clearSlideTimeout();
    this.clearAutoSlide();
  }

  nextProducts() {
    if (this.allProducts.length === 0 || this.isAnimating) return;

    this.resetAutoSlide();
    this.slideDirection = 'left';
    this.isAnimating = true;
    this.runSlideTransition(() => {
      this.currentIndex += this.itemsPerView;

      if (this.currentIndex >= this.allProducts.length) {
        this.currentIndex = 0;
        this.allProducts = this.shuffleArray([...this.allProducts]);
      }
    });
  }

  prevProducts() {
    if (this.allProducts.length === 0 || this.isAnimating) return;

    this.resetAutoSlide();
    this.slideDirection = 'right';
    this.isAnimating = true;
    this.runSlideTransition(() => {
      this.currentIndex -= this.itemsPerView;

      if (this.currentIndex < 0) {
        this.currentIndex = Math.max(0, this.allProducts.length - this.itemsPerView);
      }
    });
  }

  openProduct(product: any) {
    this.selectedProduct = product;
    this.modalQuantity = 1;
    this.showAddedMessage = false;
  }

  closeProduct() {
    this.selectedProduct = null;
    this.modalQuantity = 1;
    this.showAddedMessage = false;
  }

  increaseQuantity() {
    this.modalQuantity++;
  }

  decreaseQuantity() {
    if (this.modalQuantity > 1) {
      this.modalQuantity--;
    }
  }

  addToCart(product: any) {
    for (let i = 0; i < this.modalQuantity; i++) {
      this.cartService.addProduct(product);
    }

    this.showAddedMessage = true;

    setTimeout(() => {
      this.showAddedMessage = false;
    }, 2000);
  }

  viewMore(product: any) {
    this.viewMoreClicked.emit(product);
    this.closeProduct();
  }

  private startAutoSlide() {
    this.autoSlideInterval = setInterval(() => {
      this.nextProducts();
    }, 15000);
  }

  private resetAutoSlide() {
    this.clearAutoSlide();
    this.startAutoSlide();
  }

  private clearAutoSlide() {
    if (this.autoSlideInterval) {
      clearInterval(this.autoSlideInterval);
      this.autoSlideInterval = null;
    }
  }

  private clearSlideTimeout() {
    if (this.slideTimeoutId) {
      clearTimeout(this.slideTimeoutId);
      this.slideTimeoutId = null;
    }
  }

  private runSlideTransition(updateIndex: () => void) {
    this.clearSlideTimeout();

    this.slideTimeoutId = setTimeout(() => {
      updateIndex();
      this.updateVisibleProducts();
      this.cdr.detectChanges();

      requestAnimationFrame(() => {
        requestAnimationFrame(() => {
          this.isAnimating = false;
          this.cdr.detectChanges();
        });
      });
    }, 260);
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

  trackByProduct(index: number, product: any) {
    return product?.id ?? product?.imageUrl ?? `${product?.name ?? 'product'}-${index}`;
  }

  private shuffleArray(array: any[]) {
    return array
      .map(value => ({ value, sort: Math.random() }))
      .sort((a, b) => a.sort - b.sort)
      .map(({ value }) => value);
  }

  private determineHeroGenderIds(products: any[]) {
    const genderMap = new Map<string, number>();

    products.forEach((product) => {
      const name = String(product.genderName ?? '').trim().toLowerCase();
      if (!name) return;
      if (!genderMap.has(name)) {
        genderMap.set(name, product.genderId);
      }
    });

    // Common Spanish names for gender categories
    this.femaleGenderId = genderMap.get('mujer') ?? genderMap.get('femenino') ?? null;
    this.maleGenderId = genderMap.get('hombre') ?? genderMap.get('masculino') ?? null;
  }

  viewCollection(genderId: number | null) {
    this.viewMoreClicked.emit({ genderId });
  }
}
