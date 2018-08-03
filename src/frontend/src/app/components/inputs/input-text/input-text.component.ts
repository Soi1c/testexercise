import {Component, forwardRef, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChange} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {conformToMask} from 'angular2-text-mask';

declare const require: any;
const R = require('ramda');

@Component({
  selector: 'app-input-text',
  host: {'(blur)': 'onTouched($event)'},
  templateUrl: './input-text.component.html',
  styleUrls: ['./input-text.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputTextComponent),
      multi: true
    }
  ]
})
export class InputTextComponent implements OnInit {
  @Input() tooltip: boolean;
  @Input() tooltipPosition: string;
  @Input() tooltipContent: string;
  @Input() tooltipTheme: string;
  @Input() inputPlaceholder: string = '';
  @Input() inputType: string = 'text'; // password
  @Input() disabled: boolean = false;
  @Input() label: boolean;
  @Input() mask: any;
  @Input() tipText: string;
  @Input() tipIsError: boolean = false;
  @Input() placeholder: string = '';
  @Input() uppercase: boolean = false;
  @Input() blurred: boolean = false;
  @Output() blur: EventEmitter<any> = new EventEmitter();
  @Output() change: EventEmitter<any> = new EventEmitter();
  @Output() focus: EventEmitter<any> = new EventEmitter();
  @Output() keyup: EventEmitter<any> = new EventEmitter();

  private data;

  private isLabelPulled: boolean = false;

  public hideErrors: boolean = true;

  private propagateChange = (_: any) => {};

  public onTouched: any = () => { /*Empty*/ };

  public writeValue(value: any) {
    if (!R.isNil(value)) {
      this.data = value;
      this.triggerMask();
    }
    this.updateLabel();
  }

  triggerMask = () => {
    try {
      this.data = conformToMask(this.data, this.mask.mask, {guide: false}).conformedValue;
    } catch (e) { }
  }

  public registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  public registerOnChange(fn: any) {
    this.propagateChange = fn;
  }

  public setDisabledState(isDisabled: boolean) {
    this.disabled = isDisabled;
  }

  public isTooltiped = () => this.tooltip;

  private onChange = (e, input) => {
    this.data = e.target.value;
    this.updateLabel();

    if (this.data === '') {
      this.isLabelPulled = true;
    }

    this.change.emit(e.target.value);
    this.keyup.emit({event: e, data: input});

    this.propagateChange(this.data); // .replace(/[^0-9,]/g, '')
  }

  private onFocus = () => {
    this.focus.emit(this.data);
    this.isLabelPulled = true;
    this.hideErrors = true;
  }

  private onBlur = ($e) => {
    this.blur.emit($e);
    this.updateLabel();
    this.hideErrors = false;
  }

  private updateLabel() {
    if (this.label) {
      this.isLabelPulled = !!this.data;
    }
  }

  private setBlur() {
    this.hideErrors = false;
  }

  ngOnInit() {
  }
}
