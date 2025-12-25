import {Component, Input, Output, EventEmitter, OnChanges, SimpleChanges} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {GenreList, Person, PersonList} from '../../../core/models/common/shared-dtos';

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

      <ul *ngIf="showSuggestions"
          class="absolute z-10 mt-1 w-full max-h-48 overflow-y-auto bg-gray-700 rounded shadow-lg">
        <li *ngFor="let person of personList.results"
            (mousedown)="select(person)"
            class="px-3 py-2  hover:bg-gray-500 cursor-pointer bg-gray-600 underline line-clamp-1">
          <div class="flex align-middle items-center  -space-x-2 overflow-hidden ">
            <img
              [src]="getProfilePhoto(person.profile_path)"

              alt=" {{ person.name }}"
              class="me-1 inline-block size-10 rounded-full   outline "/>
            {{ person.name }}
          </div>
        </li>
      </ul>
    </div>
  `
})
export class AutocompleteComponent implements OnChanges {
  ngOnChanges(changes: SimpleChanges): void {
  }

  /**
   * Array of suggestion strings to filter
   */
  @Input() personList: PersonList = {
    page: 0,
    total_pages: 0,
    total_results: 0,
    results: []
  }

  /**
   * Emits the selected item when the user clicks a suggestion
   */
  @Output() selected = new EventEmitter<Person>();

  @Output() queryChange = new EventEmitter<string>();


  query = '';
  filteredItems: string[] = [];
  showSuggestions = false;

  onInput(): void {
    const q = this.query.trim().toLowerCase();
    this.filteredItems = q ? this.personList.results.map(value => value.name).filter(item => item.toLowerCase().startsWith(q))
      : [];
    this.queryChange.emit(this.query);

  }

  select(person: Person): void {
    this.query = person.name;
    this.filteredItems = [];
    this.showSuggestions = false;
    this.selected.emit(person);
  }

  onBlur(): void {
    // Delay hiding to allow click handlers to fire
    setTimeout(() => this.showSuggestions = false, 100);
  }

  getProfilePhoto(path: string) {
    if (!path) {
      return "/images/blank.png"
    }
    return path;
  }

}
