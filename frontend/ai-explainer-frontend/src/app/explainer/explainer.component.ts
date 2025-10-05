import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-explainer',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './explainer.component.html',
  styleUrls: ['./explainer.component.css']
})
export class ExplainerComponent {
  userInput: string = '';
  inputType: string = 'code';  // default
  explanation: string = '';
  dropdownOpen: boolean = false;  // controls open/close

  constructor(private api: ApiService) {}

  toggleDropdown() {
  this.dropdownOpen = !this.dropdownOpen;
}

selectOption(value: string, event: Event) {
  event.stopPropagation(); // ✅ prevents parent toggle from firing
  this.inputType = value;
  this.dropdownOpen = false;
}


  showAlert: boolean = false;  // flag for alert box

getExplanation() {
  if (!this.userInput.trim()) {
    this.showAlert = true;   // show alert box
    return;
  }

  this.api.explain(this.userInput, this.inputType).subscribe(
    res => {
      let cleanText = res.replace(/(\\|\*|_|`)/g, '');
      cleanText = cleanText.replace(/\n{2,}/g, '\n\n');
      this.explanation = cleanText;
    },
    err => this.explanation = 'Error: ' + err.message
  );
}

closeAlert() {
  this.showAlert = false;   // hide alert box
}

}