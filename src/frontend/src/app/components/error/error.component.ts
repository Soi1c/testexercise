import {Component, Input, OnInit, SimpleChanges, OnChanges} from '@angular/core';
import {UtilityService} from '../../services/utility';

import R from 'ramda';

@Component({
    selector: 'app-error',
    template: '<span *ngIf="errorText">{{errorText}}</span>',
    styles: [
            `span {
            color: #F44336;
            display: block;
            margin: 3px 0;
        }
        `
    ]
})
export class ErrorComponent implements OnChanges {
    @Input() error;

    public errorText: string;

    ngOnChanges(changes: SimpleChanges) {
        const errorDescription = R.pathOr(this.error, ['error', 'currentValue', 'body', 'error_description'], changes);
        this.setErrorText(errorDescription);
    }

    setErrorText(description = '') {
        this.errorText = UtilityService.returnErrorMessage(description);
    }
}
