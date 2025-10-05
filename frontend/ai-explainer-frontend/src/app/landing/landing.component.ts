import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  template: `
    <div style="text-align:center; margin-top:50px">
      <h1>Welcome to AI Code & Math Explainer</h1>
      <button (click)="goToExplainer()">Go to Explainer</button>
    </div>
  `,
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  constructor(private router: Router) {}

  goToExplainer() {
    this.router.navigate(['/explainer']);
  }
}
