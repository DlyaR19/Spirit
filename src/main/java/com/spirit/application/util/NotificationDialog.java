package com.spirit.application.util;

import com.spirit.application.entitiy.Notification;
import com.spirit.application.entitiy.User;
import com.spirit.application.service.NotificationService;
import com.spirit.application.service.SessionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class NotificationDialog extends Dialog {

    private final NotificationService notificationService;
    private final SessionService sessionService;
    private final VerticalLayout notificationLayout; // Layout für Benachrichtigungen

    public NotificationDialog(NotificationService notificationService, SessionService sessionService) {
        this.notificationService = notificationService;
        this.sessionService = sessionService;

        setHeaderTitle("Notifications");
        setWidth("400px");

        // Layout für Benachrichtigungen initialisieren
        notificationLayout = new VerticalLayout();
        notificationLayout.setSpacing(true);
        notificationLayout.setPadding(false);

        // Schließen-Button einmalig hinzufügen
        Button closeButton = new Button("close", e -> close());
        Button markAllAsReadButton = new Button("Mark All as Read", e -> notificationService.markAllAsRead(sessionService.getCurrentUser().getUser().getUserID()));
        getFooter().add(closeButton, markAllAsReadButton);

        // Benachrichtigungen laden
        refreshNotifications();
    }

    public void refreshNotifications() {
        notificationLayout.removeAll(); // Alte Benachrichtigungen entfernen

        User currentUser = sessionService.getCurrentUser().getUser();
        if (currentUser != null) {
            List<Notification> notifications = notificationService.getUnreadNotifications(currentUser.getUserID());

            if (notifications.isEmpty()) {
                notificationLayout.add(new Span("No New Notifications."));
            } else {
                for (Notification notification : notifications) {
                    HorizontalLayout notificationItem = new HorizontalLayout();
                    notificationItem.setAlignItems(FlexComponent.Alignment.CENTER);

                    Span message = new Span(notification.getMessage());
                    Button markAsRead = new Button("mark as Read ", e -> {
                        notificationService.markAsRead(notification);
                        refreshNotifications(); // Benachrichtigungen aktualisieren
                    });

                    notificationItem.add(message, markAsRead);
                    notificationLayout.add(notificationItem);
                }
            }
        }

        // Benachrichtigungs-Layout zum Dialog hinzufügen (falls noch nicht geschehen)
        if (getChildren().noneMatch(component -> component == notificationLayout)) {
            add(notificationLayout);
        }
    }
}