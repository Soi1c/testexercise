import {MdPaginatorIntl} from "@angular/material";

export class AppCustomPaginatorIntl extends MdPaginatorIntl {

    itemsPerPageLabel: string = 'Записей на странице';
    nextPageLabel: string = 'Следующая страница';
    previousPageLabel: string = 'Предыдушая страница';

    getRangeLabel = (page: number, pageSize: number, length: number) => {
        if (length == 0 || pageSize == 0) {
            return `0 из ${length}`;
        }

        length = Math.max(length, 0);
        const startIndex = page * pageSize,
              endIndex = startIndex < length
                  ? Math.min(startIndex + pageSize, length)
                  : startIndex + pageSize;

        return `${startIndex + 1} - ${endIndex} из ${length}`;
    }
}
