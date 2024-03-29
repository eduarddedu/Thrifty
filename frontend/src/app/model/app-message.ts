
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
    LABEL_DELETE_OK,
    ACCOUNT_DATA_DELETE_OK,
    CATEGORY_EDIT_OK,
    EXPENSE_EDIT_OK,
    LABEL_EDIT_OK,
    CATEGORY_DELETE_WARN,
    EXPENSE_DELETE_WARN,
    LABEL_DELETE_WARN,
    ACCOUNT_DELETE_WARN,
    MUST_CREATE_CATEGORY
}

export class AppMessage {
    private static map: Map<Kind, AppMessage> = (function () {
        const map: Map<Kind, AppMessage> = new Map();
        const kinds: Kind[] = Object.keys(Kind).map(key => Kind[key]);
        kinds.forEach(kind => map.set(kind, new AppMessage(kind)));
        return map;
    })();

    readonly kind: Kind;
    readonly text: string;
    readonly alert: Alert;

    static of(kind: Kind): AppMessage {
        return this.map.get(kind);
    }

    private constructor(kind: Kind) {
        this.kind = kind;
        switch (kind) {

            /* Application error
            */
            case Kind.IN_PROGRESS:
                this.text = 'Waiting for backend...';
                this.alert = Alert.INFO;
                break;
            case Kind.UNEXPECTED_ERROR:
                this.text = 'An unexpected error has occurred.';
                this.alert = Alert.ERROR;
                break;
            case Kind.WEB_SERVICE_OFFLINE:
                this.text = 'Server connection failed. Please come back later.';
                this.alert = Alert.ERROR;
                break;

            /* Create request
            */
            case Kind.CATEGORY_CREATE_OK:
                this.text = 'Category created.';
                this.alert = Alert.SUCCESS;
                break;
            case Kind.EXPENSE_CREATE_OK:
                this.text = 'Expense created.';
                this.alert = Alert.SUCCESS;
                break;
            case Kind.LABEL_CREATE_OK:
                this.text = 'Label created.';
                this.alert = Alert.SUCCESS;
                break;
            case Kind.MUST_CREATE_CATEGORY:
                this.text = 'Please create a category first.';
                this.alert = Alert.INFO;
                break;

            /* Edit request
            */
            case Kind.CATEGORY_EDIT_OK:
                this.text = 'Category updated.';
                this.alert = Alert.SUCCESS;
                break;
            case Kind.EXPENSE_EDIT_OK:
                this.text = 'Expense updated.';
                this.alert = Alert.SUCCESS;
                break;
            case Kind.LABEL_EDIT_OK:
                this.text = 'Label updated.';
                this.alert = Alert.SUCCESS;
                break;

            /* Delete request
            */
            case Kind.CATEGORY_DELETE_OK:
                this.text = 'Category deleted.';
                this.alert = Alert.SUCCESS;
                break;
            case Kind.EXPENSE_DELETE_OK:
                this.text = 'Expense deleted.';
                this.alert = Alert.SUCCESS;
                break;
            case Kind.LABEL_DELETE_OK:
                this.text = 'Label deleted.';
                this.alert = Alert.SUCCESS;
                break;
            case Kind.ACCOUNT_DATA_DELETE_OK:
                this.text = 'Account data has been deleted.';
                this.alert = Alert.SUCCESS;
                break;

            /* Delete warning
            */
            case Kind.CATEGORY_DELETE_WARN:
                this.text = 'Category will be deleted. Continue?';
                this.alert = Alert.WARNING;
                break;
            case Kind.EXPENSE_DELETE_WARN:
                this.text = 'Expense will be deleted. Continue?';
                this.alert = Alert.WARNING;
                break;
            case Kind.LABEL_DELETE_WARN:
                this.text = 'Label will be deleted. Continue?';
                this.alert = Alert.WARNING;
                break;
            case Kind.ACCOUNT_DELETE_WARN:
                this.text = 'All data associated with your account will be permanently deleted. Continue?';
                this.alert = Alert.WARNING;

        }
    }
}


