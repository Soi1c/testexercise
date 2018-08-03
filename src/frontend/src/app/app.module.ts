import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {ButtonComponent} from './components/buttons';
import {InputTextComponent} from './components/inputs';
import {InputTextErrorsComponent} from './components/inputs';
import {TextMaskModule} from 'angular2-text-mask';
import {ErrorComponent} from './components/error/error.component';
import {UtilityService} from './services/utility';
import {AuthComponent} from './components/auth/auth.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CaptchaComponent} from './components/captcha/captcha.component';
import {LayoutComponent} from './components/layout/layout.component';
import {UserComponent} from './pages/user/user.component';
import {ApiService} from "./services/api.service";
import {HttpModule} from "@angular/http";
import {AppRoutingModule} from "./app.routes";
import {ReadBookComponent} from "./pages/user/readBookPage/readBook.component";
import {BookshelvesComponent} from "./pages/user/bookshelves/bookshelves.component";
import {TableWrapper} from "./components/table/table.component";
import {TableHeaderLocalization} from "../pipes/table-header-names-localization/pipe";
import {MdPaginatorModule, MdTableModule} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {SinglebookshelfComponent} from "./pages/user/bookshelves/singlebookshelf/singlebookshelf.component";



@NgModule({
  declarations: [
    AppComponent,
    ButtonComponent,
    InputTextComponent,
    ErrorComponent,
    InputTextErrorsComponent,
    AuthComponent,
    CaptchaComponent,
    LayoutComponent,
    UserComponent,
    ReadBookComponent,
    BookshelvesComponent,
    TableWrapper,
    SinglebookshelfComponent,
    TableHeaderLocalization
  ],
  imports: [
    TextMaskModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpModule,
    AppRoutingModule,
    MdTableModule,
    MdPaginatorModule
  ],
  providers: [
    UtilityService,
    ApiService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
