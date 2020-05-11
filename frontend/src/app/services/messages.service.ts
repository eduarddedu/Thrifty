import { Injectable } from '@angular/core';

export interface Message {
    kind: Kind;
    text: string;
    alert: Alert;
}

export enum Alert {
    INFO, ERROR, WARNING, SUCCESS
}


export enum Kind {
    IN_PROGRESS,
    UNEXPECTED_ERROR,
    EMPTY_ACCOUNT_ERROR,
    NO_LABELS_ERROR,
    WEB_SERVICE_OFFLINE,
    CATEGORY_CREATE_OK,
    EXPENSE_CREATE_OK,
    LABEL_CREATE_OK,
    CATEGORY_DELETE_OK,
    EXPENSE_DELETE_OK,
    CATEGORY_DELETE_WARN,
    EXPENSE_DELETE_WARN,
    LABEL_DELETE_WARN,
    LABEL_DELETE_OK,
    CATEGORY_EDIT_OK,
    EXPENSE_EDIT_OK,
    LABEL_EDIT_OK
}

@Injectable()
export class MessageService {

    private catalog: Map<Kind, Message> = new Map();

    private kinds: Kind[] = Object.keys(Kind).map(key => Kind[key]);

    constructor() {
        this.kinds.forEach(kind => this.catalog.set(kind, this.createMessageObject(kind)));
    }

    public get(kind: Kind) {
        return Object.assign(this.catalog.get(kind));
    }

    private createMessageObject(kind: Kind) {
        let text: string, alert: Alert;

        switch (kind) {
            /* Application error
            */
            case Kind.IN_PROGRESS:
            text = 'Waiting for backend...';
            alert = Alert.INFO;
            break;
            case Kind.UNEXPECTED_ERROR:
            text = 'An unexpected error has occurred.';
            alert = Alert.ERROR;
            break;
            case Kind.WEB_SERVICE_OFFLINE:
            text = 'A server-side problem has occured. Please retry later.';
            alert = Alert.ERROR;
            break;
            /* Missing data errors
            */
            case Kind.EMPTY_ACCOUNT_ERROR:
            text = 'No expenses are present on your account';
            alert = Alert.ERROR;
            break;
            case Kind.NO_LABELS_ERROR:
            text = 'You haven\'t defined any labels yet. To define a category, please create a label first.',
            alert = Alert.ERROR;
            break;
            /* Create operation
            */
            case Kind.CATEGORY_CREATE_OK:
            text = 'Category has been created.';
            alert = Alert.SUCCESS;
            break;
            case Kind.EXPENSE_CREATE_OK:
            text = 'Expense has been created.';
            alert = Alert.SUCCESS;
            break;
            case Kind.LABEL_CREATE_OK:
            text = 'Label has been created.';
            alert = Alert.SUCCESS;
            break;

            /* Edit operation
            */
            case Kind.CATEGORY_EDIT_OK:
            text = 'Category has been updated.';
            alert = Alert.SUCCESS;
            break;
            case Kind.EXPENSE_EDIT_OK:
            text = 'Expense has been updated.';
            alert = Alert.SUCCESS;
            break;
            case Kind.LABEL_EDIT_OK:
            text = 'Label has been updated.';
            alert = Alert.SUCCESS;
            break;

            /* Delete operation
            */
            case Kind.CATEGORY_DELETE_OK:
            text = 'Category has been deleted.';
            alert = Alert.SUCCESS;
            break;
            case Kind.EXPENSE_DELETE_OK:
            text = 'Expense has been deleted.';
            alert = Alert.SUCCESS;
            break;
            case Kind.LABEL_DELETE_OK:
            text = 'Label has been deleted.';
            alert = Alert.SUCCESS;
            break;

            /* Delete warning
            */
            case Kind.CATEGORY_DELETE_WARN:
            text = 'Only the category will be deleted. Assigned labels and expenses, if any, will stick around. Proceed?';
            alert = Alert.WARNING;
            break;
            case Kind.EXPENSE_DELETE_WARN:
            text = 'Expense will be deleted. Proceed?';
            alert = Alert.WARNING;
            break;
            case Kind.LABEL_DELETE_WARN:
            text = 'Only the label will be deleted. Associated expenses will stick around. Proceed?';
            alert = Alert.WARNING;

        }
        return {
            kind: kind,
            text: text,
            alert: alert
        };

    }
}
