import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { importProvidersFrom } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { LandingComponent } from './app/landing/landing.component';
import { ExplainerComponent } from './app/explainer/explainer.component';

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(HttpClientModule, FormsModule),
    provideRouter([
      { path: '', component: LandingComponent },
      { path: 'explainer', component: ExplainerComponent }
    ])
  ]
}).catch(err => console.error(err));
