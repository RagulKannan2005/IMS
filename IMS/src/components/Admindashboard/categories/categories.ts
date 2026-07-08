import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { categoryservice } from '../../../app/services/category';
@Component({
  selector: 'app-categories',
  imports: [CommonModule, FormsModule],
  templateUrl: './categories.html',
  styleUrl: './categories.css',
})
export class Categories {
  categories: any[] = [];
  isLoading = true;
  errormessage = '';

  // categoryService=inject(CategoryService);
  private categoryService = inject(categoryservice);
  cdr = inject(ChangeDetectorRef);

  searchCategory(event: any) {
    const name = event.target.value;

    if (!name.trim()) {
      this.loadCategories();
      return;
    }
    this.categoryService.getCategoryByName(name).subscribe({
      next:(response:any)=>{
        this.categories=response;
        this.cdr.detectChanges();
      },
      error:(err)=>{
        console.log('search failed',err);
      }
    });
  }

  showform= false;
  openform(){
    this.showform=true;
  }
  closeform(){
    this.showform=false;
  }

  ngOnInit() {
    this.loadCategories();
  }

  newCategory:any={};
  addCategory(){

    console.log('newCategory',this.newCategory);
    this.categoryService.addcategory(this.newCategory).subscribe({
      next:(response:any)=>{
        console.log('Category added',response);
        this.closeform();
        this.loadCategories();
        this.cdr.detectChanges();
      },
      error:(err)=>{
        console.log('Failed to add category',err);
        this.errormessage=err.error.message || 'Failed to add category';
        this.cdr.detectChanges();
      }
    });
    
  }

  loadCategories() {
    this.isLoading = true;
    this.categoryService.getAllCategories().subscribe({
      next: (response: any) => {
        console.log('API Request succeed ', response);
        try {
          if (response && response.data && Array.isArray(response.data)) {
            this.categories = response.data;
            console.log('Categories loaded', this.categories.length);
          } else if (response && response.value && Array.isArray(response.value)) {
            this.categories = response.value;
          } else if (Array.isArray(response)) {
            this.categories = response;
          } else {
            console.error('unexpected response format: ' + JSON.stringify(response));
            this.errormessage = 'Unexpected response format';
            this.categories = [];
          }
          this.isLoading = false;
          this.cdr.detectChanges();
        } catch (error) {
          console.error('Error processing response:', error);
          this.errormessage = 'Error processing data';
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        console.error('Failed to load categories', error);
        this.errormessage = 'Failed to load categories';
        this.isLoading = false;
        this.cdr.detectChanges();
      },
    });
  }
}
