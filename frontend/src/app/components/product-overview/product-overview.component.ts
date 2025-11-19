import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ProductService } from '../../services/product.service';
import { DataProviderService } from '../../services/data-provider.service';
import { Product, DataProviderProduct } from '../../models/product.model';

@Component({
  selector: 'app-product-overview',
  standalone: true,
  imports: [
    CommonModule,
    MatTabsModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatFormFieldModule
  ],
  templateUrl: './product-overview.component.html',
  styleUrls: ['./product-overview.component.css']
})
export class ProductOverviewComponent implements OnInit {
  internalProducts: Product[] = [];
  dataProviderProducts: DataProviderProduct[] = [];

  internalProductColumns = ['internalId', 'globalTradeIdentifier', 'attributes'];
  dataProviderProductColumns = ['dataProviderId', 'externalId', 'globalTradeIdentifier', 'associationStrategy', 'attributes', 'associatedProduct'];

  loading = false;
  importing = false;

  // Product comparison tab
  selectedProduct: Product | null = null;
  associatedDataProviderProducts: DataProviderProduct[] = [];

  constructor(
    private productService: ProductService,
    private dataProviderService: DataProviderService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.loadInternalProducts();
    this.loadDataProviderProducts();
  }

  loadInternalProducts() {
    this.loading = true;
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.internalProducts = products;
        this.loading = false;
      },
      error: (error) => {
        this.snackBar.open('Error loading internal products: ' + error.message, 'Close', { duration: 5000 });
        this.loading = false;
      }
    });
  }

  loadDataProviderProducts() {
    this.loading = true;
    this.dataProviderService.getAllDataProviderProducts().subscribe({
      next: (products) => {
        this.dataProviderProducts = products;
        this.loading = false;
      },
      error: (error) => {
        this.snackBar.open('Error loading data provider products: ' + error.message, 'Close', { duration: 5000 });
        this.loading = false;
      }
    });
  }

  runJsonImport() {
    this.importing = true;
    this.dataProviderService.importJsonProducts().subscribe({
      next: (result) => {
        this.importing = false;
        const message = `Import completed! Total: ${result.totalProducts}, Associated: ${result.associatedProducts}, Not Associated: ${result.notAssociatedProducts}`;
        this.snackBar.open(message, 'Close', { duration: 7000 });
        
        // Reload both tables
        this.loadInternalProducts();
        this.loadDataProviderProducts();
      },
      error: (error) => {
        this.importing = false;
        this.snackBar.open('Error importing products: ' + error.message, 'Close', { duration: 5000 });
      }
    });
  }

  formatAttributes(attributes: any[] | undefined): string {
    if (!attributes || attributes.length === 0) {
      return 'None';
    }
    return attributes.map(attr => `${attr.name}: ${attr.value}`).join(', ');
  }

  getAssociatedProductId(product: DataProviderProduct): string {
    return product.associatedProduct?.internalId || 'Not Associated';
  }

  onProductSelected(product: Product) {
    this.selectedProduct = product;
    this.loadAssociatedDataProviderProducts();
  }

  loadAssociatedDataProviderProducts() {
    if (!this.selectedProduct) {
      this.associatedDataProviderProducts = [];
      return;
    }

    // Filter data provider products that are associated with the selected internal product
    this.associatedDataProviderProducts = this.dataProviderProducts.filter(
      dp => dp.associatedProduct?.internalId === this.selectedProduct?.internalId
    );
  }
}

