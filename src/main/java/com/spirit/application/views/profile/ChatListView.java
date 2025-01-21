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

/**
 * This class represents the Chat List View in the application, where users can see a list of active chats
 * and start new conversations. It displays a grid of existing chats, allows searching for users,
 * and supports navigation to the Chat View for messaging.
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @RolesAllowed}: Restricts access to users with the roles of STUDENT or UNTERNEHMEN.</li>
 *   <li>{@code @Route}: Specifies the routing path for this view as {@code Globals.Pages.CHATLIST} and
 *       associates it with the {@code AppView} layout.</li>
 *   <li>{@code @CssImport}: Links to the CSS file for styling the Chat List View.</li>
 *   <li>{@code @PageTitle}: Sets the page title to "Chat List".</li>
 * </ul>
 */
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

    /**
     * Constructs a new ChatListView.
     * @param chatListService  the service managing the list of chats.
     * @param sessionService   the service handling user sessions.
     * @param userRepository   the repository for accessing user data.
     * @param chatService      the service for handling chat messages.
     */
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

    /**
     * Creates the header for the Chat List View, including a title and a "New Chat" button.
     * @return the component representing the header.
     */
    private Component createHeader() {
        H2 title = new H2("Chats");
        Button newChatBtn = new Button("Neuer Chat", VaadinIcon.PLUS.create());
        newChatBtn.addClickListener(e -> showUserSelectionDialog());

        HorizontalLayout header = new HorizontalLayout(title, newChatBtn);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);
        return header;
    }

    /**
     * Opens a dialog for selecting a user to start a new chat.
     */
    private void showUserSelectionDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Neuen Chat starten");

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
        searchField.setPlaceholder("Nach Users suchen ...");
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
        Button closeButton = new Button("Abbrechen", e -> dialog.close());
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

    /**
     * Configures the chat grid, defining its structure and item click behavior.
     */
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

    /**
     * Formats a timestamp to display relative time (e.g., "Today", "Yesterday") or date.
     * @param timestamp the timestamp to format.
     * @return the formatted timestamp as a string.
     */
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

    /**
     * Refreshes the chat grid with the latest chat data from the ChatListService.
     */
    private void refreshChats() {
        List<ChatListDTO> chats = chatListService.getChatsForUser(
                sessionService.getCurrentUser().getUserID()
        );
        chatGrid.setItems(chats);
    }

    /**
     * Handles UI updates when the view is attached to the UI.
     * @param attachEvent the event triggered when the view is attached.
     */
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

    /**
     * Cleans up listeners and resources when the view is detached from the UI.
     * @param detachEvent the event triggered when the view is detached.
     */
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        chatListService.removeUpdateListener(chatListUpdateListener);
        chatService.removeMessageListener(messageListener);
    }

    /**
     * Adds listeners for real-time chat updates and message notifications.
     */
    private void setupPushUpdates() {
        chatListService.addUpdateListener(update -> {
            getUI().ifPresent(ui -> ui.access(this::refreshChats));
        });
    }
}
