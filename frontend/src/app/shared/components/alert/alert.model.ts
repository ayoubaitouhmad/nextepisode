export interface Alert {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  title?: string;
  duration?: number;
  dismissible?: boolean;
  icon?: boolean;
  isVisible?: boolean;
}

export interface AlertConfig {
  type?: 'success' | 'error' | 'warning' | 'info';
  title?: string;
  duration?: number;
  dismissible?: boolean;
  icon?: boolean;
}
