import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './landing/landing.component';
import { ExplainerComponent } from './explainer/explainer.component';

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'explainer', component: ExplainerComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
