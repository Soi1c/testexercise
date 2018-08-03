import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {

  @Input() type = 'button';
  @Input() disabled = false;
  @Input() submit = false;
  @Input() primary = true;
  @Input() icon = false;
  @Input() borderless = false;
  @Input() iconOnly = false;
  @Input() loading = false;
  @Input() loadingText = 'Отправка данных';
  @Output() clicked: EventEmitter<any> = new EventEmitter();

  onClick(e: Event) {
    if (!this.disabled) {
      this.clicked.emit();
    } else {
      e.preventDefault();
      e.stopImmediatePropagation();
      e.stopPropagation();
      return false;
    }
  }

  ngOnInit() {
  }

}
