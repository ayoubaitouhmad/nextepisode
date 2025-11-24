import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {AuthService, SignupRequest} from '../../../../core/services/auth-service';
import {DomSanitizer, SafeHtml} from '@angular/platform-browser';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="flex items-center justify-center">
      <div class="w-full max-w-md space-y-6 p-8 rounded-lg">
        <h1 class="text-center text-white text-3xl font-bold">Inscription</h1>

        <h1>
          {{ errorMsg }}
        </h1>
        <h1 [innerHTML]="dynamicHtmlContent">

        </h1>

        <div class="relative flex items-center">
          <span class="flex-grow border-t border-gray-700"></span>
          <span class="px-4 text-gray-400">ou</span>
          <span class="flex-grow border-t border-gray-700"></span>
        </div>

        <form (ngSubmit)="signup()" class="space-y-4">

          <div>
            <label class="block text-gray-300 mb-1">Username</label>
            <input
              type="text"
              [(ngModel)]="username"
              name="lastName"
              required
              class="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <span *ngIf="getFieldError('username')" class="mt-1 text-sm text-red-400 block">
              {{ getFieldError('username') }}
            </span>

          </div>
          <div>
            <label class="block text-gray-300 mb-1">Adresse e-mail</label>
            <input
              type="email"
              [(ngModel)]="email"
              name="email"
              required
              class="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <span *ngIf="getFieldError('email')" class="mt-1 text-sm text-red-400 block">
              {{ getFieldError('email') }}
            </span>

          </div>
          <div>
            <label class="block text-gray-300 mb-1">Mot de passe</label>
            <input
              type="password"
              [(ngModel)]="password"
              name="password"
              required
              class="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <span *ngIf="getFieldError('password')" class="mt-1 text-sm text-red-400 block">
              {{ getFieldError('password') }}
            </span>

          </div>

          <button
            type="submit"
            class="w-full bg-blue-600 text-white rounded px-4 py-2 hover:bg-blue-700 transition"
          >
            S’inscrire
          </button>
        </form>

        <div class="text-center">
          <a routerLink="/forgot-password" class="text-gray-400 hover:underline">Mot de passe oublié ?</a>
        </div>
        <div class="text-center text-gray-400">
          Vous avez déjà un compte ? <a routerLink="/auth/login" class="underline">
          Connexion
        </a>
        </div>
      </div>
    </div>

  `
})
export class SignupComponent {

  dynamicHtmlContent: SafeHtml;
  username = 'nuvykuga';
  email = 'nuvykugmailinator.com';
  password = 'nuvykuga@mailinator.com';
  errorMsg: string | null = null;
  fieldErrors: FieldErrors = {};
  loading = false;


  constructor(
    private auth: AuthService,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {
    this.dynamicHtmlContent = this.sanitizer.bypassSecurityTrustHtml("");

    if (auth.isAuthenticated()) {
      this.router.navigateByUrl('/');
    }
  }


  getFieldError(fieldName: string): string | null {
    return this.fieldErrors[fieldName] || null;
  }


  signup() {
    this.errorMsg = null;
    this.loading = true;
    this.fieldErrors = {};


    const req: SignupRequest = {
      username: this.username,
      email: this.email,
      password: this.password
    };

    // console.log(req)
    this.auth.register(req).subscribe({
      next: user => {
        this.loading = false;
        this.router.navigateByUrl('/auth/login');
      },
      error: err => this.handleError(err)
    });
  }


  handleError(err: any) {
    this.loading = false;
    this.errorMsg = err.error.message;
    if (err.error.code = "VAL_001") {
      this.fieldErrors = err.error.fieldErrors;
    }
  }

}

interface FieldErrors {
  [key: string]: string;
}
