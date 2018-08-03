import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InputTextErrorsComponent } from './input-text-errors.component';

describe('InputTextErrorsComponent', () => {
  let component: InputTextErrorsComponent;
  let fixture: ComponentFixture<InputTextErrorsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InputTextErrorsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InputTextErrorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
