import {Component, Input, Output, EventEmitter} from '@angular/core';
import {CommonModule} from '@angular/common';

type ButtonVariant = 'primary' | 'secondary' | 'ghost';
type ButtonSize = 'small' | 'medium' | 'large';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './button.component.html',
  styleUrl: './button.component.scss'
})
export class ButtonComponent {
  @Input() variant: ButtonVariant = 'primary';
  @Input() size: ButtonSize = 'medium';
  @Input() disabled = false;
  @Input() fullWidth = false;
  @Output() clicked = new EventEmitter<void>();

  get classes(): string {
    const baseClasses = 'font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-neutral-900';

    const variantClasses = {
      primary: 'bg-amber-600 text-white hover:bg-amber-500 focus:ring-amber-500',
      secondary: 'bg-neutral-800 text-white hover:bg-neutral-700 focus:ring-neutral-600',
      ghost: 'bg-transparent text-white hover:bg-neutral-800 focus:ring-neutral-600'
    };

    const sizeClasses = {
      small: 'px-3 py-1 text-sm',
      medium: 'px-4 py-2',
      large: 'px-6 py-3 text-lg'
    };

    const widthClass = this.fullWidth ? 'w-full' : '';
    const disabledClass = this.disabled ? 'opacity-50 cursor-not-allowed' : '';

    return `${baseClasses} ${variantClasses[this.variant]} ${sizeClasses[this.size]} ${widthClass} ${disabledClass} rounded-full`;
  }

  onClick(): void {
    if (!this.disabled) {
      this.clicked.emit();
    }
  }
}
