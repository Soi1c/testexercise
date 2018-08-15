import {Component, OnInit} from "@angular/core";
import {ApiService} from "../../../services/api.service";
import R from "ramda";

const VIEW_STATES = {
  LIST:"SHOW_LIST_OF_BOOKS",
  BOOK:"SHOW_BOOK"
};

@Component({
  selector:'app-approvedBooks',
  templateUrl: './approvedBooks.component.html',
  styleUrls:['./approvedBooks.component.scss']
})
export class ApprovedBooksComponent implements OnInit{
  constructor(private api: ApiService){
  }
  state;
  public table = {
    data: [],
    size: 0,
    page: 0,
    displayed: ['id', 'name','description']
  };

  errors = '';

  public isListShowable = () =>this.state === VIEW_STATES.LIST;
  public isBookShowable = () =>this.state === VIEW_STATES.BOOK;

  public getBookList = (filter?) =>{
    this.state = VIEW_STATES.LIST;
    this.api.user.getMySharedBooks()
      .subscribe(
        response=>{
          this.table.data = R.prop( response)
            ? R.map(
              (it) => ({
                ...it,
                name: it.name
              }),
              response)
            : this.table.data;
          this.table.size = R.propOr(this.table.size, 'totalElements', response);
          this.table.page = R.propOr(this.table.page, 'number', response);
          this.errors = '';
          if(!this.table.size){
            this.errors = 'У вас еще нет одобренных книг...'
          }
        },
        error => {}
      );
  }

  public showBook(){
    this.state = VIEW_STATES.BOOK;
  }

  public paginatorChanged = (data) =>
    this.getBookList({size: data.pageSize, page: data.pageIndex});

  public rowClicked = (data) => {
    this.showBook();
  }

  ngOnInit() {
    this.getBookList();

  }

}
