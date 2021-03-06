import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PageComponent } from '../page/page.component';
import { AccountDetailsComponent } from '../page-content/view/account-details/account-details.component';
import { CategoryDetailsComponent } from '../page-content/view/category-details/category-details.component';
import { LabelDetailsComponent } from '../page-content/view/label-details/label-details.component';
import { CategoryEditComponent } from '../page-content/forms/category/category-edit.component';
import { CategoryCreateComponent } from '../page-content/forms/category/category-create.component';
import { ExpenseEditComponent } from '../page-content/forms/expense/expense-edit.component';
import { ExpenseCreateComponent } from '../page-content/forms/expense/expense-create.component';
import { LabelCreateComponent } from '../page-content/forms/label/label-create.component';
import { LabelEditComponent } from '../page-content/forms/label/label-edit.component';
import { DeleteEntityComponent } from '../page-content/delete-entity/delete-entity.component';

const routes: Routes = [
    {
        path: '',
        redirectTo: 'view/account',
        pathMatch: 'full'
    },
    {
        path: '',
        component: PageComponent,
        children: [
            {
                path: 'view/account',
                component: AccountDetailsComponent
            },
            {
                path: 'view/category/:id',
                component: CategoryDetailsComponent
            },
            {
                path: 'view/label/:id',
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
                redirectTo: '/error', pathMatch: 'full'
            }]
    }

];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
