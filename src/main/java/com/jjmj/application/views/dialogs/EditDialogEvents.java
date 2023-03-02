package com.jjmj.application.views.dialogs;

import com.vaadin.flow.component.ComponentEvent;

public class EditDialogEvents {
    public abstract static class EditDialogEvent extends ComponentEvent<EditDialog<?>> {

        protected EditDialogEvent(EditDialog source) {
            super(source, false);
        }
    }

    public static class SaveEvent extends EditDialogEvent {
        public SaveEvent(EditDialog source) {
            super(source);
        }
    }

    public static class DeleteEvent extends EditDialogEvent {
        public DeleteEvent(EditDialog source) {
            super(source);
        }
    }

    public static class CloseEvent extends EditDialogEvent {
        public CloseEvent(EditDialog source) {
            super(source);
        }
    }
}

