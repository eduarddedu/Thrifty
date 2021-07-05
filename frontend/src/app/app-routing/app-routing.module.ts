import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PageComponent } from '../page/page.component';
import { AccountDetailsComponent } from '../page-content/view/account-details/account-details.component';
import { CategoryDetailsComponent } from '../page-content/view/category-details/category-details.component';
import { LabelDetailsComponent } from '../page-content/view/label-details/label-details.component';
import { EditCategoryComponent } from '../page-content/forms/category/edit-category.component';
import { CreateCategoryComponent } from '../page-content/forms/category/create-category.component';
import { EditExpenseComponent } from '../page-content/forms/expense/edit-expense.component';
import { CreateExpenseComponent } from '../page-content/forms/expense/create-expense.component';
import { CreateLabelComponent } from '../page-content/forms/label/create-label.component';
import { EditLabelComponent } from '../page-content/forms/label/edit-label.component';
import { DeleteEntityComponent } from '../page-content/delete-entity/delete-entity.component';
import { SettingsComponent } from '../page-content/settings/settings.component';

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
                        component: CreateExpenseComponent
                    },
                    {
                        path: 'category',
                        component: CreateCategoryComponent
                    },
                    {
                        path: 'label',
                        component: CreateLabelComponent
                    }
                ]
            },
            {
                path: 'edit',
                children: [
                    {
                        path: 'label/:id',
                        component: EditLabelComponent
                    },
                    {
                        path: 'category/:id',
                        component: EditCategoryComponent,
                    },
                    {
                        path: 'expense/:id',
                        component: EditExpenseComponent
                    }
                ]

            },
            {
                path: 'delete/:entity/:id',
                component: DeleteEntityComponent
            },
            {
                path: 'settings',
                component: SettingsComponent

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
