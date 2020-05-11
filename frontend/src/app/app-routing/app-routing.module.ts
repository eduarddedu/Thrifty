import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PageComponent } from                      '../page/page.component';
import { AccountDetailsComponent } from            '../page-content/details/account-details.component';
import { CategoryDetailsComponent } from           '../page-content/details/category-details.component';
import { CategoryEditComponent } from              '../page-content/category/category-edit.component';
import { CategoryCreateComponent } from            '../page-content/category/category-create.component';
import { ExpenseEditComponent } from               '../page-content/expense/expense-edit.component';
import { ExpenseCreateComponent } from             '../page-content/expense/expense-create.component';
import { LabelCreateComponent } from               '../page-content/label/label-create.component';
import { LabelEditComponent } from                 '../page-content/label/label-edit.component';
import { CategoryUpdatesComponent } from           '../page-content/updates/category-updates.component';
import { LabelUpdatesComponent } from              '../page-content/updates/label-updates.component';
import { PageNotFoundComponent } from              '../page-content/page-not-found/page-not-found.component';

const routes: Routes = [
    {
        path: '',
        redirectTo: 'account', pathMatch: 'full',
    },
    {
        path: '',
        component: PageComponent,
        children: [
            {
                path: 'account',
                component: AccountDetailsComponent
            },
            {
                path: 'category/:id',
                component: CategoryDetailsComponent
            },
            {
                path: 'new/expense',
                component: ExpenseCreateComponent
            },
            {
                path: 'new/category',
                component: CategoryCreateComponent
            },
            {
                path: 'new/label',
                component: LabelCreateComponent
            },
            {
                path: 'edit/label',
                component: LabelEditComponent

            },
            {
                path: 'edit/category',
                component: CategoryEditComponent,
            },
            {
                path: 'edit/expense',
                component: ExpenseEditComponent
            },
            {
                path: 'update/labels',
                component: LabelUpdatesComponent
            },
            {
                path: 'update/categories',
                component: CategoryUpdatesComponent
            },
            {
                path: '**',
                component: PageNotFoundComponent,
            }]
    }

];

@NgModule({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ]
})
export class AppRoutingModule { }
