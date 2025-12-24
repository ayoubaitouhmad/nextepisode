import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Alert, AlertConfig} from './alert.model';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  private alertsSubject = new BehaviorSubject<Alert[]>([]);
  public alerts$: Observable<Alert[]> = this.alertsSubject.asObservable();

  private defaultConfig: Required<Omit<AlertConfig, 'title' | 'type'>> = {
    duration: 5000,
    dismissible: true,
    icon: true
  };

  constructor() {
  }

  /**
   * Show a success alert
   */
  success(message: string, config?: AlertConfig): string {
    return this.show(message, {...config, type: 'success'});
  }

  /**
   * Show an error alert
   */
  error(message: string, config?: AlertConfig): string {
    return this.show(message, {...config, type: 'error'});
  }

  /**
   * Show a warning alert
   */
  warning(message: string, config?: AlertConfig): string {
    return this.show(message, {...config, type: 'warning'});
  }

  /**
   * Show an info alert
   */
  info(message: string, config?: AlertConfig): string {
    return this.show(message, {...config, type: 'info'});
  }

  /**
   * Show a custom alert
   */
  show(message: string, config?: AlertConfig): string {
    const alert: Alert = {
      id: this.generateId(),
      type: config?.type || 'info',
      message,
      title: config?.title,
      duration: config?.duration !== undefined ? config.duration : this.defaultConfig.duration,
      dismissible: config?.dismissible !== undefined ? config.dismissible : this.defaultConfig.dismissible,
      icon: config?.icon !== undefined ? config.icon : this.defaultConfig.icon,
      isVisible: false
    };

    // Add alert to the list
    const currentAlerts = this.alertsSubject.value;
    this.alertsSubject.next([...currentAlerts, alert]);

    // Trigger visibility after a brief delay for CSS animation
    setTimeout(() => {
      this.setAlertVisibility(alert.id, true);
    }, 10);

    // Auto-dismiss if duration is set
    if (alert.duration && alert.duration > 0) {
      setTimeout(() => {
        this.dismiss(alert.id);
      }, alert.duration);
    }

    return alert.id;
  }

  /**
   * Dismiss a specific alert by ID
   */
  dismiss(id: string): void {
    // First, hide the alert (triggers CSS transition)
    this.setAlertVisibility(id, false);

    // Then remove it from the DOM after animation completes
    setTimeout(() => {
      const currentAlerts = this.alertsSubject.value;
      this.alertsSubject.next(currentAlerts.filter(alert => alert.id !== id));
    }, 300); // Match CSS transition duration
  }

  /**
   * Clear all alerts
   */
  clearAll(): void {
    const currentAlerts = this.alertsSubject.value;

    // Hide all alerts first
    currentAlerts.forEach(alert => {
      this.setAlertVisibility(alert.id, false);
    });

    // Remove all after animation
    setTimeout(() => {
      this.alertsSubject.next([]);
    }, 300);
  }

  /**
   * Update default configuration
   */
  setDefaultConfig(config: Partial<Omit<AlertConfig, 'type' | 'title'>>): void {
    this.defaultConfig = {...this.defaultConfig, ...config};
  }

  /**
   * Set alert visibility for CSS transitions
   */
  private setAlertVisibility(id: string, visible: boolean): void {
    const currentAlerts = this.alertsSubject.value;
    const updatedAlerts = currentAlerts.map(alert =>
      alert.id === id ? {...alert, isVisible: visible} : alert
    );
    this.alertsSubject.next(updatedAlerts);
  }

  /**
   * Generate a unique ID for each alert
   */
  private generateId(): string {
    return `alert-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }
}
