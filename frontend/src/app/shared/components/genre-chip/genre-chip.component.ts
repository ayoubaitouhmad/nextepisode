import {Component, Input, Output, EventEmitter} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-genre-chip',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './genre-chip.component.html',
  styleUrl: './genre-chip.component.scss'
})
export class GenreChipComponent {
  @Input() genre!: string;
  @Input() selected = false;
  @Output() toggle = new EventEmitter<string>();

  onClick(): void {
    this.toggle.emit(this.genre);
  }
}
