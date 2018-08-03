import { Component, OnInit, Input } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-input-text-errors',
  templateUrl: './input-text-errors.component.html',
  styleUrls: ['./input-text-errors.component.scss']
})
export class InputTextErrorsComponent {
  @Input() control: FormControl; // контрол из которого выводятся ошибки
}
