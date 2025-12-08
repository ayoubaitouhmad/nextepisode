import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {SearchBarComponent} from '../../../shared/components/search-bar/search-bar.component';
import {AuthService} from '../../services/auth/auth-service';
import {routes} from '../../../app.routes';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app-layout.component.html',
  styleUrl: './app-layout.component.scss'
})
export class AppLayoutComponent implements OnInit, OnDestroy {
  username = '';
  isAuthenticated: boolean = false;
  private sub!: Subscription;

  open = false;

  constructor(private auth: AuthService, private router: Router) {
    this.username = auth.getUser()?.username || '';
    this.isAuthenticated = auth.isAuthenticated();
  }

  ngOnInit() {
    this.sub = this.auth.loggedIn$.subscribe(v => this.isAuthenticated = v);
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  isDropdownOpen = false;


  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown() {
    this.isDropdownOpen = false;
  }

  // Close dropdown when clicking outside
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event) {
    const target = event.target as HTMLElement;
    if (!target.closest('.user-dropdown')) {
      this.isDropdownOpen = false;
    }
  }

  onProfile() {
    this.router.navigateByUrl('/profile');
    this.closeDropdown();
  }

  onLogout() {
    console.log('Logout user');
    this.auth.logout()
    this.closeDropdown();
    this.router.navigateByUrl('/auth/login');


  }

  onLogin() {
    this.router.navigateByUrl('/auth/login');
  }


  onSignup() {
    this.router.navigateByUrl('/auth/signup');
  }

}
