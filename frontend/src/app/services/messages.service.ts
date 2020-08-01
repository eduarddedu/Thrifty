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
            /* Create operation
            */
            case Kind.CATEGORY_CREATE_OK:
            text = 'Category created.';
            alert = Alert.SUCCESS;
            break;
            case Kind.EXPENSE_CREATE_OK:
            text = 'Expense created.';
            alert = Alert.SUCCESS;
            break;
            case Kind.LABEL_CREATE_OK:
            text = 'Label created.';
            alert = Alert.SUCCESS;
            break;

            /* Edit operation
            */
            case Kind.CATEGORY_EDIT_OK:
            text = 'Category updated.';
            alert = Alert.SUCCESS;
            break;
            case Kind.EXPENSE_EDIT_OK:
            text = 'Expense updated.';
            alert = Alert.SUCCESS;
            break;
            case Kind.LABEL_EDIT_OK:
            text = 'Label updated.';
            alert = Alert.SUCCESS;
            break;

            /* Delete operation
            */
            case Kind.CATEGORY_DELETE_OK:
            text = 'Category deleted.';
            alert = Alert.SUCCESS;
            break;
            case Kind.EXPENSE_DELETE_OK:
            text = 'Expense deleted.';
            alert = Alert.SUCCESS;
            break;
            case Kind.LABEL_DELETE_OK:
            text = 'Label deleted.';
            alert = Alert.SUCCESS;
            break;

            /* Delete warning
            */
            case Kind.CATEGORY_DELETE_WARN:
            text = 'Deleting a category which is associated to an expense may break the app. Proceed?';
            alert = Alert.WARNING;
            break;
            case Kind.EXPENSE_DELETE_WARN:
            text = 'Expense will be deleted. Proceed?';
            alert = Alert.WARNING;
            break;
            case Kind.LABEL_DELETE_WARN:
            text = 'This only deletes the label. Associated expenses, if any, will stick around. Proceed?';
            alert = Alert.WARNING;

        }
        return {
            kind: kind,
            text: text,
            alert: alert
        };

    }
}
