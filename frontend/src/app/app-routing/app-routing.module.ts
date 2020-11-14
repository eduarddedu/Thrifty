import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PageComponent } from '../page/page.component';
import { AccountDetailsComponent } from '../page-content/details/account-details.component';
import { CategoryDetailsComponent } from '../page-content/details/category-details.component';
import { LabelDetailsComponent } from '../page-content/details/label-details.component';
import { CategoryEditComponent } from '../page-content/category/category-edit.component';
import { CategoryCreateComponent } from '../page-content/category/category-create.component';
import { ExpenseEditComponent } from '../page-content/expense/expense-edit.component';
import { ExpenseCreateComponent } from '../page-content/expense/expense-create.component';
import { LabelCreateComponent } from '../page-content/label/label-create.component';
import { LabelEditComponent } from '../page-content/label/label-edit.component';
import { DeleteEntityComponent } from '../page-content/delete-entity/delete-entity.component';
import { PageNotFoundComponent } from '../page-content/page-not-found/page-not-found.component';

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
                path: 'label/:id',
                component: LabelDetailsComponent
            },
            {
                path: 'new',
                children: [
                    {
                        path: 'expense',
                        component: ExpenseCreateComponent
                    },
                    {
                        path: 'category',
                        component: CategoryCreateComponent
                    },
                    {
                        path: 'label',
                        component: LabelCreateComponent
                    }
                ]
            },
            {
                path: 'edit',
                children: [
                    {
                        path: 'label/:id',
                        component: LabelEditComponent
                    },
                    {
                        path: 'category/:id',
                        component: CategoryEditComponent,
                    },
                    {
                        path: 'expense/:id',
                        component: ExpenseEditComponent
                    }
                ]

            },
            {
                path: 'delete/:entity/:id',
                component: DeleteEntityComponent
            },
            {
                path: '**',
                component: PageNotFoundComponent,
            }]
    }

];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
