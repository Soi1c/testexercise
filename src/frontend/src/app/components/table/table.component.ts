import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable} from 'rxjs/Rx';

import R from 'ramda';

@Component({
    selector: 'app-table',
    templateUrl: 'table.component.html',
    styleUrls: ['./table.component.scss']
})
export class TableWrapper implements OnChanges {

    @Input() displayed; // columns list to be displayed
    @Input() data; // data
    @Input() size; // total data count
    @Input() page;// current page
    @Output() paginatorchanged: EventEmitter<any> = new EventEmitter();
    @Output() rowclicked: EventEmitter<any> = new EventEmitter();

    public displayedColumns: String[];
    public dataSource: TableDataSource;
    public tableDataProvider;

    ngOnChanges(changes: SimpleChanges) {
        if (R.path(['data', 'currentValue'], changes)) {
            this.tableDataProvider = new TableDataProvider(this.data);
            this.dataSource = new TableDataSource(this.tableDataProvider);
        }
        if (R.path(['displayed', 'currentValue'], changes)) {
            this.displayedColumns = this.displayed;
        }
    }

    onPaginatorChanged(paginatorData) {
        this.paginatorchanged.emit(paginatorData);
    }

    onRowClicked(row) {
        this.rowclicked.emit(row);
    }
}

class TableDataProvider {
    dataChange: BehaviorSubject<any[]> = new BehaviorSubject<any[]>([]);
    get data(): any[] { return this.dataChange.value; }

    constructor(data: any[]) {
        this.dataChange.next(data);
    }
}

class TableDataSource extends DataSource<any> {
    constructor(private dataProvider: TableDataProvider) {
        super();
    }

    connect(): Observable<any> {
        const displayDataChanges = [
            this.dataProvider.dataChange
        ];

        return Observable.merge(...displayDataChanges).map(() => {
            const data = this.dataProvider.data.slice();
            return data;
        });
    }

    disconnect() {}
}
