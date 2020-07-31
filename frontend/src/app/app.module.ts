import { BrowserModule } from                      '@angular/platform-browser';
import { NgModule }      from                      '@angular/core';
import { HttpClientModule,
    HTTP_INTERCEPTORS } from                       '@angular/common/http';
import { ReactiveFormsModule }   from              '@angular/forms';
import { DataTablesModule } from                   'angular-datatables';
import { NgxMyDatePickerModule } from              'ngx-mydatepicker';
import { BsModalModule } from                      'ng2-bs3-modal';
import { ChartModule } from                        'angular-highcharts';

import { AppRoutingModule } from                   './app-routing/app-routing.module';
import { AppComponent } from                       './app.component';
import { PageComponent } from                      './page/page.component';
import { AccountDetailsComponent } from            './page-content/details/account-details.component';
import { CategoryDetailsComponent } from           './page-content/details/category-details.component';
import { ExpenseEditComponent } from               './page-content/expense/expense-edit.component';
import { ExpenseCreateComponent } from             './page-content/expense/expense-create.component';
import { PageNotFoundComponent } from              './page-content/page-not-found/page-not-found.component';
import { CategoryCreateComponent } from            './page-content/category/category-create.component';
import { CategoryEditComponent } from              './page-content/category/category-edit.component';
import { LabelCreateComponent } from               './page-content/label/label-create.component';
import { LabelEditComponent } from                 './page-content/label/label-edit.component';
import { LabelUpdatesComponent } from              './page-content/updates/label-updates.component';
import { CategoryUpdatesComponent } from           './page-content/updates/category-updates.component';
import { SidebarComponent } from                   './sidebar/sidebar.component';
import { ExpenseTableComponent } from              './expense-table/expense-table.component';
import { DropdownSelectorComponent } from          './dropdown-selector/dropdown-selector.component';
import { RadioSelectorComponent } from             './radio-selector/radio-selector.component';
import { NotificationComponent } from              './notification/notification.component';
import { ModalComponent } from                     './modal/modal.component';
import { RestService } from                        './services/rest.service';
import { MessageService } from                     './services/messages.service';
import { ChartsService } from                      './services/charts.service';
import { AnalyticsService } from                   './services/analytics.service';
import { BaseUriInterceptor } from                 './http/baseuri.interceptor';
import { ErrorInterceptor } from                   './http/error.interceptor';



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
        ModalComponent,
        DropdownSelectorComponent,
        RadioSelectorComponent,
        CategoryEditComponent,
        SidebarComponent,
        NotificationComponent,
        PageNotFoundComponent,
        CategoryCreateComponent,
        LabelUpdatesComponent,
        LabelCreateComponent,
        CategoryUpdatesComponent,
        LabelEditComponent
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
        MessageService,
        ChartsService,
        AnalyticsService,
        { provide: HTTP_INTERCEPTORS, useClass: BaseUriInterceptor, multi: true},
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
