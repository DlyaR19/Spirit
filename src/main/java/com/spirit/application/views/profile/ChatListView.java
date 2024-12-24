package com.spirit.application.views.profile;


import com.spirit.application.dto.ChatListDTO;
import com.spirit.application.dto.ChatMessageDTO;
import com.spirit.application.entitiy.User;
import com.spirit.application.repository.UserRepository;
import com.spirit.application.service.ChatListService;
import com.spirit.application.service.ChatService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RolesAllowed({Globals.Roles.STUDENT, Globals.Roles.UNTERNEHMEN})
@Route(value = Globals.Pages.CHATLIST, layout = AppView.class)
@CssImport("./themes/spirit/views/ChatListView.css")
@PageTitle("Chat List")
public class ChatListView extends VerticalLayout {
    private final ChatListService chatListService;
    private final SessionService sessionService;
    private final Grid<ChatListDTO> chatGrid;
    private final UserRepository userRepository;
    private Consumer<ChatListDTO> chatListUpdateListener;
    private Consumer<ChatMessageDTO> messageListener;
    private final ChatService chatService;

    public ChatListView(ChatListService chatListService, SessionService sessionService, UserRepository userRepository, ChatService chatService) {
        this.chatListService = chatListService;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.chatService = chatService;

        setSizeFull();
        setPadding(true);

        chatGrid = new Grid<>();
        setupGrid();

        add(createHeader(), chatGrid);
        refreshChats();
        setupPushUpdates();
    }

    private Component createHeader() {
        H2 title = new H2("Chats");
        Button newChatBtn = new Button("New Chat", VaadinIcon.PLUS.create());
        newChatBtn.addClickListener(e -> showUserSelectionDialog());

        HorizontalLayout header = new HorizontalLayout(title, newChatBtn);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);
        return header;
    }

    private void showUserSelectionDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Start New Chat");

        // Create a user grid
        Grid<User> userGrid = new Grid<>();
        userGrid.setWidthFull();
        userGrid.addColumn(User::getUsername).setHeader("Username");
        userGrid.addColumn(User::getEmail).setHeader("Email");

        // Get all users except current user
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getUserID() != sessionService.getCurrentUser().getUserID())
                .collect(Collectors.toList());
        userGrid.setItems(users);

        // Handle user selection
        userGrid.addItemClickListener(event -> {
            User selectedUser = event.getItem();
            dialog.close();
            UI.getCurrent().navigate(Globals.Pages.CHAT + "/" + selectedUser.getUserID());
        });

        // Add search field
        TextField searchField = new TextField();
        searchField.setWidthFull();
        searchField.setPlaceholder("Search users...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.addValueChangeListener(event -> {
            String searchTerm = event.getValue().toLowerCase();
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(searchTerm) ||
                            user.getEmail().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
            userGrid.setItems(filteredUsers);
        });

        // Add close button
        Button closeButton = new Button("Cancel", e -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Layout setup
        VerticalLayout layout = new VerticalLayout(searchField, userGrid);
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setSizeFull();

        dialog.add(layout);
        dialog.getFooter().add(closeButton);

        // Set dialog size
        dialog.setWidth("500px");
        dialog.setHeight("400px");

        dialog.open();
    }

    private void setupGrid() {
        chatGrid.addClassName("chat-list-grid");
        chatGrid.setHeightFull();

        chatGrid.addColumn(LitRenderer.<ChatListDTO>of(
                        "<div class='chat-list-item'>" +
                                "<div class='avatar'>" +
                                "<div class='avatar-circle'>${item.userInitial}</div>" +
                                "</div>" +
                                "<div class='chat-info'>" +
                                "<div class='chat-header'>" +
                                "<span class='username'>${item.username}</span>" +
                                "<span class='timestamp'>${item.lastMessageTime}</span>" +
                                "</div>" +
                                "<div class='last-message'>${item.lastMessage}</div>" +
                                "</div>" +
                                "</div>"
                        )
                        .withProperty("userInitial", item -> {
                            String username = item.getOtherUser().getUsername();
                            return username != null && !username.isEmpty() ? username.substring(0, 1).toUpperCase() : "?";
                        })
                        .withProperty("username", item -> item.getOtherUser().getUsername())
                        .withProperty("lastMessage", ChatListDTO::getLastMessage)
                        .withProperty("lastMessageTime", item ->
                                formatTimestamp(item.getLastMessageTimestamp()))
        ).setAutoWidth(true).setFlexGrow(1);

        chatGrid.addItemClickListener(event -> {
            UI.getCurrent().navigate(
                    Globals.Pages.CHAT + "/" + event.getItem().getOtherUser().getUserID()
            );
        });
    }

    private String formatTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) return "";

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDate today = now.toLocalDate();

        ZonedDateTime zonedDateTime = timestamp.atZone(ZoneId.systemDefault());

        if (zonedDateTime.toLocalDate().equals(today)) {
            return zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (zonedDateTime.toLocalDate().equals(today.minusDays(1))) {
            return "Yesterday";
        } else if (zonedDateTime.getYear() == now.getYear()) {
            return zonedDateTime.format(DateTimeFormatter.ofPattern("MMM d"));
        } else {
            return zonedDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        }
    }

    private void refreshChats() {
        List<ChatListDTO> chats = chatListService.getChatsForUser(
                sessionService.getCurrentUser().getUserID()
        );
        chatGrid.setItems(chats);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();

        // Listen for chat list updates
        chatListUpdateListener = chatList -> {
            ui.access(() -> {
                refreshChats();
                ui.push();
            });
        };
        chatListService.addUpdateListener(chatListUpdateListener);

        // Listen for new messages
        messageListener = message -> {
            ui.access(() -> {
                refreshChats();
                ui.push();
            });
        };
        chatService.addMessageListener(messageListener);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        chatListService.removeUpdateListener(chatListUpdateListener);
        chatService.removeMessageListener(messageListener);
    }

    private void setupPushUpdates() {
        chatListService.addUpdateListener(update -> {
            getUI().ifPresent(ui -> ui.access(this::refreshChats));
        });
    }
}
