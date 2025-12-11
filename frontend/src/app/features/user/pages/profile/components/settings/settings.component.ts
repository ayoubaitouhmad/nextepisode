import {Component, Input, Output, EventEmitter} from '@angular/core';
import {NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {PasswordChangeRequest} from '../../../../../../core/models/auth/auth.model';
import {ProfilePrivacySettingsUpdateRequest} from '../../../../../../core/models/user/user.model';


@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {
  @Input() isChangingPassword = false;
  @Input() passwordData: PasswordChangeRequest = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };
  @Input() loading = false;

  @Output() startChangingPassword = new EventEmitter<void>();
  @Output() savePassword = new EventEmitter<void>();
  @Output() cancelPasswordChange = new EventEmitter<void>();
  @Output() deleteAccount = new EventEmitter<void>();
  @Output() passwordDataChange = new EventEmitter<PasswordChangeRequest>();
  @Output() savePrivacySettings = new EventEmitter<void>();


  @Input() privacySettingsData: ProfilePrivacySettingsUpdateRequest = {
    profileVisibility: false,
    activitySharing: false
  };

  onStartChangingPassword(): void {
    this.startChangingPassword.emit();
  }

  onSavePassword(): void {
    this.passwordDataChange.emit(this.passwordData);
    this.savePassword.emit();
  }

  onCancelPasswordChange(): void {
    this.cancelPasswordChange.emit();
  }

  onDeleteAccount(): void {
    this.deleteAccount.emit();
  }

  onPasswordDataChange(): void {
    this.passwordDataChange.emit(this.passwordData);
  }

  onSavePrivacySettings(){
    this.savePrivacySettings.emit();
  }
}
