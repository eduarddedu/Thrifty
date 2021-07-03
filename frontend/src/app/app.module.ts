import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {
    HttpClientModule,
    HTTP_INTERCEPTORS
} from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { DataTablesModule } from 'angular-datatables';
import { NgxMyDatePickerModule } from 'ngx-mydatepicker';
import { BsModalModule } from 'ng2-bs3-modal';
import { ChartModule } from 'angular-highcharts';

import { AppRoutingModule } from './app-routing/app-routing.module';
import { AppComponent } from './app.component';
import { PageComponent } from './page/page.component';
import { AccountDetailsComponent } from './page-content/view/account-details/account-details.component';
import { CategoryDetailsComponent } from './page-content/view/category-details/category-details.component';
import { LabelDetailsComponent } from './page-content/view/label-details/label-details.component';
import { EditExpenseComponent } from './page-content/forms/expense/edit-expense.component';
import { CreateExpenseComponent } from './page-content/forms/expense/create-expense.component';
import { CreateCategoryComponent } from './page-content/forms/category/create-category.component';
import { EditCategoryComponent } from './page-content/forms/category/edit-category.component';
import { CreateLabelComponent } from './page-content/forms/label/create-label.component';
import { EditLabelComponent } from './page-content/forms/label/edit-label.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { ExpenseTableComponent } from './expense-table/expense-table.component';
import { DropdownSelectorComponent } from './dropdown-selector/dropdown-selector.component';
import { RadioSelectorComponent } from './radio-selector/radio-selector.component';
import { NotificationComponent } from './notification/notification.component';
import { RestService } from './services/rest.service';
import { NotificationService } from './services/notification.service';
import { DeleteEntityModalService } from './services/modal.service';
import { AccountService } from './services/account.service';
import { SessionTimeoutService } from './services/timeout.service';
import { RestApiRequestInterceptor } from './http/request.interceptor';
import { ErrorInterceptor } from './http/error.interceptor';
import { DeleteEntityComponent } from './page-content/delete-entity/delete-entity.component';
import { DeleteEntityModalComponent } from './page-content/delete-entity/delete-entity-modal.component';
import { SpinnerComponent } from './spinner/spinner.component';
import { MenuComponent} from './page-content/menu/menu.component';



@NgModule({
    declarations: [
        AppComponent,
        PageComponent,
        AccountDetailsComponent,
        CategoryDetailsComponent,
        CreateExpenseComponent,
        EditExpenseComponent,
        CreateExpenseComponent,
        ExpenseTableComponent,
        DeleteEntityModalComponent,
        DropdownSelectorComponent,
        RadioSelectorComponent,
        EditCategoryComponent,
        SidebarComponent,
        NotificationComponent,
        CreateCategoryComponent,
        CreateLabelComponent,
        EditLabelComponent,
        LabelDetailsComponent,
        DeleteEntityComponent,
        SpinnerComponent,
        MenuComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule,
        DataTablesModule,
        ReactiveFormsModule,
        NgxMyDatePickerModule.forRoot(),
        BsModalModule,
        ChartModule
    ],
    providers: [
        RestService,
        NotificationService,
        DeleteEntityModalService,
        AccountService,
        SessionTimeoutService,
        { provide: HTTP_INTERCEPTORS, useClass: RestApiRequestInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
