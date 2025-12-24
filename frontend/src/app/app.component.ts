import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {AlertContainerComponent} from './shared/components/alert/alertx.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, AlertContainerComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'movie-of-the-night';
}
