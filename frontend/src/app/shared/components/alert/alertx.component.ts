import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Subscription} from 'rxjs';
import {AlertService} from './alert.service';
import {Alert} from './alert.model';

@Component({
  selector: 'app-alert-container',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed top-4 right-4 z-[9999] space-y-3 max-w-md w-full pointer-events-none px-4">
      <div
        *ngFor="let alert of alerts; trackBy: trackByFn"
        [class.alert-enter]="alert.isVisible"
        [class.alert-exit]="!alert.isVisible"
        class="alert-base pointer-events-auto cursor-default"
      >
        <!-- Alert Box -->
        <div
          [ngClass]="getAlertClasses(alert.type)"
          class="rounded-lg shadow-lg p-4 flex items-start relative overflow-hidden pointer-events-auto"
          role="alert"
        >
          <!-- Progress Bar (for auto-dismiss) -->
          <div
            *ngIf="alert.duration && alert.duration > 0"
            class="alert-progress"
            [style.animation-duration.ms]="alert.duration"
          ></div>

          <!-- Icon -->
          <div *ngIf="alert.icon" class="flex-shrink-0">
            <svg
              *ngIf="alert.type === 'success'"
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
              ></path>
            </svg>

            <svg
              *ngIf="alert.type === 'error'"
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
              ></path>
            </svg>

            <svg
              *ngIf="alert.type === 'warning'"
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
              ></path>
            </svg>

            <svg
              *ngIf="alert.type === 'info'"
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
              ></path>
            </svg>
          </div>

          <!-- Content -->
          <div class="flex-1 min-w-0">
            <h3 *ngIf="alert.title" class="text-sm font-semibold mb-1">
              {{ alert.title }}
            </h3>
            <p class="text-sm" [class.font-medium]="!alert.title">
              {{ alert.message }}
            </p>
          </div>

          <!-- Close Button -->
          <button
            *ngIf="alert.dismissible"
            type="button"
            (click)="dismiss(alert.id)"
            class="flex-shrink-0 rounded-lg p-1 hover:bg-black hover:bg-opacity-10 transition-colors focus:outline-none focus:ring-2 focus:ring-current focus:ring-opacity-50"
            [attr.aria-label]="'Close alert'"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              ></path>
            </svg>
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    /* Base alert styles */
    .alert-base {
      transform: translateX(100%);
      opacity: 0;
      transition: transform 300ms ease-out, opacity 300ms ease-out;
    }

    /* Enter animation */
    .alert-enter {
      transform: translateX(0);
      opacity: 1;
    }

    /* Exit animation */
    .alert-exit {
      transform: translateX(100%);
      opacity: 0;
      transition: transform 200ms ease-in, opacity 200ms ease-in;
    }

    /* Progress bar animation */
    .alert-progress {
      position: absolute;
      bottom: 0;
      left: 0;
      height: 4px;
      width: 100%;
      background-color: currentColor;
      opacity: 0.3;
      animation: shrink linear;
    }

    @keyframes shrink {
      from {
        width: 100%;
      }
      to {
        width: 0%;
      }
    }

    :host {
      display: block;
    }
  `]
})
export class AlertContainerComponent implements OnInit, OnDestroy {
  alerts: Alert[] = [];
  private subscription?: Subscription;

  constructor(private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.subscription = this.alertService.alerts$.subscribe(alerts => {
      this.alerts = alerts;
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  dismiss(id: string): void {
    this.alertService.dismiss(id);
  }

  getAlertClasses(type: string): string {
    const baseClasses = 'border-l-4';

    switch (type) {
      case 'success':
        return `${baseClasses} bg-green-50 border-green-500 text-green-900`;
      case 'error':
        return `${baseClasses} bg-red-50 border-red-500 text-red-900`;
      case 'warning':
        return `${baseClasses} bg-yellow-50 border-yellow-500 text-yellow-900`;
      case 'info':
        return `${baseClasses} bg-blue-50 border-blue-500 text-blue-900`;
      default:
        return `${baseClasses} bg-gray-50 border-gray-500 text-gray-900`;
    }
  }

  trackByFn(index: number, alert: Alert): string {
    return alert.id;
  }
}
