// import { Component } from '@angular/core';
// import { FormsModule } from '@angular/forms';
// import { HttpClientModule } from '@angular/common/http';
// import { CommonModule } from '@angular/common';  // ✅ Import CommonModule
// import { ApiService } from './api.service';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [FormsModule, HttpClientModule, CommonModule], // ✅ Add CommonModule here
//   templateUrl: './app.component.html',
//   styleUrls: ['./app.component.css']
// })
// export class AppComponent {
//   userInput: string = '';
//   inputType: string = 'code';
//   explanation: string = '';

//   constructor(private api: ApiService) {}

//  getExplanation() {
//   if (!this.userInput.trim()) {
//     alert('Please enter code or math problem!');
//     return;
//   }

//   this.api.explain(this.userInput, this.inputType).subscribe(
//     res => {
//       // Remove Markdown symbols: **, *, _, ` (bold, italic, inline code)
//       let cleanText = res.replace(/(\*\*|\*|_|`)/g, '');
      
//       // Optional: replace multiple newlines with a single newline for cleaner display
//       cleanText = cleanText.replace(/\n{2,}/g, '\n\n');

//       this.explanation = cleanText;
//     },
//     err => this.explanation = 'Error: ' + err.message
//   );
// }



// }
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router'; // ✅ import RouterModule

@Component({
  selector: 'app-root',
  standalone: true,           // ✅ important
  imports: [RouterModule],    // ✅ add RouterModule here
  template: `<router-outlet></router-outlet>`,
  styleUrls: ['./app.component.css']
})
export class AppComponent {}


