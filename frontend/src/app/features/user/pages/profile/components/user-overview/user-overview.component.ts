import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {UserProfile} from '../../../../../../core/services/user.service';
import {MovieStatistics} from '../../../../../../core/services/user-movie.service';


@Component({
  selector: 'app-user-overview',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './user-overview.component.html',
  styleUrls: ['./user-overview.component.scss']
})
export class UserOverviewComponent {
  @Input() currentUser: UserProfile | null = null;
  @Input() editedProfile: Partial<UserProfile> = {};
  @Input() movieStatistics: MovieStatistics = { favoriteCount:0,watchedCount:0,watchlistCount:0};
  @Input() isEditingProfile = false;
  @Input() loading = false;

  @Output() editedProfileChange = new EventEmitter<Partial<UserProfile>>();
  @Output() startEditing = new EventEmitter<void>();
  @Output() saveProfile = new EventEmitter<void>();
  @Output() cancelEditing = new EventEmitter<void>();

  getFullName(): string {
    if (!this.currentUser) return '';
    if (!this.currentUser.isDirty) return this.currentUser.username;
    return `${this.currentUser.firstName} ${this.currentUser.lastName}`.trim();
  }

  getFormattedDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  onStartEditing(): void {
    this.startEditing.emit();
  }

  onSaveProfile(): void {
    this.saveProfile.emit();
  }

  onCancelEditing(): void {
    this.cancelEditing.emit();
  }

  updateEditedProfile(): void {
    this.editedProfileChange.emit(this.editedProfile);
  }
}
