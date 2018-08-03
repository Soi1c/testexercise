import { Pipe, PipeTransform } from '@angular/core';
import { HEADERS_LOCALISATIONS } from './localisations-list';

@Pipe({ name: 'tableHeaderRuLocalization' })
export class TableHeaderLocalization implements PipeTransform {
    transform(value: string) {
        return HEADERS_LOCALISATIONS[value] || '';
    }
}
