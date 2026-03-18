import {
  Component,
  OnInit,
  ChangeDetectorRef,
  Input,
  OnChanges,
  NgZone
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/products.service';
import { Product } from '../../models/product';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog.html',
  styleUrls: ['./catalog.scss']
})
export class CatalogComponent implements OnInit, OnChanges {

  @Input() searchText = '';
  @Input() selectedGender = 0;
  @Input() selectedCategory = 0;

  products: Product[] = [];

  addedProductId: number | null = null;
  showAddedMessage = false;

  selectedProduct: Product | null = null;
  modalQuantity = 1;
  showModalAddedMessage = false;

  constructor(
    private productService: ProductService,
    private cdr: ChangeDetectorRef,
    private cartService: CartService,
    private zone: NgZone
  ) {}

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

  ngOnChanges() {}

  addToCart(product: Product) {
    this.cartService.addProduct(product);

    this.addedProductId = product.id;
    this.showAddedMessage = true;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.zone.run(() => {
        this.addedProductId = null;
        this.showAddedMessage = false;
        this.cdr.detectChanges();
      });
    }, 2000);
  }

  openProduct(product: Product) {
    this.selectedProduct = product;
    this.modalQuantity = 1;
    this.showModalAddedMessage = false;
  }

  closeProduct() {
    this.selectedProduct = null;
    this.modalQuantity = 1;
    this.showModalAddedMessage = false;
  }

  increaseQuantity() {
    this.modalQuantity++;
  }

  decreaseQuantity() {
    if (this.modalQuantity > 1) {
      this.modalQuantity--;
    }
  }

  addToCartFromModal(product: Product) {
    for (let i = 0; i < this.modalQuantity; i++) {
      this.cartService.addProduct(product);
    }

    this.showModalAddedMessage = true;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.zone.run(() => {
        this.showModalAddedMessage = false;
        this.cdr.detectChanges();
      });
    }, 2000);
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
}