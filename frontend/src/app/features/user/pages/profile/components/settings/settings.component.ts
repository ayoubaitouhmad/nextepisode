import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PasswordChangeRequest } from '../../../../../core/services/user.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {
  @Input() loading = false;
  
  @Output() changePassword = new EventEmitter<PasswordChangeRequest>();
  @Output() deleteAccount = new EventEmitter<void>();

  isChangingPassword = false;
  errorMessage = '';

  passwordData: PasswordChangeRequest = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  startChangingPassword(): void {
    this.passwordData = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
    this.isChangingPassword = true;
    this.errorMessage = '';
  }

  savePassword(): void {
    if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      this.errorMessage = 'New password and confirmation password do not match.';
      return;
    }

    if (this.passwordData.newPassword.length < 6) {
      this.errorMessage = 'New password must be at least 6 characters long.';
      return;
    }

    this.errorMessage = '';
    this.changePassword.emit(this.passwordData);
  }

  cancelPasswordChange(): void {
    this.isChangingPassword = false;
    this.passwordData = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
    this.errorMessage = '';
  }

  onDeleteAccount(): void {
    if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
      this.deleteAccount.emit();
    }
  }

  // Called by parent when password change is successful
  onPasswordChangeSuccess(): void {
    this.isChangingPassword = false;
    this.passwordData = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  }
}
