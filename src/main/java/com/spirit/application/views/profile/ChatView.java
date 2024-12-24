package com.spirit.application.views.profile;

import com.spirit.application.dto.ChatMessageDTO;
import com.spirit.application.dto.UserDTO;
import com.spirit.application.entitiy.ChatMessage;
import com.spirit.application.entitiy.User;
import com.spirit.application.repository.UserRepository;
import com.spirit.application.service.ChatService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import com.vaadin.flow.component.notification.Notification;
import jakarta.annotation.security.RolesAllowed;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@CssImport("./themes/spirit/views/ChatView.css")
@RolesAllowed({Globals.Roles.STUDENT, Globals.Roles.UNTERNEHMEN})
@Route(value = Globals.Pages.CHAT, layout = AppView.class)
@PageTitle("Chat")
public class ChatView extends VerticalLayout implements HasUrlParameter<String> {
    private final ChatService chatService;
    private final SessionService sessionService;
    private final UserRepository userRepository;

    private MessageList messageList;
    private TextArea messageInput;
    private ComboBox<User> userSelect;
    private User selectedUser;
    private Consumer<ChatMessageDTO> messageListener;

    public ChatView(ChatService chatService,
                    SessionService sessionService,
                    UserRepository userRepository) {
        this.chatService = chatService;
        this.sessionService = sessionService;
        this.userRepository = userRepository;

        setupLayout();
        setupMessageList();
        setupControls();
        setupMessageListener();
    }

    private void setupLayout() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().set("background", "var(--lumo-shade-10pct)");
    }

    private void setupMessageList() {
        messageList = new MessageList();
        messageList.setSizeFull();
        messageList.getStyle()
                .set("border-radius", "8px")
                .set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)")
                .set("background", "white")
                .set("padding", "10px");

        add(messageList);
        setFlexGrow(1, messageList);
    }

    private void setupControls() {
        userSelect = new ComboBox<>();
        userSelect.setPlaceholder("Select user");
        userSelect.setWidth("200px");
        userSelect.setItems(userRepository.findAll().stream()
                .filter(user -> user.getUserID() != sessionService.getCurrentUser().getUserID())
                .collect(Collectors.toList()));
        userSelect.setItemLabelGenerator(user -> user.getUsername() + " (" + user.getEmail() + ")");

        userSelect.addValueChangeListener(event -> {
            selectedUser = event.getValue();
            refreshMessages();
        });

        messageInput = new TextArea();
        messageInput.setPlaceholder("Enter message...");
        messageInput.setWidthFull();
        messageInput.getStyle().set("border-radius", "8px")
                .set("padding", "10px");

        Button sendButton = new Button("Send", e -> sendMessage());
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendButton.getStyle().set("background", "var(--lumo-primary-color)")
                .set("color", "white");

        HorizontalLayout controls = new HorizontalLayout(userSelect, messageInput, sendButton);
        controls.setWidthFull();
        controls.setAlignItems(Alignment.CENTER);
        controls.setPadding(true);
        controls.getStyle().set("background", "white")
                .set("border-top", "1px solid var(--lumo-contrast-10pct)")
                .set("padding", "10px");

        add(controls);
    }

    private void setupMessageListener() {
        messageListener = message -> {
            UserDTO currentUser = sessionService.getCurrentUser();
            if (selectedUser != null &&
                    (message.getSender().getUserID() == selectedUser.getUserID() ||
                            message.getRecipient().getUserID() == selectedUser.getUserID())) {
                getUI().ifPresent(ui -> ui.access(this::refreshMessages));
            }
        };
        chatService.addMessageListener(messageListener);
    }

    private void sendMessage() {
        if (selectedUser == null || messageInput.isEmpty()) {
            Notification.show("Please select a user and enter a message");
            return;
        }

        UserDTO currentUser = sessionService.getCurrentUser();
        chatService.sendMessage(
                currentUser.getUserID(),
                selectedUser.getUserID(),
                messageInput.getValue()
        );

        messageInput.clear();
        refreshMessages();
    }

    private void refreshMessages() {
        if (selectedUser != null) {
            UserDTO currentUser = sessionService.getCurrentUser();

            List<ChatMessage> messages = chatService.getChatHistory(
                    currentUser.getUserID(),
                    selectedUser.getUserID()
            );
            List<MessageListItem> messageItems = messages.stream()
                    .map(message -> {
                        boolean isCurrentUser = message.getSender().getUserID() == currentUser.getUserID();

                        // Create the message item
                        MessageListItem messageItem = new MessageListItem(
                                message.getContent(),
                                message.getTimestamp().atZone(ZoneId.systemDefault()).toInstant(),
                                isCurrentUser ? "Me" : message.getSender().getUsername()
                        );

                        // Add a CSS class to differentiate sender and recipient
                        messageItem.setUserColorIndex(isCurrentUser ? 1 : 2);

                        // Optionally add an avatar for recipient messages
                        if (!isCurrentUser && message.getSender().getProfile() != null &&
                                message.getSender().getProfile().getAvatar() != null &&
                                !message.getSender().getProfile().getAvatar().isEmpty()) {
                            Avatar avatar = new Avatar();
                            avatar.setImage(message.getSender().getProfile().getAvatar());
                            avatar.setName(message.getSender().getUsername());
                            messageItem.setUserImage(String.valueOf(avatar));
                        }

                        return messageItem;
                    })
                    .collect(Collectors.toList());

            messageList.setItems(messageItems);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        chatService.removeMessageListener(messageListener);
        setupMessageListener();

        UI ui = attachEvent.getUI();
        UserDTO currentUser = sessionService.getCurrentUser();

        messageListener = message -> {
            if (selectedUser != null &&
                    (message.getSender().getUserID() == selectedUser.getUserID() ||
                            message.getRecipient().getUserID() == currentUser.getUserID())) {
                ui.access(() -> {
                    refreshMessages();
                    ui.push();
                });
            }
        };
        chatService.addMessageListener(messageListener);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        chatService.removeMessageListener(messageListener);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            try {
                long userId = Long.parseLong(parameter);
                this.selectedUser = userRepository.findById(userId)
                        .orElse(null);

                if (selectedUser != null) {
                    userSelect.setValue(selectedUser);
                    refreshMessages(); // Nachrichten für den Benutzer laden
                } else {
                    Notification.show("User not found");
                    userSelect.clear();
                }
            } catch (NumberFormatException e) {
                Notification.show("Invalid user ID");
            }
        }
    }
}