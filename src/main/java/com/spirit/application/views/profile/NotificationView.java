package com.spirit.application.views.profile;

import com.spirit.application.dto.UserDTO;
import com.spirit.application.repository.NotificationRepository;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.spirit.application.service.NotificationService;
import com.spirit.application.service.SessionService;

import java.util.List;

@Route(value = "notifications", layout = AppView.class) // Linking with AppView
@PageTitle("Notifications")
@Component
public class NotificationView extends VerticalLayout {

    private final NotificationService notificationService;
    private final SessionService sessionService;
    private Grid<com.spirit.application.entitiy.Notification> notificationGrid;
    private Button markAsReadButton;
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationView(NotificationService notificationService, SessionService sessionService, NotificationRepository notificationRepository) {
        this.notificationService = notificationService;
        this.sessionService = sessionService;
        this.notificationRepository = notificationRepository;
        setupLayout();
        setupGrid();  // Do not access session here
        setupMarkAsReadButton();
    }

    /**
     * Configures the layout settings for the notification view.
     */
    private void setupLayout() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().set("background", "var(--lumo-shade-10pct)");
    }

    /**
     * Sets up the notification grid to display the list of notifications.
     */
    private void setupGrid() {
        notificationGrid = new Grid<>(com.spirit.application.entitiy.Notification.class);
        notificationGrid.setSizeFull();
        notificationGrid.setColumns("message", "timestamp");
        notificationGrid.getColumnByKey("timestamp").setHeader("Date");
        add(notificationGrid);
        setFlexGrow(1, notificationGrid);
    }

    /**
     * Configures the button to mark notifications as read.
     */
    private void setupMarkAsReadButton() {
        markAsReadButton = new Button("Mark all as read", e -> markAllAsRead());
        markAsReadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        markAsReadButton.getStyle().set("background", "var(--lumo-primary-color)");

        HorizontalLayout buttonLayout = new HorizontalLayout(markAsReadButton);
        buttonLayout.setWidthFull();
        buttonLayout.setAlignItems(Alignment.CENTER);

        // Add button to the layout
        add(buttonLayout);
    }

    /**
     * Refreshes the notifications grid by loading unread notifications for the current user.
     */
    public void refreshNotifications() {
        UserDTO currentUser = sessionService.getCurrentUser();
        List<com.spirit.application.entitiy.Notification> unreadNotifications = notificationRepository.findByUser_userIDAndIsRead(currentUser.getUserID(), false);
        UI.getCurrent().access(() -> {
            notificationGrid.setItems(unreadNotifications);
        });
        System.out.println("Unread notifications for user " + currentUser.getUserID() + ": " + unreadNotifications.size());

    }

    /**
     * Marks all notifications as read and updates the notification display.
     */
    private void markAllAsRead() {
        UserDTO currentUser = sessionService.getCurrentUser();
        List<com.spirit.application.entitiy.Notification> unreadNotifications = notificationService.getUnreadNotifications(currentUser.getUserID());

        if (unreadNotifications.isEmpty()) {
            Notification.show("No unread notifications.");
            return;
        }

        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notificationService.saveNotification(notification);
        });

        refreshNotifications();
        Notification.show("All notifications marked as read.");
    }

    /**
     * Called when the view is attached to the UI, to load notifications dynamically.
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        refreshNotifications();
    }
}




