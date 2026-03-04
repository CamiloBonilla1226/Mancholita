import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/products.service';
import { Product } from '../../models/product';
import { CartService } from '../../services/cart.service'; // Importación correcta

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog.html',
  styleUrl: './catalog.scss'
})

export class CatalogComponent implements OnInit {

  products: Product[] = [];
  
  // Inyectamos ambos servicios en el constructor
  constructor(
    private productService: ProductService,
    private cartService: CartService
  ) {}
  
  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data: any) => {
        console.log('DATA BACKEND:', data);
        this.products = data.content;
      },
      error: (err) => {
        console.error('Error cargando productos', err);
      }
    });
  }

  // Método para agregar al carrito
  addToCart(product: Product): void {
    this.cartService.addProduct(product);
  }
}