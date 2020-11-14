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
import { AccountDetailsComponent } from './page-content/details/account-details.component';
import { CategoryDetailsComponent } from './page-content/details/category-details.component';
import { ExpenseEditComponent } from './page-content/expense/expense-edit.component';
import { ExpenseCreateComponent } from './page-content/expense/expense-create.component';
import { PageNotFoundComponent } from './page-content/page-not-found/page-not-found.component';
import { CategoryCreateComponent } from './page-content/category/category-create.component';
import { CategoryEditComponent } from './page-content/category/category-edit.component';
import { LabelCreateComponent } from './page-content/label/label-create.component';
import { LabelEditComponent } from './page-content/label/label-edit.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { ExpenseTableComponent } from './expense-table/expense-table.component';
import { DropdownSelectorComponent } from './dropdown-selector/dropdown-selector.component';
import { RadioSelectorComponent } from './radio-selector/radio-selector.component';
import { NotificationComponent } from './notification/notification.component';
import { RestService } from './services/rest.service';
import { NotificationService } from './services/notification.service';
import { DeleteEntityModalService } from './services/modal.service';
import { AnalyticsService } from './services/analytics.service';
import { BaseUriInterceptor } from './http/baseuri.interceptor';
import { ErrorInterceptor } from './http/error.interceptor';
import { ChartsComponent } from './charts-component/charts-component.component';
import { LabelDetailsComponent } from './page-content/details/label-details.component';
import { DeleteEntityComponent } from './page-content/delete-entity/delete-entity.component';
import { DeleteEntityModalComponent } from './page-content/delete-entity/delete-entity-modal.component';



@NgModule({
    declarations: [
        AppComponent,
        PageComponent,
        AccountDetailsComponent,
        CategoryDetailsComponent,
        ExpenseCreateComponent,
        ExpenseEditComponent,
        ExpenseCreateComponent,
        ExpenseTableComponent,
        DeleteEntityModalComponent,
        DropdownSelectorComponent,
        RadioSelectorComponent,
        CategoryEditComponent,
        SidebarComponent,
        NotificationComponent,
        PageNotFoundComponent,
        CategoryCreateComponent,
        LabelCreateComponent,
        LabelEditComponent,
        ChartsComponent,
        LabelDetailsComponent,
        DeleteEntityComponent
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
        AnalyticsService,
        { provide: HTTP_INTERCEPTORS, useClass: BaseUriInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
