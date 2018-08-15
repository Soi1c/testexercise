import {Component, OnInit} from "@angular/core";
import {ApiService} from "../../../services/api.service";
import {FormControl, FormGroup} from "@angular/forms";


@Component({
  selector:'app-usersBookshare',
  templateUrl: './userBookshare.component.html',
  styleUrls:['./userBookshare.component.scss']
})
export class UserBookshareComponent implements OnInit{

  constructor(private api: ApiService){
  }

  readonly STEPS = {
    USERS:"SHOW_USER_LIST",
    BOOKSHALVES:"SHOW_BOOKSHALVES",
    BOOKS:"SHOW_BOOKS",
    SEND_REQUEST:"SEND_REQUEST"
  };

  state;
  errors = '';
  private userId;
  private shelfId;
  private bookId;

  userList :any[];
  bookshelfList:any[];
  booksList:any[];

  book = {
    name: '',
    description: ''
  };

  public form: FormGroup;

  public user;

  public isUsersShowable = () =>this.state === this.STEPS.USERS;
  public isBookshelvesShowable = () =>this.state === this.STEPS.BOOKSHALVES;
  public isBookShowable = () =>this.state === this.STEPS.BOOKS;
  public isSendRequestShowable = () =>this.state === this.STEPS.SEND_REQUEST;

  public showUserList(){
    this.state = this.STEPS.USERS;
    this.api.user.getAllUsers()
      .subscribe(
        response => {
          this.userList = response.map(user => {
              return {
                name: user.email,
                value: user.id
              }
            }
          );
          this.errors = '';
          if(this.userList.length<1){
            this.errors = 'Список пользователей пуст';
          }
        }
      );
  }

  public showBookshelfList(userId){
    this.state = this.STEPS.BOOKSHALVES;
    this.api.user.getUserBookshelves(userId)
      .subscribe(
        response => {
          this.bookshelfList = response.map(shelf => {
              return {
                name: shelf.name,
                value: shelf.id
              }
            }
          );
          this.errors = '';
          if(this.bookshelfList.length<1){
            this.errors = 'У данного пользователя нет полок';
          }
        }
      );
  }

  public showBooksList(userId, shelfId){
    this.state = this.STEPS.BOOKS;
    this.api.user.getAllBooksFromAnotherUserBookshelf(userId,shelfId)
      .subscribe(
        response => {
          this.booksList = response.map(book => {
              return {
                name: book.name,
                value: book.id
              }
            }
          );
          this.errors = '';
          if(this.booksList.length<1){
            this.errors = 'Книжная полка пустая, выберите другую...';
          }
        }
      );
  }

  public showBook(){
    this.state = this.STEPS.SEND_REQUEST;
    this.api.user.getBookFromAnotherUserBookshelf(this.userId,this.shelfId, this.bookId)
      .subscribe(
        response =>{
          this.form.reset();
          this.form.controls.name.patchValue(response.name);
          this.form.controls.description.patchValue(response.description);
        }
      );
  }

  public chooseBooks(value){
    this.bookId = value.value;
    this.showBook();

  }

  public chooseUser(value){
    this.userId = value.value;
    this.showBookshelfList(this.userId);
  }

  public chooseBookshelf(value){
    this.shelfId = value.value;
    this.showBooksList(this.userId,this.shelfId);

  }

  public sendRequest() {
    this.api.user.createBookSharingRequest({ownerUserId:this.userId, book_id: this.bookId})
      .subscribe(
        response=>{

        }
      );
  }



  ngOnInit(){
    this.showUserList();
    this.form = new FormGroup({
      name: new FormControl(''),
      description: new FormControl('')
    });
  }
}
