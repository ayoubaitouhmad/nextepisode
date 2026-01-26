import {Injectable, isDevMode} from '@angular/core';

export class Logger {
  constructor(private context: string) {
  }

  debug(message: string, ...data: any[]): void {
    if (isDevMode()) {
      console.debug(`[${this.context}] ${message}`, ...data);
    }
  }

  info(message: string, ...data: any[]): void {
    console.info(`[${this.context}] ${message}`, ...data);
  }

  warn(message: string, ...data: any[]): void {
    console.warn(`[${this.context}] ${message}`, ...data);
  }

  error(message: string, ...data: any[]): void {
    console.error(`[${this.context}] ${message}`, ...data);
  }
}

@Injectable({providedIn: 'root'})
export class LoggerService {
  create(context: string): Logger {
    return new Logger(context);
  }
}
