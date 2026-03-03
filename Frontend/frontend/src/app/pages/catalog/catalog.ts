import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

type Product = {
  id: number;
  name: string;
  price: number;
  imageUrl?: string;
  description?: string;
};

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog.html',
  styleUrl: './catalog.scss',
})
export class CatalogComponent {
  products: Product[] = [
    { id: 1, name: 'Jean clásico', price: 90000 },
    { id: 2, name: 'Blusa casual', price: 55000 },
    { id: 3, name: 'Camisa elegante', price: 75000 },
  ];
}