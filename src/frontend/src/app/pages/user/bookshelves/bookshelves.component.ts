import {Component} from "@angular/core";
import R from "ramda";
import {ApiService} from "../../../services/api.service";

const VIEW_STATES = {
    LIST:"SHOW_LIST_OF_SHELFS",
    CREATION:"SHOW_CREATION",
    SINGLE: "SHOW_SINGLE_SHELF",
    BOOK:"SHOW_BOOK"
}

@Component({
  selector: 'app-bookshelves',
  templateUrl: './bookshelves.component.html',
  styleUrls:['./bookshelves.component.scss']
})
export class BookshelvesComponent {

  constructor(private api: ApiService){
    this.showList();
  }
  public state;
  public chosen;
  public table = {
    data: [],
    size: 0,
    page: 0,
    displayed: ['id', 'name', 'dateStart', 'booksCount']
  };


  public isListShowable = () =>this.state === VIEW_STATES.LIST;
  public isCreationShowable = () =>this.state === VIEW_STATES.CREATION;
  public isSingleShelfShowable = () =>this.state === VIEW_STATES.SINGLE;
  public isBookShowable = () =>this.state === VIEW_STATES.BOOK;

  public showList(){
    this.getList({page: 0, size: 10});
  }
  public showCreation(){
    this.state = VIEW_STATES.CREATION;
  }
  public showSingleShelf(){
    this.state = VIEW_STATES.SINGLE;
  }
  public showBook(){
    this.state = VIEW_STATES.BOOK;
  }

  private getList = (filter?) => {
    this.state = VIEW_STATES.LIST;
    this.api.bookshelves.getBookShelves(1)
      .subscribe(
        (response) => {
          this.table.data = R.prop('bookshelves', response)
            ? R.map(
              (it) => ({
                ...it,
                name: it.name
              }),
              response.bookshelves)
            : this.table.data;
          this.table.size = R.propOr(this.table.size, 'totalElements', response);
          this.table.page = R.propOr(this.table.page, 'number', response);
        });
  }

  public rowClicked = (data) => {
    this.chosen = data;
    this.showSingleShelf();
  }
  public paginatorChanged = (data) =>
    this.getList({size: data.pageSize, page: data.pageIndex});
}
