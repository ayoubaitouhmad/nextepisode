import {Component, Input, Output, EventEmitter} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-autocomplete',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="relative w-full max-w-md">
      <label for="casts">Acteurs & Équipe</label>
      <input
        type="text"
        name="casts"
        [(ngModel)]="query"
        (input)="onInput()"
        (focus)="showSuggestions = true"
        (blur)="onBlur()"
        placeholder="Search..."
        class="w-full bg-gray-600 rounded p-2"
      />

      <ul *ngIf="true"
          class="absolute z-10 mt-1 w-full max-h-48 overflow-y-auto bg-gray-700 rounded shadow-lg">
        <li *ngFor="let item of items"
            (mousedown)="select(item)"
            class="px-3 py-2  hover:bg-gray-500 cursor-pointer bg-gray-600 underline">
          {{ item }}
        </li>
      </ul>
    </div>
  `
})
export class AutocompleteComponent {
  /**
   * Array of suggestion strings to filter
   */
  @Input() items: string[] = [
    "ayoub", "mrx"
  ];

  /**
   * Emits the selected item when the user clicks a suggestion
   */
  @Output() selected = new EventEmitter<string>();

  @Output() queryChange = new EventEmitter<string>();


  query = '';
  filteredItems: string[] = [];
  showSuggestions = false;

  onInput(): void {
    const q = this.query.trim().toLowerCase();
    this.filteredItems = q ? this.items.filter(item => item.toLowerCase().startsWith(q))
      : [];
    this.queryChange.emit(this.query);

  }

  select(item: string): void {
    this.query = item;
    this.filteredItems = [];
    this.showSuggestions = false;
    this.selected.emit(item);
  }

  onBlur(): void {
    // Delay hiding to allow click handlers to fire
    setTimeout(() => this.showSuggestions = false, 100);
  }
}
