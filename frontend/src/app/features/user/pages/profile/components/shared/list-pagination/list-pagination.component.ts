import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgClass, NgIf } from '@angular/common';

@Component({
  selector: 'app-list-pagination',
  standalone: true,
  imports: [NgClass, NgIf],
  templateUrl: './list-pagination.component.html',
  styleUrls: []
})
export class ListPaginationComponent {
  @Input() currentCount: number = 0;
  @Input() totalCount: number = 0;
  @Input() minItems: number = 5;
  @Input() step: number = 5;

  @Output() onShowMore = new EventEmitter<void>();
  @Output() onShowLess = new EventEmitter<void>();

  get canShowMore(): boolean {
    return this.currentCount < this.totalCount;
  }

  get canShowLess(): boolean {
    return this.currentCount > this.minItems;
  }

  showMore(): void {
    if (this.canShowMore) {
      this.onShowMore.emit();
    }
  }

  showLess(): void {
    if (this.canShowLess) {
      this.onShowLess.emit();
    }
  }
}
