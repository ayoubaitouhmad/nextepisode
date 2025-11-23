import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterModule, Router} from '@angular/router';

import {AuthService, LoginRequest} from '../../../../core/services/auth-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="flex items-center justify-center  ">
      <div class="w-full max-w-md space-y-6  p-8 rounded-lg ">
        <h1 class="text-center text-white text-3xl font-bold">Log In</h1>

        <div class="relative flex items-center">
          <span class="flex-grow border-t border-gray-700"></span>
          <span class="px-4 text-gray-400">ou</span>
          <span class="flex-grow border-t border-gray-700"></span>
        </div>

        <div *ngIf="errorMsg"
             class="p-4 mb-4 text-sm text-red-800 rounded-lg bg-red-50 dark:bg-gray-800 dark:text-red-400" role="alert">
  <span class="font-medium">
    {{ errorMsg }}
  </span>
        </div>

        <form (ngSubmit)="login()" class="space-y-4">
          <div>
            <label class="block text-gray-300 mb-1">username</label>
            <input
              type="email"
              [(ngModel)]="username"
              name="email"
              required
              class="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
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
          </div>
          <button
            type="submit"
            class="w-full bg-blue-600 text-white rounded px-4 py-2 hover:bg-blue-700 transition"
          >
            Se connecter
          </button>
        </form>

        <div class="text-center">
          <a routerLink="/forgot-password" class="text-gray-400 hover:underline">Mot de passe oublié ?</a>
        </div>
        <div class="text-center text-gray-400">
          Vous n’avez pas de compte ? <a routerLink="/auth/signup" class="underline">Inscrivez-vous ici.</a>
        </div>

      </div>
    </div>
  `

})
export class LoginComponent {
  username = 'aa';
  password = '12345';
  errorMsg: string | null = null;
  loading = false;

  constructor(
    private auth: AuthService,
    private router: Router
  ) {
    if (auth.isAuthenticated()) {
      this.router.navigateByUrl('/');
    }
  }


  login() {
    this.errorMsg = null;
    this.loading = true;

    const req: LoginRequest = {username: this.username, password: this.password};


    this.auth.login(req).subscribe({
      next: res => {
        console.log(res)
        this.loading = false;
        // res.user contains id, firstName, lastName
        this.router.navigateByUrl('/');
      },
      error: err => {
        console.log(err)
        // this.loading = false;
        this.errorMsg = err.error?.message || 'Invalid credentials';
      }
    });
  }
}
