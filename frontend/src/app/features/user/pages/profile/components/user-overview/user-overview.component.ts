import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';

import {UserMoviesAndTvShowStats} from '../../../../../../core/models/common/shared-dtos';
import {UserProfile} from '../../../../../../core/models/user/user.model';

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
  @Input() userMoviesAndTvShowStats: UserMoviesAndTvShowStats = {
    favoriteCount: 0,
    watchedCount: 0,
    watchlistCount: 0
  };
  @Input() isEditingProfile = false;
  @Input() loading = false;

  @Output() saveProfile = new EventEmitter<void>();
  @Output() cancelEditing = new EventEmitter<void>();
  @Output() startEditing = new EventEmitter<void>();
  @Output() editedProfileChange = new EventEmitter<Partial<UserProfile>>();

  getFullName(): string {
    if (!this.currentUser) return '';
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
    this.editedProfileChange.emit(this.editedProfile);
    this.saveProfile.emit();
  }

  onCancelEditing(): void {
    this.cancelEditing.emit();
  }

  onProfileChange(): void {
    this.editedProfileChange.emit(this.editedProfile);
  }
}
