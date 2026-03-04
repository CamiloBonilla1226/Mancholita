export interface Product {

  id: number;
  name: string;
  description: string;

  imageUrl: string;

  price: number;

  active: boolean;

  categoryId: number;
  categoryName: string;

  genderId: number;
  genderName: string;

}