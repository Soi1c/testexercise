import { Injectable } from '@angular/core';

@Injectable()
export class LocalStorageService {
  private step1;
  private step2;

  private getFormItem(name: string) {
    return this.getItem(`ospFormValue${name}`);
  }

  private setFormItem(name: string, value: any) {
    this.setItem(`ospFormValue${name}`, value);
  }

  public getItem(name: string) {
    return JSON.parse(localStorage.getItem(name));
  }

  public removeItem(name: string) {
    localStorage.removeItem(name);
  }

  public setItem(name: string, value: any) {
    localStorage.setItem(name, JSON.stringify(value));
  }

  public getFormStepData(step: number) {
    const data = {};
    for (const it of this[`step${step}`]) {
      data[it] = this.getFormItem(it);
    }
    return data;
  }

  public setFormStepData(step: number, value: Object) {
    this[`step${step}`] = Object.keys(value);
    for (const it of this[`step${step}`]) {
      this.setFormItem(it, value[it]);
    }
  }
}
